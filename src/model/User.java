package model;
/**
 * A class to store information of the user of the app/
 * 
 * @author Bairu Li
 * @author Riley Bennett
 * @version 0.1
 */
public class User extends Account {


	/**
	 * Constructs a user with a name and a email.
	 * 
	 * @param theName
	 * @param theEmail
	 */
	public User(final String theName, final String theEmail) {
		super(theName, theEmail);
	}

	public User(final String theName, final String theEmail, String theId) {
		super(theName, theEmail, theId);
	}
	
	/**
	 * Sets the name of the user.
	 * 
	 * @param theName
	 */
	public void setName(final String theName) {
		name = theName;
	}
	
	/**
	 * Sets the email of the user.
	 * 
	 * @param theEmail
	 */
	public void setEmail(final String theEmail) {
		email = theEmail;
	}
}

