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

/**
 * 
 * @author Dimitry Fingerman
 * @version 1.0.0
 * Implements the functionality to fetch user profile.
 */
@Singleton
public class UserProfileService {
    private final WSClient wsClient;
    private final TwitterAuthenticator twitterAuth;
    private String baseUrl = "https://api.twitter.com";
    
    @Inject
    public UserProfileService(WSClient wsClient, TwitterAuthenticator twitterAuth) {
      this.wsClient = wsClient;
      this.twitterAuth = twitterAuth;
      
    }
    public void setBaseUrl(String url) {
  		this.baseUrl = url;
  	}
    
    /**
     * Entry point for this class file and returns user profile to controller.
     * @param userName
     * @return an instance of UserProfileAndTweets class in the form of completionstage.
     */
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
    
    /**
     * Interacts with twitter api to fetch user profile.
     * @param accessToken
     * @param userName
     * @return an instance of UserProfile class in the form of completionstage.
     */
    
    private CompletionStage<UserProfile> getUserProfile(String accessToken, String userName){
			
		return 
			CompletableFuture.supplyAsync(() -> 
				wsClient
				.url(baseUrl+"/1.1/users/show.json")
		        .addHeader("Authorization", "Bearer " + accessToken)
		        .addQueryParameter("screen_name", userName))
	        .thenCompose(r -> r.get())
	        .thenApply(r -> r.getBody(WSBodyReadables.instance.json()))
	        .thenApply(r -> Json.fromJson(r, UserProfile.class));
    }
    
    /**
     * Interacts with twitter api to fetch 10 tweets based on given username.
     * @param accessToken
     * @param userName
     * @return list of tweets in completionstage format.
     */
    private CompletionStage<List<Tweet>> getUserLastTenTweets(String accessToken, String userName){
		
		return 
			CompletableFuture.supplyAsync(() -> 
				wsClient
				.url(baseUrl+"/1.1/statuses/user_timeline.json")
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