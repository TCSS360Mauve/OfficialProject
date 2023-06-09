package test;

import controller.DocumentController;
import model.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.BudgetTab;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * This class is for testing method for budget tab.
 *
 * @author Thinh Le
 * @version 0.1
 */
class BudgetTest {
    BudgetTab budget;
    Document doc = new Document("Doc1", "Description",
            "projectID", "UserID", BigDecimal.valueOf(94));
    HashMap<String, Document> myDoc;

    @BeforeEach
    public void setUp() {

        DocumentController.addDocument(doc);
        myDoc = DocumentController.getDocsByProjectID(doc.getId());
        myDoc.put(doc.getId(), doc);

    }


    /**
     * This is to test delete Doc.
     *
     * @Author Thinh Le
     */

    @Test
    public void AddingDocByIDTest() {
        DocumentController.addDocument(doc);
        Assertions.assertNotNull(DocumentController.findDocbyID(doc.getId()));

    }
    /**
     * Test Value of Doc.
     */
    @Test
    void testValue() {


        assertEquals(BigDecimal.valueOf(94), myDoc.get(doc.getId()).getTotalCost());
    }

    /**
     * Test Name of Doc.
     */
    @Test
    void testName() {

        assertEquals("Doc1", myDoc.get(doc.getId()).getDocumentName());
    }

    /**
     * Test User id.
     */
    @Test
    void testUserId() {

        assertEquals("UserID", myDoc.get(doc.getId()).getUserID());
    }

    /**
     * Test BudgetTab before adding.
     */
    @Test
    void testBudgetTabBeforeAdding() {
        try {
            BudgetTab budget = new BudgetTab(doc.getId());
        } catch (Exception e) {
            System.out.println("Null");
        }


    }
}