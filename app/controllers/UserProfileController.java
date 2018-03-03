package controllers;

import javax.inject.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.UserProfile;
import services.TwitterAuthenticator;

import play.mvc.*;
import play.twirl.api.Content;
import play.libs.Json;
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
    			twitterAuth.getAccessToken()
                .thenCompose(r -> getUserProfile(r, userName))
                .thenApply(r -> Json.fromJson(r, UserProfile.class))
                .thenApply(r -> ok(r.toString()));
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
