package views;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.stringContainsInOrder;

import java.util.Arrays;

import org.junit.Test;
import org.webjars.play.WebJarsUtil;

import play.mvc.Http;
import play.test.Helpers;
import play.test.WithServer;
import play.twirl.api.Content;

import static org.mockito.Mockito.*;
import static play.test.Helpers.fakeRequest;

/**
 * Implements JUnit test cases for responsive tweets page.
 * @author Dmitriy Fingerman
 * @version 1.0.0
 */
public class responsiveTweetsTest extends WithServer {
	
	/**
	 * Mock of WebJars
	 */
	
	private final WebJarsUtil webJarsUtil = mock(WebJarsUtil.class);

	/**
	 * Checks the rendering of the Responsive tweets
	 * views, navigation bar and then
	 * checks the search tweets are present.
	 */
	
	@Test
	public void whenRenderingResponsiveTweetsViewThenNavBarAndSearchFormTextArePresent() {
		String serverURL = "ws://localhost:" + this.testServer.port() + "/responsive";
		Http.RequestBuilder request = fakeRequest("GET", serverURL);
		Http.Context context = Helpers.httpContext(request.build());
		Http.Context.current.set(context);

		Content html = views.html.responsiveTweets.render(webJarsUtil);
		assertThat("text/html", is(equalTo(html.contentType())));
		assertThat(html.body(), stringContainsInOrder(Arrays.asList(
				"Search Tweets", 
				"Search Tweets (Web Socket)", 
				"Profile", 
				"Enter a Key word phrase (ex. Canadian Nature)", 
				"Send")));
	}
}