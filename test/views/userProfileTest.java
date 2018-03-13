package views;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.stringContainsInOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import models.Tweet;
import models.UserProfile;
import models.UserProfileAndTweets;
import play.twirl.api.Content;

/**
 * Implements JUnit test cases for user profile page.
 * @author Nikita Baranov
 * @version 1.0.0
 */
public class userProfileTest {
	
	/**
	 * Rendering of the form with UserProfile and tweet data
	 */
	
	@Test
	public void render_validProfile_success(){
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
		List<Tweet> tweets = new ArrayList<Tweet>();
		tweets.add(tweet1);
		tweets.add(tweet2);

		UserProfileAndTweets userProfileAndTweets = new UserProfileAndTweets(up, tweets);
		
		Content html = views.html.userProfile.render(userProfileAndTweets);
		assertThat("text/html", is(equalTo(html.contentType())));
		assertThat(html.body(), stringContainsInOrder(Arrays.asList("testName","testSname")));
	}	
}
