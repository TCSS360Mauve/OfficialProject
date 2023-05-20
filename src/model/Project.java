package model;


import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

/**
 * This Project is to handle the budget for the user.
 *
 * @author Thinh Le
 * @version 0.1
 */
public class Project implements Jsonable {
     /**
     * id for project
     */

   private final String id;
    /**
     * project name
     */
    private  String projectName;

 /**
     * this is the total cost
     */
    private BigDecimal totalCost = BigDecimal.valueOf(0);
    /**
     * This is the budget of a user.
     */
    private BigDecimal budget = BigDecimal.valueOf(0);

    /**
     * this is the User Id which this document belongs to.
     */
    private final String userID;



 /**
     * The date that users buy item.
     */
    private LocalDate  date;


    /**
     * This constructor helps to calculate the cost of item for user, and let the uses to see the current
     * budget, and this is also helps them to track item by date and total cost.
     *
     * @param theProjectName
      * @param totalCost
     * @param budget
     * @param theUserID
     *
     */
    public Project( String theProjectName, BigDecimal totalCost, BigDecimal budget, String theUserID){
     this.totalCost = totalCost;
     this.budget = budget;
     this.id =  UUID.randomUUID().toString();
     this.date = LocalDate.now();
     this.userID = theUserID;
     this.projectName = theProjectName;
    }

    /**
     * DO NOT CALL THIS CONSTRUCTOR TO CREATE A NEW PROJECT.
     * THIS IS FOR DATA MAPPING ONLY.
     * @author Tin Phu
     * @param theId
     * @param theProjectName
     * @param totalCost
     * @param budget
     * @param theUserID
     */
    public Project( String theId, String theProjectName, BigDecimal totalCost, BigDecimal budget, String theUserID, LocalDate theDate){
        this.totalCost = totalCost;
        this.budget = budget;
        this.date = theDate;
        this.userID = theUserID;
        this.projectName = theProjectName;
        this.id = theId;
    }

    /**
     * Getter for id.
     * @return
     */
    public String getId(){
        return id;
    }



    /**
     * Getter for date.
     * @return
     */
    public LocalDate  getDate(){
        return date;
    }

    /**
     * Getter for total cost.
     * @return
     */
    public BigDecimal getTotalCost(){
        return totalCost;
    }

    /**
     * Getter for budget.
     * @return
     */
    public BigDecimal getBudget(){
    return budget;
    }

    /**
     * Getter for balance.
     * @return
     */
    public BigDecimal getBalance(){
    return budget.subtract(totalCost);
    }






    /**
     * This is the setter for budget
     * @param budget
     */
    public void setBudget(BigDecimal budget){
        this.budget = budget;
    }

    /**
     *
     * This is the seeter for total caost
     * @param totalCost
     */
    public void setTotalCost(BigDecimal totalCost){
        this.totalCost = totalCost;
    }

   /**
    * OwnerName getter
    * @return
     */
     public String getUserID() {
        return this.userID;
    }


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

    @Override
    public void toJson(Writer writer) throws IOException {
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
     * Overide toString()
     * @author Tin Phu
     * @return
     */
    @Override
    public String toString(){
         return "id:" + this.id + "|" + "projectName:" + this.projectName + "|userID:" + this.userID + "| totalCost" + this.totalCost + "| budget:" + this.budget + "\n";

    }
}
