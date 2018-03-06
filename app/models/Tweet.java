package models;

public class Tweet {
	
	private String created_at;
	private String text;
	private UserProfile user;
	
	public Tweet(){
		
	}
			
	public Tweet(String created_at, String text, String account){
		this.created_at = created_at;
		this.text = text;
	}
	
	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}
	
	public String toString(){
		return "created_at: " + created_at + ";\t" +
				"text: " + text + ";\t" +
				"screen_name: " + user.getScreen_name();
	}
}
