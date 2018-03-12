package models;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Implements JUnit test cases for UserProfileandTweets functionality.
 * @author Tumer Horloev
 * @version 1.0.0
 */
public class UserProfileAndTweetsTest {
	
	/**
	 * User Profile Object for testing purpose.
	 */
	private UserProfile up = new UserProfile();
	/**
	 * List to store dummy tweet object.
	 */
	private List<Tweet> tweets = new ArrayList<Tweet>();
	
	
	/**
	 * Initialize dummy User Profile and tweets data for testing purpose.
	 */
	@Before
	public void setup() {
		
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
		tweet1.setUser(up);
		tweet1.setFull_text("tweet1");
		tweet1.setCreated_at("tw1createdAt");
		Tweet tweet2 = new Tweet("tw2createdAt","tweet2","doesn't work");
		tweet2.setUser(up);
		
		tweets.add(tweet1);
		tweets.add(tweet2);
		
	}
	
	/**
	 * Test UserProfileAndTweets Model with dummy testing data.
	 * Once the testing is done, we are verifying the result with the help of assert.
	 */

	@Test
	public void testUserProfileAndTweetsModel() {
		UserProfileAndTweets upat = new UserProfileAndTweets(new UserProfile(), new ArrayList<Tweet>());
		upat.setTweets(tweets);
		upat.setUserProfile(up);

		assert(upat.getUserProfile()==up);
		assert(upat.getTweets()==tweets);

	}
	
	/**
	 * Test UserProfileAndTweets Model's string conversion functionality and asserting the results.
	 */
	
	@Test
	public void testUserProfileAndTweetsModelToString() {
		UserProfileAndTweets upat = new UserProfileAndTweets(up, tweets);
		String test = upat.toString();
		//System.out.println(test);
		assert(test.contains("testSname"));
	}


}