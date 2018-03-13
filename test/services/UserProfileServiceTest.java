package services;

import static play.mvc.Results.ok;

import java.io.IOException;
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
	 * Initializes test environment.
	 * Setups mocks and the responses they should return.
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
	 * Tests User Profile service with dummy data.
	 * Sets a fake user profile and sets fake tweets that should be returned from mocks, 
	 * calls user profile service to retrieve a user profile data and verifies that the result
	 * is equal to the mock data.
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