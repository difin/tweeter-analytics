package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.libs.ws.WSClient;
import play.routing.RoutingDsl;
import play.server.Server;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static play.mvc.Results.ok;

/**
 * Implements JUnit test cases for Twitter Authentication functionality.
 *
 * @author Tumer Horloev
 * @version 1.0.0
 */
public class TwitterAuthenticatorTest {

    /**
     * Twitter Authentication object.
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
                .POST("/oauth2/token").routeTo(() -> ok().sendResource("token.json")).build());
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
     * Verifies access token with expected result from mock
     */
    @Test
    public void getAccessToken_correctJson_success() throws InterruptedException, ExecutionException {
        String token = twitterAuthenticator.getAccessToken().toCompletableFuture().get();
        assertThat(token, equalTo("Test Key"));
    }

    /**
     * Verifies correctness of enconding scheme with expected result from mock
     */
    @Test
    public void testException() throws ExecutionException, InterruptedException {
        twitterAuthenticator.setEncoding("123123");
        String token = twitterAuthenticator.getAccessToken().toCompletableFuture().get();
        assertThat(token, equalTo("Test Key"));
    }
}