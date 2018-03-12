package models;

import java.util.List;

/**
 * Represents User Profile object, in which we are storing user's data like name,
 * username , description , followers list etc. from incoming JSON response.
 * @author Dimitry Fingerman
 * @version 1.0.0
 */

public class UserProfile{
	
	/**
	 * Name is string format.
	 */
	private String name;
	/**
	 * Screen Name is string name.
	 */
	private String screen_name;
	/**
	 * Description details.
	 */
	private String description;
	/**
	 * Number of the followers in long datatype.
	 */
	private long followers_count;
	/**
	 * {@literal friends_count store friend counts.}
	 */
	private long friends_count;
	/**
	 * Number of friends in long datatype.
	 */
	private long favourites_count;
	/**
	 * Number of favorites.
	 */
	private String created_at;
	/**
	 * Time zone details in string format.
	 */
	private String time_zone;
	/**
	 * Status is string representation.
	 */
	private String statuses_count;
	/**
	 * List to store tweets in Tweet object format.
	 */
	private List<Tweet> tweets;
	/**
	 * Profile Image URL.
	 */
	private String profile_image_url;
	/**
	 * Profile Image URL which point to the profile photo of size 400*400
	 */
	private String profile_image_url_400x400;
	
	/**
	 * Returns name of the user.
	 * @return Name of the Twitter User.
	 */
	
	public String getName() {
		return name;
	}
	
	/**
	 * Sets user name is string format.
	 * @param name     set user name.
	 */

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns screen name of the user.
	 * @return user's screen name.
	 */

	public String getScreen_name() {
		return screen_name;
	}
	
	/**
	 * Sets user's screen name in string format.
	 * @param screen_name     Twitter User Screen Name.
	 */

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}
	
	/**
	 * Returns description of Twitter user.
	 * @return description of user.
	 */

	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets User's description.
	 * @param description     Setting up user description.
	 */

	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns number of follower of particular User.
	 * @return receiving follower's count.
	 */

	public long getFollowers_count() {
		return followers_count;
	}
	
	/**
	 * Sets how many followers this twitter user has.
	 * @param followers_count     Follower's figure.
	 */

	public void setFollowers_count(long followers_count) {
		this.followers_count = followers_count;
	}
	
	/**
	 * Returns number of the friends this user have on twitter.
	 * @return number of friends this user have.
	 */

	public long getFriends_count() {
		return friends_count;
	}
	
	/**
	 * Sets the number of friends this twitter user has.
	 * @param friends_count Setting up friends count for particular user.
	 */

	public void setFriends_count(long friends_count) {
		this.friends_count = friends_count;
	}
	/**
	 * Returns number of favorite for given twitter user.
	 * @return retrieve number of favorites.
	 */
	public long getFavourites_count() {
		return favourites_count;
	}
	
	/**
	 * Sets how many favorites this given user have on twitter.
	 * @param favourites_count     Number of favorite in long format.
	 */

	public void setFavourites_count(long favourites_count) {
		this.favourites_count = favourites_count;
	}
	
	/**
	 * Returns creation date of given User Profile.
	 * @return Creation date of User Profile.
	 */

	public String getCreated_at() {
		return created_at;
	}
	
	/**
	 * Sets creation date of profile from JSON response.
	 * @param created_at     set creation date of user profile.
	 */

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
	/**
	 * Returns time zone information of given user.
	 * @return time zone information of any twitter user.
	 */

	public String getTime_zone() {
		return time_zone;
	}
	
	/**
	 * Sets time zone information for given User Profile.
	 * @param time_zone     TimeZone information for particular user.
	 */

	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}

	/**
	 * Returns status count information for given Twitter User.
	 * @return number of status for given user.
	 */
	public String getStatuses_count() {
		return statuses_count;
	}
	
	/**
	 * Sets how many status this User Profile has based on JSON response.
	 * @param statuses_count     Status Count of any User Profile.
	 */
	public void setStatuses_count(String statuses_count) {
		this.statuses_count = statuses_count;
	}
	
	/**
	 * Returns associated list of 10 tweets for given user profile.
	 * @return list of tweets.
	 */

	public List<Tweet> getTweets() {
		return tweets;
	}
	
	/**
	 * Sets latest 10 tweets for given user profile.
	 * First creating Tweet object and then saving it into List.
	 * @param tweets     List of 10 tweets.
	 */

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}
	
	/**
	 * Returns URL of Profile Picture for given user.
	 * @return Profile Picture URL.
	 */
		
	public String getProfile_image_url() {
		return profile_image_url;
	}
	
	/**
	 * Stores URL of Profile Picture of incoming twitter user.
	 * @param profile_image_url     String formatted URL.
	 */

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
		setProfile_image_url_400x400(profile_image_url.replace("normal", "400x400"));
	}
	
	/**
	 * Returns URL of Enlarged Profile Picture size of 400*400.
	 * @return URL of larged profile picture. 
	 */

	public String getProfile_image_url_400x400() {
		return profile_image_url_400x400;
	}
	
	/**
	 * Sets URL of Profile Picture where Photograph dimension is high 400*400.
	 * @param profile_image_url_400x400     Enlarged Profile Picture URL.
	 */

	public void setProfile_image_url_400x400(String profile_image_url_400x400) {
		this.profile_image_url_400x400 = profile_image_url_400x400;
	}
	
	/**
	 * Returns String representation of UserProfile class each and every data.
	 * @return Object's properties in readable format.
	 */
	public String toString(){
		return "name: " + name + "\n" +
				"screen_name: " + screen_name + "\n" +
				"description: " + description + "\n" +
				"followers_count: " + followers_count + "\n" +
				"friends_count: " + friends_count + "\n" +
				"favourites_count: " + favourites_count + "\n" +
				"created_at: " + created_at + "\n" +
				"time_zone: " + time_zone + "\n" +
				"statuses_count: " + statuses_count;
	}
}