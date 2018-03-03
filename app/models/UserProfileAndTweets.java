package models;

import java.util.List;

public class UserProfileAndTweets {
	
	private UserProfile userProfile;
	private List<Tweet> tweets;
	
	public UserProfileAndTweets(UserProfile userProfile, List<Tweet> tweets){
		this.userProfile = userProfile;
		this.tweets = tweets;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

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
