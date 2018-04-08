package models;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Implements JUnit test cases for TweetTest functionality.
 *
 * @author Nikita Baranov
 * @version 1.0.0
 */
public class TweetTest {

    /**
     * Checks Equals method
     */
    @Test
    public void equals_correct_success() {
        UserProfile userProfile = new UserProfile();
        userProfile.setName("testName");
        userProfile.setScreen_name("testSname");
        userProfile.setCreated_at("createdAt");
        userProfile.setDescription("testDesc");
        userProfile.setFavourites_count(123123);
        userProfile.setFollowers_count(123123);
        userProfile.setFriends_count(123);
        userProfile.setTime_zone("zone");
        userProfile.setStatuses_count("sc");

        Tweet tweet1 = new Tweet();
        tweet1.setUser(userProfile);
        tweet1.setFull_text("tweet1");
        tweet1.setCreated_at("tw1createdAt");

        Tweet tweet2 = new Tweet();
        tweet2.setUser(userProfile);
        tweet2.setFull_text("tweet1");
        tweet2.setCreated_at("tw1createdAt");

        assertTrue(tweet1.equals(tweet1));

        assertTrue(tweet1.equals(tweet2));
    }

    /**
     * Checks Equals method
     */
    @Test
    public void equals_inCorrect_success() {
        UserProfile userProfile = new UserProfile();
        userProfile.setName("testName");
        userProfile.setScreen_name("testSname");
        userProfile.setCreated_at("createdAt");
        userProfile.setDescription("testDesc");
        userProfile.setFavourites_count(123123);
        userProfile.setFollowers_count(123123);
        userProfile.setFriends_count(123);
        userProfile.setTime_zone("zone");
        userProfile.setStatuses_count("sc");

        Tweet tweet1 = new Tweet();
        tweet1.setUser(userProfile);
        tweet1.setFull_text("tweet1");
        tweet1.setCreated_at("tw1createdAt");

        Tweet tweet2 = new Tweet();
        tweet2.setUser(null);
        tweet2.setFull_text(null);
        tweet2.setCreated_at(null);

        assertFalse(tweet1.equals(tweet2));

        tweet2.setCreated_at(tweet1.getCreated_at());
        assertFalse(tweet1.equals(tweet2));

        tweet2.setFull_text(tweet1.getFull_text());
        assertFalse(tweet1.equals(tweet2));

        assertFalse(tweet1.equals(null));
    }

    /**
     * Checks Hash Code method
     */
    @Test
    public void hashCode_success() {
        UserProfile userProfile = new UserProfile();
        userProfile.setName("testName");
        userProfile.setScreen_name("testSname");
        userProfile.setCreated_at("createdAt");
        userProfile.setDescription("testDesc");
        userProfile.setFavourites_count(123123);
        userProfile.setFollowers_count(123123);
        userProfile.setFriends_count(123);
        userProfile.setTime_zone("zone");
        userProfile.setStatuses_count("sc");

        Tweet tweet1 = new Tweet();
        tweet1.setUser(userProfile);
        tweet1.setFull_text("tweet1");
        tweet1.setCreated_at("tw1createdAt");

        Tweet tweet2 = new Tweet();
        tweet2.setUser(userProfile);
        tweet2.setFull_text("tweet2");
        tweet2.setCreated_at("tw2createdAt");

        assertTrue(tweet1.hashCode() == tweet1.hashCode());
        assertFalse(tweet1.hashCode() == tweet2.hashCode());
    }
}