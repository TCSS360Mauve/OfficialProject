package test;

import controller.DocumentController;
import controller.ProjectController;
import model.Document;
import model.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUnit test Project Controller and Repository.
 * @author Tin Phu
 */
public class ProjectTest {
    /**
     * Test1: Create a project and project folder test.
     * Copy and Delete File Tests.
     * Test2: A new doc with AppInfo.json is created and copy to \src\test
     * Check if the file at that location exists
     * Test3: Delete the document
     * check if the file at that location still exists.
     * Unfortunately I have to  put three tests into a single @Test,
     * because I want to create, copy and delete the files immediately after this single test is finished.
     * @author Tin Phu
     */
    @Test
    public void userCreateProjectAddDocumentAndDeleteTest() throws IOException {
        Project currentP = new Project("Name", BigDecimal.ZERO, BigDecimal.ZERO,"UserID");
        ProjectController.addProject(currentP);
        //Test 1: create a project and then add it to Controller.
        //Test ProjectController.findProjectByID
        assertEquals(currentP.getId(), ProjectController.findProjectByID(currentP.getId()).getId());
        //Test 1.1: find the project folder.
        String currentPath = System.getProperty("user.dir");


        File destFile = new File(currentPath +"\\projects\\"+ currentP.getId());
            if(destFile.exists()){
                Assertions.assertTrue(true);
            } else {
                Assertions.fail();
            }
        //Test 2: Adding Document.
        String srcFile = currentPath + "\\AppInfo.json";


        Document  testDoc = new Document("DocTest1","DocTest1Drecription",
                    currentP.getId(),"UserID", BigDecimal.valueOf(111), srcFile);



        DocumentController.addDocument(testDoc);
        //Test 2.1: DocumentController.findDocbyID
        Document currentDoc = DocumentController.findDocbyID(testDoc.getId());
        assertEquals(testDoc.getId(), currentDoc.getId());
        File destFileD = new File(currentPath + testDoc.getFilePath());
        //Test 2.2: Check if AppInfo.json is copied to the right path.
        if(destFileD.exists()){
            Assertions.assertTrue(true);
        } else {
            Assertions.fail();
        }

        //Test 3: Delete doc and its file.
        //Test 3.1: Test DocumentController.deleteADocument
        DocumentController.deleteADocument(currentDoc);
        Assertions.assertNull(DocumentController.findDocbyID(currentDoc.getId()));
        //Test 3.2: Test delete doc's file.
        File destFileDD = new File(currentPath +"\\projects\\"+ currentDoc.getFilePath());
        if(!destFileDD.exists()){
            Assertions.assertTrue(true);
        } else {
            Assertions.fail();
        }
        //Test 3.3: Delete Project.
        ProjectController.deleteProjectByID(currentP.getId());
        Assertions.assertNull(ProjectController.findProjectByID(currentP.getId()));
        //Test 3.4: Delete Project Folder.
        File destFileDDD = new File(currentPath + currentP.getId());
        if(!destFileDDD.exists()){
            Assertions.assertTrue(true);
        } else {
            Assertions.fail();
        }
    }
}
