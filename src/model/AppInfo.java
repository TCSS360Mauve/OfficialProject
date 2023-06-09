package model;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import java.io.*;
import java.util.ArrayList;

/**
 * A class to store info about the FileNtro app.
 *
 * @author Hassan Abbas
 * @author Riley Bennett
 * @author Tin Phu
 * @version 0.3
 */
public class AppInfo implements Jsonable {

    /** The version number of the app. */
    private double versionNumber;
    
    /** The developers' team name. */
    private final String teamName = "Team Mauve";
    
    /** A list to store each developer. */
    private final ArrayList<Account> teamMembers = new ArrayList<>(); 
    
    /** The current user of the app. */
    private User user;

    /**
     * Constructor to create an AppInfo object with the current user
     * @author Hassan Abbas
     * @param user The current user of the app
     */
    public AppInfo(final User user) {
        this.user = user;
    }

    /**
     * Constructor to add developer names and set the user.
     * @author Hassan Abbas
     * @param versionNumber The version number of the app
     * @param user The current user of the app
     */
    public AppInfo(final double versionNumber, final User user) {
        this.versionNumber = versionNumber;
        this.user = user;
    }

    /**
     * Constructor to create an AppInfo object with the version number of the app.
     * @author Tin Phu
     * @param versionNumber The version number of the app
     */
    public AppInfo(final double versionNumber) {

        this.versionNumber = versionNumber;
    }
    
    /**
     * Empty constructor
     */
    public AppInfo() {
        // Empty Constructor
    }

    /**
     * A getter for the user object.
     * @author Hassan Abbas
     * @return The current user
     */
    public User getUser() {
        return user;
    }
    
    /**
     * A getter for the developer team name.
     * @author Hassan Abbas
     * @return The developer team name
     */
    public String getTeamName() {
        return teamName;
    }
    
    /**
     * A getter for the app version number.
     * @author Hassan Abbas
     * @return The version of the app
     */
    public double getVersion() {
        return versionNumber; 
    }
    
    /**
     * A getter for the developers of the app.
     * @author Tin Phu
     * @return ArrayList of the developers
     */
    public ArrayList<Account> getDevelopers() {
        ArrayList<Account> copy = (ArrayList<Account>)(teamMembers.clone());
        return copy;
    }

    /**
     * Adds an account to the developers list.
     * @author Tin Phu
     * @param theAccount The account to be added
     */
    public void addNewDeveloper(final Account theAccount){
        this.teamMembers.add(theAccount);
    }


    /**
     * Clears list of developers.
     * @author Riley Bennett
     */
    public void clearDevelopers() {
        teamMembers.clear();
    }

    /**
     * Sets the current user to the specified user.
     * @author Tin Phu
     * @param user The current user to be set
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * Converts this AppInfo object to JSON formatted stream.
     * @author Tin Phu
     * @return String JSON formatted stream of this AppInfo object
     */
    @Override
    public String toJson() {
        final StringWriter writable = new StringWriter();
        try {
            this.toJson(writable);
        } catch (final IOException e) {
            throw new RuntimeException();
        }
        return writable.toString();
    }

    /**
     * Sets the version number of the app.
     * @author Tin Phu
     * @param versionNumber The version number to be set to.
     */
    public void setVersionNumber(final double versionNumber) {
        this.versionNumber = versionNumber;
    }

    /**
     * Converts this account to JSON formatted stream.
     * @author Tin Phu
     * @param writer Writer to write to stream with.
     * @throws IOException
     */
    @Override
    public void toJson(final Writer writer) throws IOException {
        final JsonObject json = new JsonObject();
        json.put("versionNumber", this.versionNumber);
        json.put("teamName", this.teamName);
        json.put("teamMembers", this.teamMembers);
        json.put("user", this.user);

        json.toJson(writer);
     }

    /**
     * Removes a developer by their name.
     * @author Tin Phu
     * @param theName The name of the developer to be removed
     */
    public void removeDevByName(final String theName){
        for(int i = 0; i < this.teamMembers.size(); i++){
            if(this.teamMembers.get(i).getName().equals(theName)){
//                System.out.println("remove" + this.teamMembers.get(i).getName() );
                this.teamMembers.remove(i);

            }
        }
    }
}