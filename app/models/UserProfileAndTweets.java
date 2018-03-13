package models;

import java.util.List;

/**
 * Combines the data of User Profile and Tweets related to that User into single object.
 * @author Dmitriy Fingerman
 * @version 1.0.0
 *
 */

public class UserProfileAndTweets {
	
	/**
	 * User Profile object
	 */
	private UserProfile userProfile;
	/**
	 * List to of tweet objects
	 */
	private List<Tweet> tweets;
	
	/**
	 * Creates new object based on given parameters
	 * @param userProfile     User Profile
	 * @param tweets          List of tweets
	 */
	
	public UserProfileAndTweets(UserProfile userProfile, List<Tweet> tweets){
		this.userProfile = userProfile;
		this.tweets = tweets;
	}
	/**
	 * Returns User Profile object
	 * @return user profile object
	 */
	public UserProfile getUserProfile() {
		return userProfile;
	}
	
	/**
	 * Sets user profile object based
	 * @param userProfile user profile object.
	 */

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	/**
	 * Returns list of tweets associated with this user
	 * @return list of tweets
	 */
	public List<Tweet> getTweets() {
		return tweets;
	}
	/**
	 * Sets list of tweets
	 * @param tweets list of tweets
	 */
	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}
	
	/**
	 * Returns string representation of this model
	 * @return string representation of this model
	 */
	public String toString(){
		
		String tweetsAsString = tweets.stream()
				.map(x -> x.toString())
				.reduce("", (a,b) -> a + "\n" + b);
		
		return userProfile.toString() + "\n" + tweetsAsString;			
	}
}