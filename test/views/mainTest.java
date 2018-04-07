package views;

import org.junit.Test;
import play.twirl.api.Content;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.stringContainsInOrder;

/**
 * Implements JUnit test cases for main page.
 *
 * @author Nikita Baranov
 * @version 1.0.0
 */
public class mainTest {

    /**
     * Correct rendering of menu
     */
    @Test
    public void render_null_null() {
        Content html = views.html.main.render(null, null);
        assertThat("text/html", is(equalTo(html.contentType())));
        assertThat(html.body(), stringContainsInOrder(Arrays.asList("Search Tweets", "Search Tweets (Web Socket)", "Profile")));
    }
}