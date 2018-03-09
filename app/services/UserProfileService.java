package services;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.inject.Singleton;

import models.Tweet;
import models.UserProfile;
import models.UserProfileAndTweets;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSClient;

@Singleton
public class UserProfileService {
    private final WSClient wsClient;
    private final TwitterAuthenticator twitterAuth;
    
    @Inject
    public UserProfileService(WSClient wsClient, TwitterAuthenticator twitterAuth) {
      this.wsClient = wsClient;
      this.twitterAuth = twitterAuth;
    }

    public CompletionStage<UserProfileAndTweets> userProfle(String userName) {
    	
    	CompletionStage<String> tokenFuture = twitterAuth.getAccessToken();
    	
    	CompletionStage<UserProfile> userProfileFuture =     			
    			tokenFuture
                .thenCompose(token -> getUserProfile(token, userName));
    	
    	CompletionStage<List<Tweet>> userLastTenTweetsFuture =     			
    			tokenFuture
                .thenCompose(token -> getUserLastTenTweets(token, userName));
    	
    	CompletionStage<UserProfileAndTweets> userProfileAndTweetsFuture = 
    			userProfileFuture.thenCombine(userLastTenTweetsFuture, 
    					(prof, tweets) -> new UserProfileAndTweets(prof,tweets));
    	    	    	
    	return userProfileAndTweetsFuture;
    }
    
    private CompletionStage<UserProfile> getUserProfile(String accessToken, String userName){
			
		return 
			CompletableFuture.supplyAsync(() -> 
				wsClient
				.url("https://api.twitter.com/1.1/users/show.json")
		        .addHeader("Authorization", "Bearer " + accessToken)
		        .addQueryParameter("screen_name", userName))
	        .thenCompose(r -> r.get())
	        .thenApply(r -> r.getBody(WSBodyReadables.instance.json()))
	        .thenApply(r -> Json.fromJson(r, UserProfile.class));
    }
    
    private CompletionStage<List<Tweet>> getUserLastTenTweets(String accessToken, String userName){
		
		return 
			CompletableFuture.supplyAsync(() -> 
				wsClient
				.url("https://api.twitter.com/1.1/statuses/user_timeline.json")
		        .addHeader("Authorization", "Bearer " + accessToken)
		        .addQueryParameter("screen_name", userName)
		        .addQueryParameter("trim_user", "1")
		        .addQueryParameter("count", "10")
		        .addQueryParameter("tweet_mode", "extended"))
			.thenCompose(r -> r.get())
	        .thenApply(r -> r.getBody(WSBodyReadables.instance.json()))
            .thenApply(r -> StreamSupport.stream(r.spliterator(), false)
					.map(x -> Json.fromJson(x, Tweet.class))
					.collect(Collectors.toList()));
    }
}
