package models;

public class UserProfile{

	public String name;
	public String screen_name;
	public String description;
	public long followers_count;
	public long friends_count;
	public long favourites_count;
	public String created_at;
	public String time_zone;
	public String statuses_count;
	
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
