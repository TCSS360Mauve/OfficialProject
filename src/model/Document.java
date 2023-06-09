package model;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.UUID;

/**
 * This class has information about document such as document name, file path, document description, and id.
 *
 * @author Thinh Le
 * @author Tin Phu
 * @version 0.3
 */
public class Document implements Jsonable {

    /**
     * This is name of document.
     */
    private String documentName;

    /**
     * This is the file path to open.
     */
    private String filePath ="not yet";

    /**
     * This is description about the document.
     */
    private String documentDescription;

    /**
     * Cost associated with the document.
     */
    private BigDecimal totalCost;

    /**
     * This is the projectID which this document belongs to.
     */
    private final String projectID;

    /**
     * This is the User Id which this document belongs to.
     */
    private final String userID;

    /**
     * This is the id for the document.
     */
    private final String id;

    /**
     * This is the date of the document.
     */
    private final LocalDate date;

    /**
     * This constructor creates Document with id, document name, document description.
     * @author Thinh Le
     * @author Tin Phu
     * @param documentName Name of the document.
     * @param documentDescription Description associated with the document.
     * @param theProjectId The id of the project this document is in.
     * @param theUserID The id of the user this document belongs to.
     * @param theTotalCost The cost associated with this document .
     */
    public Document(final String documentName, final String documentDescription, final String theProjectId, 
                    final String theUserID, final BigDecimal theTotalCost){

        this.documentName = documentName;
        this.documentDescription = documentDescription;
        this.id =   UUID.randomUUID().toString();
        this.projectID = theProjectId;
        this.userID = theUserID;
        this.date = LocalDate.now();
        this.totalCost = theTotalCost;
        this.filePath = "\\" + theProjectId + this.id;
    }

    /**
     * This constructor creates Document with id, document name, document description and "file path".
     * @author Thinh Le
     * @author Tin Phu
     * @param documentName Name of the document.
     * @param documentDescription Description associated with the document.
     * @param theProjectId The id of the project this document is in.
     * @param theUserID The id of the user this document belongs to.
     * @param theTotalCost The cost associated with this document.
     */
    public Document(final String documentName, final String documentDescription, final String theProjectId, 
                    final String theUserID, final BigDecimal theTotalCost, final String srcFileString) throws IOException {

        this.documentName = documentName;
        this.documentDescription = documentDescription;
        this.id =   UUID.randomUUID().toString();
        this.projectID = theProjectId;
        this.userID = theUserID;
        this.date = LocalDate.now();
        this.totalCost = theTotalCost;
        this.filePath = this.copyFileToAppRep(srcFileString);
    }

    /**
     * A constructor for data mapping.
     * @author Tin Phu
     * @param documentName Name of the document
     * @param documentDescription Description associated with the document
     * @param theProjectId The id of the project this document is in
     * @param theUserID The id of the user this document belongs to
     * @param theTotalCost The cost associated with this document 
     * @param theID The id of this document
     * @param theDate The date associated with this document
     * @param thePath The file path of this document
     */
    public Document(final String documentName, final String documentDescription, final String theProjectId, 
                    final String theUserID, final BigDecimal theTotalCost, final String theID, final LocalDate theDate, 
                    final String thePath ){

        this.documentName = documentName;
        this.documentDescription = documentDescription;
        this.id =   theID;
        this.projectID = theProjectId;
        this.userID = theUserID;
        this.date = theDate;
        this.totalCost = theTotalCost;
        this.filePath = thePath;
    }

    /**
     * Returns the ID of this document.
     * @author Thinh Le
     * @return The ID of the document.
     */
    public String id(){
        return id;
    }

    /**
     * Returns the name of this document.
     * @author Thinh Le
     * @return The name of the document.
     */
    public String getDocumentName(){
        return documentName;
    }

    /**
     * Returns the description of this document.
     * @author Thinh Le
     * @return This document's description.
     */
    public String getDocumentDescription(){
        return documentDescription;
    }

