package services;

import models.Tweet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.libs.ws.WSClient;
import play.routing.RoutingDsl;
import play.server.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.assertTrue;
import static play.mvc.Results.ok;

/**
 * Implements JUnit test cases for 10 Tweets retrieval functionality.
 *
 * @author Tumer Horloev
 * @version 1.0.0
 */
public class TenTweetsForKeywordServiceTest {

    /**
     * Twitter Authentication object for authentication purpose.
     */
    private TwitterAuthenticator twitterAuthenticator;

    /**
     * WS Client object to interact with REST-API Calls.
     */
    private WSClient ws;

    /**
     * Server object.
     */
    private Server server;

    /**
     * Initializes test environment.
     * Setups mocks and the response they should return.
     */
    @Before
    public void setup() {

        server = Server.forRouter((components) -> RoutingDsl.fromComponents(components)
                .POST("/oauth2/token").routeTo(() -> ok().sendResource("token.json"))
                .GET("/1.1/search/tweets.json").routeTo(() -> ok().sendResource("search.json"))
                .build());
        ws = play.test.WSTestClient.newClient(server.httpPort());
        twitterAuthenticator = new TwitterAuthenticator(ws);
        twitterAuthenticator.setBaseUrl("");

    }

    /**
     * Destroys testing setup.
     *
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
     * Prepares 2 tweets expected to be returned for a search, executes a tweets search method
     * and verifies that the result is as expected.
     */
    @Test
    public void testGetTenTweetsForKeyword() throws InterruptedException, ExecutionException {
        List<String> searchList = new ArrayList<>();
        searchList.add("SearchPhrase1");
        searchList.add("SearchPhrase2");

        Tweet tweet1 = new Tweet();
        tweet1.setFull_text("tweet1");
        tweet1.setCreated_at("tw1createdAt");
        List<Tweet> twl1 = new ArrayList<>();
        twl1.add(tweet1);

        Tweet tweet2 = new Tweet();
        tweet2.setFull_text("tweet2");
        tweet2.setCreated_at("tw2createdAt");
        List<Tweet> twl2 = new ArrayList<>();
        twl2.add(tweet2);

        TenTweetsForKeywordService ttfks = new TenTweetsForKeywordService(ws, twitterAuthenticator);
        ttfks.setBaseUrl("");

        Map<String, List<Tweet>> actual = ttfks.getTenTweetsForKeyword(searchList).toCompletableFuture().get();

        assertTrue(actual.get("SearchPhrase1").containsAll(Arrays.asList(tweet1, tweet2)));
        assertTrue(actual.get("SearchPhrase2").containsAll(Arrays.asList(tweet1, tweet2)));
    }
}