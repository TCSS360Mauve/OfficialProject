package test;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;
import com.github.cliftonlabs.json_simple.Jsoner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import controller.AppInfoController;
import model.User;
import model.AppInfo;
import model.Account;

/**
 * @author Riley Bennett
 */
public class ExportTests {
    
    User user = new User("Test User", "Test@gmail.com");
    AppInfo info = new AppInfo(0.2, user);

    @BeforeEach
    void setUp() {
        info.addNewDeveloper(new Account("John Doe", "jdoe@yahoo.com"));
        info.addNewDeveloper(new Account("Jane Doe", "jadoe@yahoo.com"));
        AppInfoController.setUser(user);    // Calls export() from repository
    }

    /**
     * Test method for model.User
     */
    @Test
    public void testEmail() {
        assertEquals("Test@gmail.com", user.getEmail());
    }

    /**
     * Test method for model.User
     */
    @Test
    public void testName() {
        assertEquals("Test User", user.getName());
    }

    /**
     * Test method for model.AppInfo
     */
    @Test
    public void testCorrectUser() {
        assertEquals(user, info.getUser());
    }

    /**
     * Test method for model.AppInfo
     */
    @Test
    public void testAddDeveloper() {
        assertEquals(2, info.getDevelopers().size());
    }

    /**
     * Test method for model.AppInfo
     */
    @Test
    public void testRemoveDeveloper() {
        info.removeDevByName("John Doe");
        assertEquals("Jane Doe", info.getDevelopers().get(0).getName());
    }

    /**
     * Test method for model.AppInfo
     */
    @Test
    public void testRemoveDeveloperDNE() {
        info.removeDevByName("Jeff Doe");
        assertEquals(2, info.getDevelopers().size());
    }

    /**
     * Test method for controller.AppInfoController
     */
    @Test
    public void testExport() {
        assertEquals("Test User", AppInfoController.getAppInfo().getUser().getName());
    }

    /**
     * Test method for controller.AppInfoController
     */
    @Test
    public void testImport() {
        info.setUser(new User("Test 2 User", "test2@gmail.com"));
        try (FileWriter fileWriter = new FileWriter("AppInfo.json")) {    // Change to direct file path to get working
            Jsoner.serialize(info, fileWriter);
        } catch (IOException e) {}
        AppInfoController.importData();
        assertEquals("Test 2 User", AppInfoController.getAppInfo().getUser().getName());
    }
}
