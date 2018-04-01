package views;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.stringContainsInOrder;

import java.util.Arrays;

import org.junit.Test;
import org.webjars.play.WebJarsUtil;

import play.twirl.api.Content;

import static org.mockito.Mockito.*;

/**
 * Implements JUnit test cases for responsive tweets page.
 * @author Dmitriy Fingerman
 * @version 1.0.0
 */
public class responsiveTweetsTest {
	
	private final WebJarsUtil webJarsUtil = mock(WebJarsUtil.class);

	@Test
	public void whenRenderingResponsiveTweetsViewThenNavBarAndSearchFormTextArePresent() {
		
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