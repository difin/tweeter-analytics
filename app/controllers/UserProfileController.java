package controllers;

import javax.inject.*;

import com.fasterxml.jackson.databind.JsonNode;
import services.TwitterAuthenticator;

import play.mvc.*;
import play.libs.ws.*;
import play.libs.ws.WSBodyReadables;

import java.util.concurrent.CompletionStage;

@Singleton
public class UserProfileController extends Controller {
	
    private final WSClient wsClient;
    private final TwitterAuthenticator twitterAuth;
    
    @Inject
    public UserProfileController(WSClient wsClient, TwitterAuthenticator twitterAuth) {
      this.wsClient = wsClient;
      this.twitterAuth = twitterAuth;
    }

    public CompletionStage<Result> userProfile(String userName) {
    	
    	return
    			twitterAuth.getAccessToken(userName)
                .thenCompose(r -> getUserProfile(r, userName))
                .thenApply(r -> ok(r));
    }
    
    private CompletionStage<JsonNode> getUserProfile(String accessToken, String userName){
			
		return 
				wsClient
				.url("https://api.twitter.com/1.1/users/show.json")
		        .addHeader("Authorization", "Bearer " + accessToken)
		        .addQueryParameter("screen_name", userName)
		        .get()
		        .thenApply(r -> r.getBody(WSBodyReadables.instance.json()));
    }
}
