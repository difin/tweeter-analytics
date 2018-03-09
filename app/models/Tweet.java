package models;

public class Tweet {
	
	private String created_at;
	private String full_text;
	private UserProfile user;
	
	public Tweet(){
		
	}
			
	public Tweet(String created_at, String full_text, String account){
		this.created_at = created_at;
		this.full_text = full_text;
	}
	
	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getFull_text() {
		return full_text;
	}

	public void setFull_text(String full_text) {
		this.full_text = full_text;
	}

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}
	
	public String toString(){
		return "created_at: " + created_at + ";\t" +
				"extended_text: " + full_text + ";\t" +
				"screen_name: " + user.getScreen_name();
	}
}
