package test;

import controller.DocumentController;
import model.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.BudgetTab;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is for testing method for budget tab.
 *
 * @author  Thinh Le
 * @version 0.1
 */
class BudgetTest {
 
    private BudgetTab budgetTab;

    @BeforeEach
    void setUp() {
        budgetTab = new BudgetTab("project");
    }


    @Test
    void setTotalBudget() {
        budgetTab.setTotalBudget(50000);

        assertEquals(50000, budgetTab.totalBudget);
    }


    @Test
    void updateTotalCost() {
        HashMap<String, Document> myDoc = new HashMap<>();
        myDoc.put("id-1", new Document("Document 1", "", "project-1", "", BigDecimal.valueOf(100.00)));
        budgetTab.myDoc = myDoc;

        budgetTab.updateTotalCost();

        assertEquals(100.00, budgetTab.theTotalCost);
    }

    @Test
    void documentCreationFormPopUp() {
        BudgetTab.DocumentCreationFormPopUp formPopUp = budgetTab.new DocumentCreationFormPopUp("project-1");

        assertNotNull(formPopUp.documentNameField);
        assertNotNull(formPopUp.documentDescriptionField);
        assertNotNull(formPopUp.totalCostField);
        assertNotNull(formPopUp.dialog);
    }
    @Test
    void testSetTotalBudget() {
        double newBudget = 50000;
        budgetTab.setTotalBudget(newBudget);
        double totalBudgetLabel = budgetTab.totalBudget;

        assertEquals(newBudget, budgetTab.totalBudget, 0.001);
        assertEquals( newBudget, totalBudgetLabel);
    }

    @Test
    public void DeleteDocTest() {
        Document myDoc = new Document("DocTest1","",
                "projectID","", BigDecimal.valueOf(25));
        DocumentController.deleteADocument(myDoc);
        assertNull(DocumentController.findDocbyID(myDoc.getId()));
    }
    @Test
    public void addDocTest() {
        Document myDoc = new Document("DocTest1","",
                "projectID","", BigDecimal.valueOf(25));
        DocumentController.addDocument(myDoc);
        budgetTab.myDoc.put(myDoc.id(), myDoc);
        budgetTab.addRowToTable(myDoc.id(), myDoc.getDocumentName(), myDoc.getTotalCost().toString());

        // Check that the document has been added to the BudgetTab
        assertEquals(myDoc, budgetTab.myDoc.get(myDoc.id()));
        assertEquals(1, budgetTab.model.getRowCount());
    }
}