    /**
     * Sets the name of the document.
     * @author Thinh Le
     * @param documentName The new name of this document.
     */
    public void setDocumentName(final String documentName){
        this.documentName = documentName;
    }

    /**
     * Sets the file path of this document.
     * @author Thinh Le
     * @param filepath The new file path of this document.
     */
    public void setFilepath(final String filepath){
        this.filePath = filepath;
    }

    /**
     * Sets the description of this document.
     * @author Thinh Le
     * @param documentDescription The new description of this document.
     */
    public void setDocumentDescription(final String documentDescription){
        this.documentDescription = documentDescription;
    }

    /**
     * Returns the ID of this document.
     * @author Tin Phu
     * @return The ID of this document.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the cost associated with this document.
     * @author Tin Phu
     * @param totalCost The cost to associate this document with.
     */
    public void setTotalCost(final BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * Returns the id of the project this document is in.
     * @author Tin Phu
     * @return The ID of the project.
     */
    public String getProjectID() {
        return projectID;
    }

    /**
     * Returns the cost associated with this document.
     * @author Tin Phu
     * @return The cost associated with this document.
     */
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    /**
     * Returns the ID of the user this document belongs to.
     * @author Tin Phu
     * @return The ID of the user.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Converts this document to JSON formatted stream.
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
     * Converts this document to JSON formatted stream.
     * @author Tin Phu
     * @param writer Writer to write to stream with.
     * @throws IOException
     */
    @Override
    public void toJson(final Writer writer) throws IOException {
        final JsonObject json = new JsonObject();
        json.put("id", this.id);
        json.put("totalCost", this.totalCost.toString());
        json.put("documentDescription", this.documentDescription);
        json.put("projectID", this.projectID);
        json.put("date",  this.date.toString());
        json.put("userID", this.userID);
        json.put("documentName", this.documentName);
        json.put("filePath",this.filePath);
        json.toJson(writer);
    }

    /**
     * Returns a String representation of this document.
     * @author Tin Phu
     * @return String representation of this document.
     */
    @Override
    public String toString(){
        return "id: " + this.id + "|documentName:"+this.documentName + "|documentDescription:" + this.documentDescription +"|projectID:" + this.projectID
                + "|userID:"+ this.userID+"|totalCost:" + this.totalCost + "|filePath:" + this.filePath + "|date:" + this.date + "\n" ;
    }

    /**
     * Returns the date of this document.
     * @author Tin Phu
     * @return The date of this document.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Copies a file from user input to a file path destination.
     * @author Tin Phu
     * @param srcString File path string.
     * @return Destination file path.
     * @throws IOException
     */
    private String copyFileToAppRep(final String srcString) throws IOException {
        File src = new File(srcString);
        String fileType = "";
        for(int i = srcString.length()-1; i >= 0; i-- ){
            if(srcString.charAt(i) != '.'){
                fileType =  srcString.charAt(i) + fileType;
            } else {
                fileType = "." + fileType;
                break;
            }
        }
        String currentPath = System.getProperty("user.dir");
        String destString = "\\projects\\" + this.projectID + "\\" + this.id + fileType;
        File dest = new File(currentPath + destString);
        Files.copy(src.toPath(), dest.toPath());
        return destString;
    }

    /**
     * Opens a file associated with a document.
     * @author Tin Phu
     * @return True if the file exists, false otherwise.
     */
    public boolean openDoc(){
        String currentPath = System.getProperty("user.dir");

        File file = new File(currentPath + this.filePath);
        Desktop desktop = Desktop.getDesktop();
        if(file.exists())         //checks file exists or not
        {
            try {
                desktop.open(file);              //opens the specified file
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return true;
        }
        return false;
    }

    /**
     * Returns the file path of this document.
     * @author Tin Phu
     * @return The file path of this document.
     */
    public String getFilePath() {
        return filePath;
    }
}