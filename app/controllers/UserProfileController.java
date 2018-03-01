package controllers;

import javax.inject.*;

import play.mvc.*;
import play.libs.ws.*;
import play.libs.ws.WSBodyReadables;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.concurrent.CompletionStage;

import scala.concurrent.ExecutionContextExecutor;

@Singleton
public class UserProfileController extends Controller {
	
	private static final String TWITTER_CONSUMER_KEY = "2uiozTAH7aMj7zf3BfrXvajw0";
	private static final String TWITTER_CONSUMER_SECRET = "8yeB9yu6bGG18CZu5fK23dQK6FgK2H2OJyA0uoY0Mv4LiTTnhP";
	
    private final WSClient ws;
    
    @Inject
    public UserProfileController(WSClient ws) {
      this.ws = ws;
    }

    public CompletionStage<Result> userProfile(String userName) {
    	
    	return
    			ws
    			.url("https://api.twitter.com/oauth2/token")
                .addHeader("User-Agent", "SOEN-6441")
                .addHeader("Authorization", "Basic " + encodeKeys(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET))
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .addHeader("Content-Length", "29")
                .post("grant_type=client_credentials")
                .thenApply(r -> r.getBody(WSBodyReadables.instance.json()).get("access_token").asText())
                .thenCompose(r -> getUserProfile(r, userName));
    }
    
    private CompletionStage<Result> getUserProfile(String accessToken, String userName){
			
		return 
				ws
				.url("https://api.twitter.com/1.1/users/show.json")
		        .addHeader("Authorization", "Bearer " + accessToken)
		        .addQueryParameter("screen_name", userName)
		        .get()
		        .thenApply(r -> ok(r.getBody(WSBodyReadables.instance.json())));
    }
    
	private static String encodeKeys(String consumerKey, String consumerSecret) {
	    try {
	        String encodedConsumerKey = URLEncoder.encode(consumerKey, "UTF-8");
	        String encodedConsumerSecret = URLEncoder.encode(consumerSecret, "UTF-8");
	        String fullKey = encodedConsumerKey + ":" + encodedConsumerSecret;
	        byte[] encodedBytes = Base64.getEncoder().encode(fullKey.getBytes());
	        return new String(encodedBytes);  
	    }
	    catch (UnsupportedEncodingException e) {
	        return new String();
	    }
	}
}
