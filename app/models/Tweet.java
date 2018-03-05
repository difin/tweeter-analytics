package models;

public class Tweet {
	
	private String created_at;
	private String text;
	private String account; // Feel free to change this field if needed.
	
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

	public String toString(){
		return "created_at: " + created_at + ";\t" +
				"text: " + text;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
