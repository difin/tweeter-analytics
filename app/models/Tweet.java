package models;

/**
 * Tweet class implements functionality of adding every tweet as object from JSON response.
 * @author Mayank Acharya
 * @version 1.0.0
 *
 */

public class Tweet {
	
	private String created_at;
	private String full_text;
	private UserProfile user;
	
	/**
	 * Default Constructor.
	 */
	
	public Tweet(){
		
	}
	
	/**
	 * Parametarized constructor.
	 * @param created_at
	 * @param full_text
	 * @param account
	 */
			
	public Tweet(String created_at, String full_text, String account){
		this.created_at = created_at;
		this.full_text = full_text;
	}
	
	/**
	 * 
	 * @return created_at Creation Date.
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
	 * @return full_text receive full text.
	 */

	public String getFull_text() {
		return full_text;
	}
	
	/**
	 * 
	 * @param full_text set tweet text.
 	 */

	public void setFull_text(String full_text) {
		this.full_text = full_text;
	}

	/**
	 * 
	 * @return user return profile object. 
	 */
	public UserProfile getUser() {
		return user;
	}

	/**
	 * 
	 * @param user set userprofile object.
	 */
	public void setUser(UserProfile user) {
		this.user = user;
	}
	
	
	public String toString(){
		return "created_at: " + created_at + ";\t" +
				"extended_text: " + full_text + ";\t" +
				"screen_name: " + user.getScreen_name();
	}
}
