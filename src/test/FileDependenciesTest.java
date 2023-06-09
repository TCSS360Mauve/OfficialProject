package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
/**
 * Tests for File Dependency.
 * @Author Tin Phu
 * @version 0.1
 */
public class FileDependenciesTest {
    String currentPath = System.getProperty("user.dir");

    /**
     * Test if AppInfo.json exists.
     */
    @Test
    public void AppInfoJsonFindTest(){
        File theDir = new File(currentPath + "\\AppInfo.json");
        if (theDir.exists()){
            Assertions.assertTrue(true);
        }else
            Assertions.fail();
    }
    /**
     * Test if UserProfile.json exists.
     */
    @Test
    public void UserProfileJsonFindTest(){
        File theDir = new File(currentPath + "\\UserProfile.json");
        if (theDir.exists()){
            Assertions.assertTrue(true);
        }else
            Assertions.fail();
    }
    /**
     * Test if ProjectList.json exists.
     */
    @Test
    public void ProjectListJsonFindTest(){
        File theDir = new File(currentPath + "\\ProjectList.json");
        if (theDir.exists()){
            Assertions.assertTrue(true);
        }else
            Assertions.fail();
    }
    /**
     * Test if DocumentList.json exists.
     */
    @Test
    public void DocumentListJsonFindTest(){
        File theDir = new File(currentPath + "\\DocumentList.json");
        if (theDir.exists()){
            Assertions.assertTrue(true);
        }else
            Assertions.fail();
    }



}
