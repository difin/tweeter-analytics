package models;

import java.util.List;

/**
 * Combines the data of User Profile and Tweets related to that User into single object.
 * @author Dimitry Fingerman
 * @version 1.0.0
 *
 */

public class UserProfileAndTweets {
	
	/**
	 * User Profile Object
	 */
	private UserProfile userProfile;
	/**
	 * List to store tweet object.
	 */
	private List<Tweet> tweets;
	
	/**
	 * Initialize object based on given parameters.
	 * @param userProfile     User Profile Object.
	 * @param tweets          List of tweet object.
	 */
	
	public UserProfileAndTweets(UserProfile userProfile, List<Tweet> tweets){
		this.userProfile = userProfile;
		this.tweets = tweets;
	}
	/**
	 * Returns Saved User Profile Object.
	 * @return gives user profile object.
	 */
	public UserProfile getUserProfile() {
		return userProfile;
	}
	
	/**
	 * Sets User Profile object based on the given user details.
	 * @param userProfile     User Profile Object.
	 */

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	/**
	 * Returns list of tweet's that associated with given user.
	 * @return List of tweet object.
	 */
	public List<Tweet> getTweets() {
		return tweets;
	}
	/**
	 * Store Tweet in List object for given user for further processing.
	 * @param tweets     Store Tweets with given user in List Object.
	 */
	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}
	
	
	/**
	 * Returns string representation of Object's all the data.
	 * @return String representation of object's property.
	 */
	public String toString(){
		
		String tweetsAsString = tweets.stream()
				.map(x -> x.toString())
				.reduce("", (a,b) -> a + "\n" + b);
		
		return userProfile.toString() + "\n" + tweetsAsString;			
	}
}