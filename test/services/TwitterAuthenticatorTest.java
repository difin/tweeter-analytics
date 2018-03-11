package services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static play.mvc.Results.ok;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.libs.ws.WSClient;
import play.routing.RoutingDsl;
import play.server.Server;
import services.TwitterAuthenticator;

/**
 * Implements JUnit test cases for Twitter Authentication functionality.
 * @author Tumer Horloev
 * @version 1.0.0
 */
public class TwitterAuthenticatorTest {
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
				.POST("/oauth2/token").routeTo(() -> ok().sendResource("token.json")).build());
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
	public void getAccessToken_correctJson_success() throws InterruptedException, ExecutionException {
		String token = twitterAuthenticator.getAccessToken().toCompletableFuture().get();
		assertThat(token, equalTo("Test Key"));
	}
	
	/**
	 * Testing various operation with dummy data.
	 */
	
	@Test //(expected = UnsupportedEncodingException.class)
	public void testException() throws UnsupportedEncodingException, Exception, ExecutionException{ 
		twitterAuthenticator.setEncoding("123123");
		String token=twitterAuthenticator.getAccessToken().toCompletableFuture().get();
		assertThat(token, equalTo("Test Key"));
	}

}