package repository;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import model.Document;
import model.Project;

import javax.print.Doc;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * DocumentRepository
 * @Author  Tin Phu
 * @version 0.1
 */
public class DocumentRepository {


    /**
     * no-arg constructor
     * which automatically importData()
     */
    public DocumentRepository(){
        importData();
    }
    /**
     * File name
     */
    private final String FILEPATH = "DocumentList.json";
    /**
     * Hashmap<Srting, Document> to hold importData();
     * Keep in mind that the key id.
     */
    private final  HashMap<String, Document> listOfDocument = new HashMap<String, Document>();

    public HashMap<String, Document> getListOfDocument(){
        return this.listOfDocument;
    }
    /**
     * Import data from FILEPATH.json and map to hashMap.
     * @author Tin Phu
     *
     */
    public void importData() {
        try (FileReader fileReader = new FileReader(FILEPATH)) {
            JsonArray objects = Jsoner.deserializeMany(fileReader);
            JsonObject o = (JsonObject) objects.get(0);
            for (Map.Entry<String, Object> entry : o.entrySet()) {
                JsonObject currentDoc = (JsonObject) entry.getValue();
                BigDecimal totalCost = BigDecimal.valueOf(0);
                if(currentDoc.get("totalCost") != null){
                    totalCost = BigDecimal.valueOf(Double.valueOf(currentDoc.get("totalCost").toString()));
                }

                String documentDescription = "notFound";
                if (currentDoc.get("documentDescription") != null) {
                    documentDescription = currentDoc.get("documentDescription").toString();
                }

                String projectID = "notFound";
                if (currentDoc.get("projectID") != null) {
                    projectID = currentDoc.get("projectID").toString();
                }

                LocalDate date = null;
                if (currentDoc.get("date") != null) {
                    date = LocalDate.parse(currentDoc.get("date").toString());
                }

                String userID = "notFound";
                if (currentDoc.get("userID") != null) {
                    userID = currentDoc.get("userID").toString();
                }

                String id = "notFound";
                if (currentDoc.get("id") != null) {
                    id = currentDoc.get("id").toString();
                }
                String documentName = "notFound";
                if (currentDoc.get("documentName") != null) {
                    documentName = currentDoc.get("documentName").toString();
                }

                String filePath = "not Yet";
                if (currentDoc.get("filePath") != null) {
                    filePath = currentDoc.get("filePath").toString();
                }

                Document theDoc = new Document(documentName,documentDescription,projectID, userID, totalCost, id, date, filePath );
                this.getListOfDocument().put(theDoc.getId(), theDoc);

            }
            System.out.println("Import Data from UserProfile.json");
            System.out.println(this.listOfDocument.toString());
        } catch (IOException | JsonException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * export the hashmap to FILEPATH.json file.
     * @author Tin Phu
     */
    public void exportData(){
        try (FileWriter fileWriter = new FileWriter(this.FILEPATH)) {
            Jsoner.serialize(this.listOfDocument, fileWriter);
            System.out.println("Export Data to UserProfile.json");
            System.out.println(this.listOfDocument.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * get FILEPATH
     * @return FILEPATH
     */
    public String getFILEPATH() {
        return FILEPATH;
    }

    /**
     * add Document
     * @param theDoc
     */
    public void addDocument( Document theDoc){
        this.listOfDocument.put(theDoc.getId(), theDoc);
        this.exportData();
    }

    /**
     * Delete a Document by id
     * @param id
     */
    public void deleteDocument(String id){
        this.listOfDocument.remove(id);
        this.exportData();
    }

    /**
     * delete a document by document.
     * @param theDoc
     */
    public void deleteDocument(Document theDoc){
        this.listOfDocument.remove(theDoc.getId());
        this.exportData();
    }


    /**
     * Find document by id
     * @param id
     * @return
     */
    public Document findDocumentById(String id){
        return this.listOfDocument.get(id);
    }



}
