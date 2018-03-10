package models;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class UserProfileTest {
	UserProfile up = new UserProfile();

	@Test

	public void testUserProfileModel() {
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

		up.setTweets(tweets);

		assert(up.getTweets()==tweets);
	}

}
