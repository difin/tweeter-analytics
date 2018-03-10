package models;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class UserProfileAndTweetsTest {
	private UserProfile up = new UserProfile();
	private List<Tweet> tweets = new ArrayList<Tweet>();
	
	
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

	@Test
	public void testUserProfileAndTweetsModel() {
		UserProfileAndTweets upat = new UserProfileAndTweets(new UserProfile(), new ArrayList<Tweet>());
		upat.setTweets(tweets);
		upat.setUserProfile(up);

		assert(upat.getUserProfile()==up);
		assert(upat.getTweets()==tweets);

	}
	
	@Test
	public void testUserProfileAndTweetsModelToString() {
		UserProfileAndTweets upat = new UserProfileAndTweets(up, tweets);
		String test = upat.toString();
		//System.out.println(test);
		assert(test.contains("testSname"));
	}


}
