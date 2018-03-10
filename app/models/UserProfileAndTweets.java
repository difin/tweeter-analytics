package models;

import java.util.List;

/**
 * UserProfileAndTweets class combine the data of Tweet and UserProfile object into single one.
 * @author Dimitry Fingerman
 * @version 1.0.0
 *
 */

public class UserProfileAndTweets {
	
	private UserProfile userProfile;
	private List<Tweet> tweets;
	
	/**
	 * Parametarized constructor.
	 * @param userProfile
	 * @param tweets
	 */
	
	public UserProfileAndTweets(UserProfile userProfile, List<Tweet> tweets){
		this.userProfile = userProfile;
		this.tweets = tweets;
	}
	/**
	 * 
	 * @return get userprofile object.
	 */
	public UserProfile getUserProfile() {
		return userProfile;
	}
	
	/**
	 * 
	 * @param userProfile set user profile class's object.
	 */

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	/**
	 * 
	 * @return list of tweet.
	 */
	public List<Tweet> getTweets() {
		return tweets;
	}
	/**
	 * 
	 * @param tweets set tweet list.
	 */
	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}
	
	public String toString(){
		
		String tweetsAsString = tweets.stream()
				.map(x -> x.toString())
				.reduce("", (a,b) -> a + "\n" + b);
		
		return userProfile.toString() + "\n" + tweetsAsString;			
	}
}
