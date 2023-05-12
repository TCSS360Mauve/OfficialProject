package model;

import com.github.cliftonlabs.json_simple.*;
import repository.AboutAppRepository;

import java.io.*;
import java.util.ArrayList;

/**
 * A class to store info about the FileNtro app.
 *
 * @author Hassan Abbas
 * @author Riley Bennett
 * @version 0.1
 */
public class AppInfo implements Jsonable {


    public AppInfo(User user) {
        this.user = user;
    }

    public static void main(String[] args) throws IOException, JsonException {
        AboutAppRepository aboutAppRepository = new AboutAppRepository();
        AppInfo currentInfo = aboutAppRepository.getAppInfo();
        System.out.println(currentInfo.versionNumber);
    }
    
    /** The version number of the app. */
    private  double versionNumber;
    
    /** The developers' team name. */
    private final String teamName = "Team Mauve";
    
    /** A list to store each developer. */
    private final ArrayList<Account> teamMembers = new ArrayList<>(); 
    
    /** The current user of the app. */
    private User user;

    /**
     * Constructor to add developer names and set the user.
     *
     * @param versionNumber
     * @param user          The user of the app
     */
    public AppInfo(double versionNumber, final User user) {
        this.versionNumber = versionNumber;
        this.user = user;      
//        teamMembers.add(new Account("Hassan Abbas", "habbas91@uw.edu"));
//        teamMembers.add(new Account("Riley Bennett", "benn3230@uw.edu"));
//        teamMembers.add(new Account("Thinh Le", "lenguyenducthinh2003@gmail.com"));
//        teamMembers.add(new Account("Bairu Li", "bairul@uw.edu"));
//        teamMembers.add(new Account("Tin Phu", "phuhutin@uw.edu"));
    }

    /**
     *
     */
    public AppInfo(double versionNumber) {

        this.versionNumber = versionNumber;
    }
    public AppInfo() {


    }
    
    /**
     * A getter for the user object.
     *
     * @return The current user
     */
    public User getUser() {
        return user;
    }
    
    /**
     * A getter for the developer team name.
     *
     * @return The developer team name.
     */
    public String getTeamName() {
        return teamName;
    }
    
    /**
     * A getter for the app version number.
     *
     * @return The version of the app
     */
    public double getVersion() {
        return versionNumber; 
    }
    
    /**
     * A getter for the developers of the app.
     *
     * @return List of the developers
     */
    public ArrayList<Account> getDevelopers() {
        ArrayList<Account> copy = (ArrayList<Account>)(teamMembers.clone());
        return copy;
    }

    public void addNewDeveloper(Account theAccount){
        this.teamMembers.add(theAccount);
    }


    /**
     * User Setter.
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public String toJson() {
        final StringWriter writable = new StringWriter();
        try {
            this.toJson(writable);
        } catch (final IOException e) {
        }
        return writable.toString();
    }

    public double getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(double versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Override
    public void toJson(Writer writer) throws IOException {
        final JsonObject json = new JsonObject();
        json.put("versionNumber", this.versionNumber);
        json.put("teamName", this.teamName);
        json.put("teamMembers", this.teamMembers);

        json.toJson(writer);
    }
    public void addMember(Account theMember){
        this.teamMembers.add(theMember);
    }
}