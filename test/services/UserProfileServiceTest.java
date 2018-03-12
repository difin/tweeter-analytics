package services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static play.mvc.Results.ok;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Tweet;
import models.UserProfile;
import models.UserProfileAndTweets;
import play.libs.ws.WSClient;
import play.routing.RoutingDsl;
import play.server.Server;
import services.TwitterAuthenticator;

/**
 * Implements JUnit test cases for UserProfileService functionality.
 * @author Tumer Horloev
 * @version 1.0.0
 */
public class UserProfileServiceTest {
	/**
	 * Twitter Authentication object for authentication purpose.
	 */
	private TwitterAuthenticator twitterAuthenticator;
	/**
	 * WSClient to play with RESTAPI Calls.
	 */
    private WSClient ws;
    /**
	 * Server class instance.
	 */
    private Server server;
    
    /**
	 * Initializing test environment.
	 * 1. Authenticating with twitter server to get access token.
	 */
	@Before
	public void setup() {
		
		server = Server.forRouter((components) -> RoutingDsl.fromComponents(components)
				.POST("/oauth2/token").routeTo(() -> ok().sendResource("token.json"))
				.GET("/1.1/users/show.json").routeTo(()->ok().sendResource("profile.json"))
				.GET("/1.1/statuses/user_timeline.json").routeTo(()->ok().sendResource("tweets.json"))
				.build());
		ws = play.test.WSTestClient.newClient(server.httpPort());
		twitterAuthenticator = new TwitterAuthenticator(ws);
		twitterAuthenticator.setBaseUrl("");
		
	}
	
	/**
	 * Destroys testing setup.
	 * @throws IOException
	 */

	@After
	public void tearDown() throws IOException {
		try {
			ws.close();
		} finally {
			server.stop();
		}
	}
	/**
	 * Test User Profile service with dummy data.
	 * 1. Set the fake userprofile.
	 * 2. Set fake tweets.
	 * Passing this data to UserProfileService class and verify result.
	 */
	@Test
	public void testUserProfile() throws InterruptedException, ExecutionException {
    	UserProfile up = new UserProfile();
    	up.setName("testName");
    	up.setScreen_name("testSname");
    	up.setCreated_at("createdAt");
    	up.setDescription("testDesc");
    	up.setFavourites_count(123123);
    	up.setFollowers_count(123123);
    	up.setFriends_count(123);
    	up.setTime_zone("zone");
    	up.setStatuses_count("sc");

    	Tweet tweet1 = new Tweet();
    	tweet1.setFull_text("tweet1");
    	tweet1.setCreated_at("tw1createdAt");
    	Tweet tweet2 = new Tweet();
    	tweet2.setFull_text("tweet2");
    	tweet2.setCreated_at("tw2createdAt");
    	
    	UserProfileService ups = new UserProfileService(ws, twitterAuthenticator);
    	ups.setBaseUrl("");
    	UserProfileAndTweets upt = ups.userProfle("testSname").toCompletableFuture().get();
    	
    	assert(upt.getTweets().contains(tweet1));
    	assert(upt.getTweets().contains(tweet1));
    	assert(upt.getUserProfile().equals(up));
    	
    	
	}

}