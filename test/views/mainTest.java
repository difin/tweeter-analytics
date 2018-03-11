package views;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.stringContainsInOrder;

import java.util.Arrays;

import org.junit.Test;

import play.twirl.api.Content;

/**
 * Implements JUnit test cases for main page.
 * @author Tumer Horloev
 * @version 1.0.0
 */
public class mainTest {
	
	/**
	 * Testing various operation with dummy data.
	 */

	@Test
	public void render_null_null() {
		Content html = views.html.main.render(null, null);
		assertThat("text/html", is(equalTo(html.contentType())));
		assertThat(html.body(), stringContainsInOrder(Arrays.asList("Home", "Profile")));
	}
}