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
 * Implements the functionality to fetch user profile info and user's last ten tweets using Twitter API.
 * @author Dmitriy Fingerman
 * @version 1.0.0
 */
@Singleton
public class UserProfileService {
	
	/**
	 * Web services client
	 */
    private final WSClient wsClient;
    
    /**
	 * Service that provides authentication token required to use Twitter API
	 */
    private final TwitterAuthenticator twitterAuth;
    
    /**
	 * Base url of Twitter API
	 */
    private String baseUrl = "https://api.twitter.com";
   
    /**
     * Instantiates new user profile service with the supplied parameters
     * 
     * @param wsClient Web service client
     * @param twitterAuth Service that provides authentication token required to use Twitter API
     */
    
    @Inject
    public UserProfileService(WSClient wsClient, TwitterAuthenticator twitterAuth) {
      this.wsClient = wsClient;
      this.twitterAuth = twitterAuth;
      
    }
    
    /**
     * Sets base URL for Twitter API
     * @param url
     */
    public void setBaseUrl(String url) {
  		this.baseUrl = url;
  	}
    
    /**
     * Retrieves user profile info and last ten tweets for a given <code>userName</code>.
     * First, the service retrieves a token for Twitter API. 
     * Then it gets user's profile info and user's ten last tweets and constructs a model 
     * object with the two and returns it as output. All this operations are done asynchronously.
     * 
     * @param userName Twitter User ID
     * 
     * @return a promise of UserProfileAndTweets which is the model that combines user profile info
     * and user's ten last tweets
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
     * Retrieves user profile info via Twitter API
     * 
     * @param accessToken token used to authorize Twitter API requests
     * @param userName Twitter User ID
     * @return a promise of UserProfile model
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
     * Interacts with twitter api to fetch 10 last tweets based on given <code>userName</code>.
     * 
     * @param accessToken token used to authorize Twitter API requests
     * @param userName Twitter User ID
     * @return promise of last ten tweets as a list
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