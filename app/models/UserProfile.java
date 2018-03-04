package models;

import java.util.List;

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
	private String profile_image_url_https;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getFollowers_count() {
		return followers_count;
	}

	public void setFollowers_count(long followers_count) {
		this.followers_count = followers_count;
	}

	public long getFriends_count() {
		return friends_count;
	}

	public void setFriends_count(long friends_count) {
		this.friends_count = friends_count;
	}

	public long getFavourites_count() {
		return favourites_count;
	}

	public void setFavourites_count(long favourites_count) {
		this.favourites_count = favourites_count;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getTime_zone() {
		return time_zone;
	}

	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}

	public String getStatuses_count() {
		return statuses_count;
	}

	public void setStatuses_count(String statuses_count) {
		this.statuses_count = statuses_count;
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}
		
	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public String getProfile_image_url_https() {
		return profile_image_url_https;
	}

	public void setProfile_image_url_https(String profile_image_url_https) {
		this.profile_image_url_https = profile_image_url_https;
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
