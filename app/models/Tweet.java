package models;

import java.util.Objects;

/**
 * Represents tweet object, in which it stores details of every single tweet like creation date,
 * text and User Profile detail from JSON response of Twitter API.
 *
 * @author Mayank Acharya
 * @version 1.0.0
 */

public class Tweet {
    /**
     * Creation date in string format.
     */
    private String created_at;

    /**
     * Full text from tweet in string format.
     */
    private String full_text;

    /**
     * User's data in User Profile Model.
     */
    private UserProfile user;

    /**
     * Default Constructor.
     */
    public Tweet() {
    }

    /**
     * Creates new tweet object based on input parameters.
     *
     * @param created_at Creation Date.
     * @param full_text  Full Tweet text.
     */
    public Tweet(String created_at, String full_text) {
        this.created_at = created_at;
        this.full_text = full_text;
    }

    /**
     * Returns creation date of tweet.
     *
     * @return creation date of tweet.
     */
    public String getCreated_at() {
        return created_at;
    }

    /**
     * Sets creation date of tweet.
     *
     * @param created_at Creation date of tweet.
     */
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    /**
     * Returns tweet's full text in string format.
     *
     * @return Tweet's text in string representation.
     */
    public String getFull_text() {
        return full_text;
    }

    /**
     * Sets tweet text in string representation form.
     *
     * @param full_text Tweet's text.
     */
    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }

    /**
     * Returns User Profile class's object.
     *
     * @return user UserProfile associated with present tweet.
     */
    public UserProfile getUser() {
        return user;
    }

    /**
     * Sets User Profile associated with given tweet.
     *
     * @param user Creates user profile object and store it with attached tweet.
     */
    public void setUser(UserProfile user) {
        this.user = user;
    }

    /**
     * @return String representation of every information that tweet object contains.
     */
    public String toString() {
        return "created_at: " + created_at + ";\t" +
                "extended_text: " + full_text + ";\t" +
                "screen_name: " + user.getScreen_name();
    }

    /**
     * Checks equality of two Tweet objects
     *
     * @param o Tweet to compare to
     * @return result of check
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tweet)) return false;
        Tweet tweet = (Tweet) o;
        return Objects.equals(getCreated_at(), tweet.getCreated_at())
                && Objects.equals(getFull_text(), tweet.getFull_text())
                && Objects.equals(getUser(), tweet.getUser());
    }

    /**
     * Creates a hashcode
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {

        return Objects.hash(getCreated_at(), getFull_text(), getUser());
    }
}