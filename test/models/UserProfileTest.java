package models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Implements JUnit test cases for UserProfile functionality.
 *
 * @author Tumer Horloev
 * @version 1.0.0
 */
public class UserProfileTest {
    /**
     * Initializes dummy User Profile and enters dummy properties inside it.
     * Once the test is done, we are verifying all properties.
     */
    @Test
    public void testUserProfileModel() {
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

        up.setTweets(tweets);

        assertThat(up.getTweets(), is(equalTo(tweets)));
    }

    /**
     * Checks Equals method
     */
    @Test
    public void equals_correct_success() {
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

        up.setTweets(tweets);

        assertTrue(up.equals(up));
    }

    /**
     * Checks Equals method
     */
    @Test
    public void equals_inCorrect_success() {
        UserProfile up1 = new UserProfile();
        up1.setName("testName");
        up1.setScreen_name("testSname");
        up1.setCreated_at("createdAt");

        Tweet tweet1 = new Tweet();
        tweet1.setFull_text("tweet1");
        tweet1.setCreated_at("tw1createdAt");
        Tweet tweet2 = new Tweet();
        tweet2.setFull_text("tweet2");
        tweet2.setCreated_at("tw2createdAt");

        List<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet1);
        tweets.add(tweet2);

        up1.setTweets(tweets);

        UserProfile up2 = new UserProfile();

        assertFalse(up1.equals(up2));

        up2.setName("testName");
        assertFalse(up1.equals(up2));

        up2.setScreen_name("testSname");
        assertFalse(up1.equals(up2));
    }

    /**
     * Checks Hash Code method
     */
    @Test
    public void hashCode_success() {
        UserProfile up1 = new UserProfile();
        up1.setName("testName");
        up1.setScreen_name("testSname");
        up1.setCreated_at("createdAt");


        Tweet tweet1 = new Tweet();
        tweet1.setFull_text("tweet1");
        tweet1.setCreated_at("tw1createdAt");
        Tweet tweet2 = new Tweet();
        tweet2.setFull_text("tweet2");
        tweet2.setCreated_at("tw2createdAt");
        List<Tweet> tweets = new ArrayList<Tweet>();
        tweets.add(tweet1);
        tweets.add(tweet2);
        up1.setTweets(tweets);

        UserProfile up2 = new UserProfile();
        up2.setName("testName2");
        up2.setScreen_name("testSname2");
        up2.setCreated_at("createdAt2");

        up2.setTweets(tweets);

        assertTrue(up1.hashCode() == up1.hashCode());
        assertFalse(up1.hashCode() == up2.hashCode());
    }
}
