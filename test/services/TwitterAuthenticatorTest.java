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
	 * Initializing test environment.
	 * 1. Authenticating with twitter server to get access token.
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
	 * Verifying access token with given input parameter.
	 */
	@Test
	public void getAccessToken_correctJson_success() throws InterruptedException, ExecutionException {
		String token = twitterAuthenticator.getAccessToken().toCompletableFuture().get();
		assertThat(token, equalTo("Test Key"));
	}
	
	/**
	 * Verifying Enconding scheme, that we are using in our application.
	 */
	
	@Test //(expected = UnsupportedEncodingException.class)
	public void testException() throws UnsupportedEncodingException, Exception, ExecutionException{ 
		twitterAuthenticator.setEncoding("123123");
		String token=twitterAuthenticator.getAccessToken().toCompletableFuture().get();
		assertThat(token, equalTo("Test Key"));
	}

}