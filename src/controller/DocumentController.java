package controller;
import java.io.File;
import java.util.HashMap;
import model.Document;
import repository.DocumentRepository;

/**
 * A controller for the list of documents in every project.
 * @author Riley Bennett
 * @author Tin Phu
 * @version 0.3
 */
public class DocumentController {

    /**
     * Document Repository, which has access to the documents in DocumentList.json.
     */
    private final static DocumentRepository documentRepository = new DocumentRepository();

    /**
     * Imports data from DocumentList.json
     * @author Riley Bennett
     */
    public static void importData() {
        documentRepository.importData();
    }

    /**
     * Exports document data to DocumentList.json
     * @author Riley Bennett
     */
    public static void exportData() {
        documentRepository.exportData();
    }

    /**
     * Get List of Doc of a specific projectID.
     * @author Tin Phu
     * @param thePID The ID of the project
     * @return A copy of HashMap of the list.
     */
    public static HashMap<String, Document> getDocsByProjectID(final String thePID){
        HashMap<String, Document> listOfDoc = new HashMap<String, Document>();
        documentRepository.getListOfDocument().forEach((k,e)->{
            if(e.getProjectID().equals(thePID)){
                listOfDoc.put(k,e);
            }
        });

        return listOfDoc;
    }

    /**
     * Adds a document to the database.
     * @author Tin Phu
     * @param theDoc The document to be added.
     */
    public static void addDocument(final Document theDoc){
        documentRepository.addDocument(theDoc);
    }

    /**
     * Deletes a document from the database.
     * @author Tin Phu
     * @param theDoc The document to be deleted.
     */
    public static void deleteADocument(final Document theDoc){
        documentRepository.deleteDocument(theDoc.getId());
        String currentPath = System.getProperty("user.dir");
        File file = new File(currentPath + theDoc.getFilePath());
        file.delete();

    }

    /**
     * Finds a document by it's ID.
     * @author Tin Phu
     * @param theID The ID of the project to be found.
     * @return The document with the given ID.
     */
    public static Document findDocbyID(final String theID){
        return documentRepository.findDocumentById(theID);
    }

    public static HashMap<String, Document> getAllDocs(){
        return documentRepository.getListOfDocument();
    }
}