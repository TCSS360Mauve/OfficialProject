package model;


import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * A class to represent a Project in the FileNtro program.
 *
 * @author Thinh Le
 * @author Tin Phu
 * @author Riley Bennett
 * @version 0.3
 */
public class Project implements Jsonable {

    /**
     * The ID of the project.
     */
   private final String id;

    /**
     * The name of the project.
     */
    private String projectName;

    /**
     * The total cost of the project.
     */
    private BigDecimal totalCost = BigDecimal.valueOf(0);

    /**
     * The budget of this project.
     */
    private BigDecimal budget = BigDecimal.valueOf(0);

    /**
     * The ID of the user this project belongs to.
     */
    private final String userID;

    /**
     * The date of this project.
     */
    private LocalDate date;

    /**
     * This constructor helps to calculate the cost of item for user, and let the uses to see the current
     * budget, and this is also helps them to track item by date and total cost.
     * @author Tin Phu
     * @param theProjectName The name of the project.
     * @param totalCost The total cost of the project.
     * @param budget The budget of the project.
     * @param theUserID The id of the user this project belongs to.
     *
     */
    public Project(final String theProjectName, final BigDecimal totalCost, final BigDecimal budget, 
                   final String theUserID){

        this.totalCost = totalCost;
        this.budget = budget;
        this.id =  UUID.randomUUID().toString();
        this.date = LocalDate.now();
        this.userID = theUserID;
        this.projectName = theProjectName;
        this.createProjectFolder();
    }

    /**
     * Constructor for data mapping.
     * @author Tin Phu
     * @param theId The id of the project.
     * @param theProjectName The name of the project.
     * @param totalCost The total cost of the project.
     * @param budget The budget of the project.
     * @param theUserID The id of the user this project belongs to.
     */
    public Project(final String theId, final String theProjectName, final BigDecimal totalCost, 
                   final BigDecimal budget, final String theUserID, final LocalDate theDate){

        this.totalCost = totalCost;
        this.budget = budget;
        this.date = theDate;
        this.userID = theUserID;
        this.projectName = theProjectName;
        this.id = theId;
    }

    /**
     * Returns the ID of this project.
     * @author Thinh Le
     * @return The ID of this project.
     */
    public String getId(){
        return id;
    }

    /**
     * Returns the date of this project.
     * @author Thinh Le
     * @return The date of this project.
     */
    public LocalDate getDate(){
        return date;
    }

    /**
     * Returns the total cost of this project.
     * @author Thinh Le
     * @return The total cost of this project.
     */
    public BigDecimal getTotalCost(){
        return totalCost;
    }

    /**
     * Returns the budget of this project.
     * @author Thinh Le
     * @return The budget of this project.
     */
    public BigDecimal getBudget(){
        return budget;
    }

    /**
     * Returns the remaining balance of this project.
     * @author Thinh Le
     * @return The balance left on this project.
     */
    public BigDecimal getBalance(){
        return budget.subtract(totalCost);
    }

    /**
     * Sets the budget for this project.
     * @author Thinh Le
     * @param budget The new budget of this project.
     */
    public void setBudget(final BigDecimal budget){
        this.budget = budget;
    }

    /**
     * Sets the total cost of this project.
     * @author Thinh Le
     * @param totalCost The new total cost of this project.
     */
    public void setTotalCost(final BigDecimal totalCost){
        this.totalCost = totalCost;
    }

    /**
     * Returns the ID of the user this project belongs to.
     * @author Tin Phu
     * @return The ID of the user.
     */
    public String getUserID() {
        return this.userID;
    }

    /**
     * Converts this project to JSON formatted stream.
     * @author Tin Phu
     * @return String JSON formatted stream of this document.
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
     * Converts this project to JSON formatted stream.
     * @author Tin Phu
     * @param writer Writer to write to stream with.
     */
    @Override
    public void toJson(final Writer writer) throws IOException {
        final JsonObject json = new JsonObject();
        json.put("id", this.id);
        json.put("totalCost", this.totalCost.toString());
        json.put("budget", this.budget.toString());
        json.put("userID", this.userID);
        json.put("date",  this.date.toString());
        json.put("projectName", this.projectName);
        json.toJson(writer);

    }

    /**
     * Returns a String representation of this project.
     * @author Tin Phu
     * @return String representation of this project.
     */
    @Override
    public String toString(){
         return "id:" + this.id + "|" + "projectName:" + this.projectName + "|userID:" + this.userID + 
         "| totalCost" + this.totalCost + "| budget:" + this.budget + "\n";
    }

    /**
     * Creates A folder for a project at currentPath\id.
     * @author Tin Phu
     */
    private void createProjectFolder(){
        String currentPath = System.getProperty("user.dir");
        File theDir = new File(currentPath + "\\projects\\"+ this.id);
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }

    /**
     * Returns this project's name.
     * @author Tin Phu
     * @return The project name.
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets this project's name to the specified name.
     * @author Riley Bennett
     * @param theName String new name of the project.
     */
    public void setProjectName(String theName) {
        projectName = theName;
    }
}
