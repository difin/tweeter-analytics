package models;

import java.util.List;

/**
 * 
 * @author Dimitry Fingerman
 * @version 1.0.0
 */

public class UserProfile{

	private String name;
	private String screen_name;
	private String description;
	private long followers_count;
	private long friends_count;
	private long favourites_count;
	private String created_at;
	private String time_zone;
	private String statuses_count;
	private List<Tweet> tweets;
	private String profile_image_url;
	private String profile_image_url_400x400;
	
	/**
	 * 
	 * @return name Name of the Twitter User.
	 */
	
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param name set name.
	 */

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return screen_name
	 */

	public String getScreen_name() {
		return screen_name;
	}
	
	/**
	 * 
	 * @param screen_name set the screen name.
	 */

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}
	
	/**
	 * 
	 * @return get description.
	 */

	public String getDescription() {
		return description;
	}
	
	/**
	 * 
	 * @param description set description.
	 */

	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 
	 * @return return follower count.
	 */

	public long getFollowers_count() {
		return followers_count;
	}
	
	/**
	 * 
	 * @param followers_count set follower count.
	 */

	public void setFollowers_count(long followers_count) {
		this.followers_count = followers_count;
	}
	
	/**
	 * 
	 * @return get friend count.
	 */

	public long getFriends_count() {
		return friends_count;
	}
	
	/**
	 * 
	 * @param friends_count set friend count.
	 */

	public void setFriends_count(long friends_count) {
		this.friends_count = friends_count;
	}
	/**
	 * 
	 * @return get favorite.
	 */
	public long getFavourites_count() {
		return favourites_count;
	}
	
	/**
	 * 
	 * @param favourites_count set favorite.
	 */

	public void setFavourites_count(long favourites_count) {
		this.favourites_count = favourites_count;
	}
	
	/**
	 * 
	 * @return creation date.
	 */

	public String getCreated_at() {
		return created_at;
	}
	
	/**
	 * 
	 * @param created_at set creation date.
	 */

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
	/**
	 * 
	 * @return time zone information.
	 */

	public String getTime_zone() {
		return time_zone;
	}
	
	/**
	 * 
	 * @param time_zone set timezone information.
	 */

	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}

	/**
	 * 
	 * @return status count information.
	 */
	public String getStatuses_count() {
		return statuses_count;
	}
	
	/**
	 * 
	 * @param statuses_count set status count.
	 */
	public void setStatuses_count(String statuses_count) {
		this.statuses_count = statuses_count;
	}
	
	/**
	 * 
	 * @return list of tweets.
	 */

	public List<Tweet> getTweets() {
		return tweets;
	}
	
	/**
	 * 
	 * @param tweets set list of tweets.
	 */

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}
	
	/**
	 * 
	 * @return image url.
	 */
		
	public String getProfile_image_url() {
		return profile_image_url;
	}
	
	/**
	 * 
	 * @param profile_image_url set profile image url.
	 */

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
		setProfile_image_url_400x400(profile_image_url.replace("normal", "400x400"));
	}
	
	/**
	 * 
	 * @return get image (400*400) url. 
	 */

	public String getProfile_image_url_400x400() {
		return profile_image_url_400x400;
	}
	
	/**
	 * 
	 * @param profile_image_url_400x400 set profile image (400*400) url.
	 */

	public void setProfile_image_url_400x400(String profile_image_url_400x400) {
		this.profile_image_url_400x400 = profile_image_url_400x400;
	}

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
