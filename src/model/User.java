package model;
/**
 * A class to store information of the user of the app.
 * 
 * @author Bairu Li
 * @author Tin Phu
 * @version 0.3
 */
public class User extends Account {

	/**
	 * Constructs a user with a name and a email.
	 * @author Bairu Li
	 * @param theName The name of the user.
	 * @param theEmail The email of the user.
	 */
	public User(final String theName, final String theEmail) {
		super(theName, theEmail);
	}

	/**
	 * Constructor for data mapping.
	 * @author Tin Phu
	 * @param theName The name of the user.
	 * @param theEmail The email of the user.
	 * @param theId The id of the user.
	 */
	public User(final String theName, final String theEmail, String theId) {
		super(theName, theEmail, theId);
	}
	
	/**
	 * Sets the name of the user.
	 * @author Bairu Li
	 * @param theName The new name of the user.
	 */
	public void setName(final String theName) {
		name = theName;
	}
	
	/**
	 * Sets the email of the user.
	 * @author Bairu Li
	 * @param theEmail The new email of the user.
	 */
	public void setEmail(final String theEmail) {
		email = theEmail;
	}
}

