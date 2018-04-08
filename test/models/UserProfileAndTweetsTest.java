package models;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Implements JUnit test cases for UserProfileandTweets functionality.
 *
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
     * Initializes dummy User Profile and tweets data for testing purpose.
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

        Tweet tweet2 = new Tweet("tw2createdAt", "tweet2");
        tweet2.setUser(up);

        tweets.add(tweet1);
        tweets.add(tweet2);
    }

    /**
     * Tests UserProfileAndTweets Model with dummy testing data.
     * Once the testing is done, we are verifying the result with the help of assert.
     */
    @Test
    public void testUserProfileAndTweetsModel() {
        UserProfileAndTweets upat = new UserProfileAndTweets(new UserProfile(), new ArrayList<>());
        upat.setTweets(tweets);
        upat.setUserProfile(up);

        assertEquals(up, upat.getUserProfile());
        assertEquals(tweets, upat.getTweets());
    }

    /**
     * Tests UserProfileAndTweets Model's string conversion functionality and asserting the results.
     */
    @Test
    public void testUserProfileAndTweetsModelToString() {
        UserProfileAndTweets upat = new UserProfileAndTweets(up, tweets);
        String test = upat.toString();
        assertTrue(test.contains("testSname"));
    }

    /**
     * Checks Equals method
     */
    @Test
    public void equals_correct_success() {
        UserProfileAndTweets userProfileAndTweets1 = new UserProfileAndTweets(new UserProfile(), new ArrayList<>());
        userProfileAndTweets1.setTweets(tweets);
        userProfileAndTweets1.setUserProfile(up);

        UserProfileAndTweets userProfileAndTweets2 = new UserProfileAndTweets(new UserProfile(), new ArrayList<>());
        userProfileAndTweets2.setTweets(tweets);
        userProfileAndTweets2.setUserProfile(up);

        assertTrue(userProfileAndTweets1.equals(userProfileAndTweets1));

        assertTrue(userProfileAndTweets1.equals(userProfileAndTweets2));
    }

    /**
     * Checks Equals method
     */
    @Test
    public void equals_inCorrect_success() {
        UserProfileAndTweets userProfileAndTweets1 = new UserProfileAndTweets(new UserProfile(), new ArrayList<>());
        userProfileAndTweets1.setTweets(tweets);
        userProfileAndTweets1.setUserProfile(up);

        UserProfileAndTweets userProfileAndTweets2 = new UserProfileAndTweets(new UserProfile(), new ArrayList<>());

        assertFalse(userProfileAndTweets1.equals(userProfileAndTweets2));

        userProfileAndTweets2.setUserProfile(userProfileAndTweets1.getUserProfile());
        assertFalse(userProfileAndTweets1.equals(userProfileAndTweets2));

        assertFalse(userProfileAndTweets1.equals(null));
    }

    @Test
    public void hashCode_success() {
        UserProfileAndTweets userProfileAndTweets1 = new UserProfileAndTweets(new UserProfile(), new ArrayList<>());
        userProfileAndTweets1.setTweets(tweets);
        userProfileAndTweets1.setUserProfile(up);

        UserProfileAndTweets userProfileAndTweets2 = new UserProfileAndTweets(new UserProfile(), new ArrayList<>());

        assertTrue(userProfileAndTweets1.hashCode() == userProfileAndTweets1.hashCode());
        assertFalse(userProfileAndTweets1.hashCode() == userProfileAndTweets2.hashCode());
    }
}