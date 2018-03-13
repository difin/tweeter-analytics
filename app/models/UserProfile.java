package models;

import java.util.List;

/**
 * Represents User Profile object, in which we are storing user's data like name,
 * username , description , followers list etc. from incoming JSON response.
 * @author Dmitriy Fingerman
 * @version 1.0.0
 */

public class UserProfile{
	
	/**
	 * User name
	 */
	private String name;
	/**
	 * User ID
	 */
	private String screen_name;
	/**
	 * Profile description
	 */
	private String description;
	/**
	 * Number of the followers
	 */
	private long followers_count;
	/**
	 * Number of friends
	 */
	private long friends_count;
	/**
	 * Number of favourites
	 */
	private long favourites_count;
	/**
	 * reation date
	 */
	private String created_at;
	/**
	 * Time zone details
	 */
	private String time_zone;
	/**
	 * Number of statuses
	 */
	private String statuses_count;
	/**
	 * Tweets of this user
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
	 * Returns name of the user
	 * @return name of the user
	 */
	
	public String getName() {
		return name;
	}
	
	/**
	 * Sets user name
	 * @param name set user name
	 */

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns screen name of the user
	 * @return user's screen name
	 */

	public String getScreen_name() {
		return screen_name;
	}
	
	/**
	 * Sets user's screen name
	 * @param screen_name Twitter User Screen Name
	 */

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}
	
	/**
	 * Returns description of user
	 * @return description of user
	 */

	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets User's description
	 * @param description Sets user description
	 */

	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns number of follower of this user
	 * @return receiving follower's count
	 */

	public long getFollowers_count() {
		return followers_count;
	}
	
	/**
	 * Sets how many followers this twitter user has
	 * @param followers_count follower's figure
	 */

	public void setFollowers_count(long followers_count) {
		this.followers_count = followers_count;
	}
	
	/**
	 * Returns number of the friends of this user
	 * @return number of friends this user has
	 */

	public long getFriends_count() {
		return friends_count;
	}
	
	/**
	 * Sets the number of friends this twitter user has
	 * @param friends_count Setting up friends count for particular user
	 */

	public void setFriends_count(long friends_count) {
		this.friends_count = friends_count;
	}
	/**
	 * Returns number of favorites for this twitter user
	 * @return retrieve number of favorites
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
	 * Returns creation date of given User Profile
	 * @return Creation date of User Profile
	 */

	public String getCreated_at() {
		return created_at;
	}
	
	/**
	 * Sets creation date of profile from JSON response
	 * @param created_at set creation date of user profile
	 */

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
	/**
	 * Returns time zone information of this user
	 * @return time zone information of this twitter user
	 */

	public String getTime_zone() {
		return time_zone;
	}
	
	/**
	 * Sets time zone information for this User Profile
	 * @param time_zone TimeZone information for this user
	 */

	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}

	/**
	 * Returns status count information for given user
	 * @return number of status for given user
	 */
	public String getStatuses_count() {
		return statuses_count;
	}
	
	/**
	 * Sets how many statuses this user has
	 * @param statuses_count status count of this user
	 */
	public void setStatuses_count(String statuses_count) {
		this.statuses_count = statuses_count;
	}
	
	/**
	 * Returns associated list of tweets for this user profile
	 * @return list of tweets
	 */

	public List<Tweet> getTweets() {
		return tweets;
	}
	
	/**
	 * Sets tweets for this user profile
	 * @param tweets list of tweets
	 */

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}
	
	/**
	 * Returns url of profile picture for this user
	 * @return url of profile picture for this user
	 */
		
	public String getProfile_image_url() {
		return profile_image_url;
	}
	
	/**
	 * Stores URL of profile picture of incoming twitter user
	 * @param profile_image_url url to image profile picture
	 */

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
		setProfile_image_url_400x400(profile_image_url.replace("normal", "400x400"));
	}
	
	/**
	 * Returns URL of enlarged profile picture of size 400*400
	 * @return URL of enlarged profile picture
	 */

	public String getProfile_image_url_400x400() {
		return profile_image_url_400x400;
	}
	
	/**
	 * Sets URL of profile picture where photograph dimension is 400*400.
	 * @param profile_image_url_400x400 enlarged profile picture URL.
	 */

	public void setProfile_image_url_400x400(String profile_image_url_400x400) {
		this.profile_image_url_400x400 = profile_image_url_400x400;
	}
	
	/**
	 * Returns string representation of this data model
	 * @return string representation of this data model
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