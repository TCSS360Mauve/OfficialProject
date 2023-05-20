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
 * This class has information about document such as document name, file path, document description, and id.
 *
 * @author Thinh Le
 * @version 0.1
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
        This is description about document.
     */
    private String documentDescription;
    /**
     * Total cost of anything that comes with this documents.
     */
    private BigDecimal totalCost;
    /**
     * this is the projectID which this document belongs to.
     */
    private final String projectID;

    /**
     * this is the User Id which this document belongs to.
     */
    private final String userID;
    /**
        This is the id for document
     */
    private final String id;

    private final LocalDate date;

    /**
     * This constructor creates Document witt id, document name, document description and file path.
     * @param documentName
     * @param documentDescription

     */
    public Document( String documentName, String documentDescription, String theProjectId, String theUserID, BigDecimal theTotalCost){
    this.documentName = documentName;
    this.documentDescription = documentDescription;
    this.id =   UUID.randomUUID().toString();
    this.projectID = theProjectId;
    this.userID = theUserID;
    this.date = LocalDate.now();
    this.totalCost = theTotalCost;
}

    /**
     * @Author Tin Phu
     * DO NOT USE TO CREATE   AN INSTANCE
     * THIS IS FOR DATA MAPPING ONLY
     * @param documentName
     * @param documentDescription
     * @param theProjectId
     * @param theUserID
     * @param theTotalCost
     * @param theID
     * @param theDate
     * @param thePath
     */
    public Document(String documentName, String documentDescription, String theProjectId, String theUserID, BigDecimal theTotalCost, String theID, LocalDate theDate, String thePath ){
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
     * This is the getter for document id.
     *
     * @return
     */
    public String id(){
    return id;
}

    /**
     * This is getter for document name.
     *
     * @return
     */
    public String getDocumentName(){
    return documentName;
}

    /**
     * This is the getter for file path.
     * @return
     */
//    public String getFilepath(){
//        return filepath;
//    }

    /**
     * this is the getter for document description.
     * @return
     */
    public String getDocumentDescription(){
        return documentDescription;
    }


    /**
     * This is the setter for document name.
     * @param documentName
     */
    public void setDocumentName(String documentName){
        this.documentName = documentName;
    }

    /**
     * This is the setter for file path.
     * @param filepath
     */
    public void setFilepath(String filepath){
        this.filePath = filepath;
    }

    /**
     * This is the setter for document description
     * @param documentDescription
     */
    public void setDocumentDescription(String documentDescription){
        this.documentDescription = documentDescription;
    }
    public String getId() {
        return id;
    }
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getProjectID() {
        return projectID;
    }

    /**
     * @author Tin Phu
     * @return
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
     * @author Tin Phu
     * @param writer
     * @throws IOException
     */
    @Override
    public void toJson(Writer writer) throws IOException {
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
    @Override
    public String toString(){
        return "id: " + this.id + "|documentName:"+this.documentName + "|documentDescription:" + this.documentDescription +"|projectID:" + this.projectID
                + "|userID:"+ this.userID+"|totalCost:" + this.totalCost + "|filePath:" + this.filePath + "|date:" + this.date + "\n" ;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public String getUserID() {
        return userID;
    }
}
