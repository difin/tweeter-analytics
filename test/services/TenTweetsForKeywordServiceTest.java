package services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static play.mvc.Results.ok;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
 * Implements JUnit test cases for 10 Tweets retrieval functionality.
 * @author Tumer Horloev
 * @version 1.0.0
 */
public class TenTweetsForKeywordServiceTest {
	/**
	 * {@literal twitterAuthenticator TwitterAuthenticator class object.}
	 */
	private TwitterAuthenticator twitterAuthenticator;
	/**
	 * {@literal ws WSClient object for auth and serialization.}
	 */
    private WSClient ws;
    /**
	 * {@literal server Server class instance.}
	 */
    private Server server;
    
    /**
	 * Initialize dummy testing data.
	 */
	@Before
	public void setup() {
		
		server = Server.forRouter((components) -> RoutingDsl.fromComponents(components)
				.POST("/oauth2/token").routeTo(() -> ok().sendResource("token.json"))
				.GET("/1.1/search/tweets.json").routeTo(()->ok().sendResource("search.json"))
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
	 * Testing various operation with dummy data.
	 */

	@Test
	public void testGetTenTweetsForKeyword() throws InterruptedException, ExecutionException {
    	List<String> searchList = new ArrayList<String>();
    	searchList.add("tweet1");
    	searchList.add("tweet2");
    	
    	Tweet tweet1 = new Tweet();
    	tweet1.setFull_text("tweet1");
    	tweet1.setCreated_at("tw1createdAt");
    	Tweet tweet2 = new Tweet();
    	tweet2.setFull_text("tweet2");
    	tweet2.setCreated_at("tw2createdAt");
    	
    	List<Tweet> twl1 = new ArrayList<Tweet>();
    	List<Tweet> twl2 = new ArrayList<Tweet>();
    	twl1.add(tweet1);
    	twl2.add(tweet2);
    	
    	Map<String,List<Tweet>> result = new LinkedHashMap<String,List<Tweet>>();
    	result.put("tweet1", twl1);
    	result.put("tweet2", twl2);

    	TenTweetsForKeywordService ttfks = new TenTweetsForKeywordService(ws, twitterAuthenticator);
    	ttfks.setBaseUrl("");
    	
    	Map<String,List<Tweet>> tenTweets = ttfks.getTenTweetsForKeyword(searchList).toCompletableFuture().get();   
    	
    	assert(result.equals(tenTweets));
    	
    	
	}

}