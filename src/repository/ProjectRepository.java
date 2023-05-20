package repository;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import model.Project;
import model.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tin Phu
 * @version 0.1
 */
public class ProjectRepository {


    /**
     * no-arg constructor
     * which automatically importData()
     */
    public ProjectRepository(){
        importData();
    }

    /**
     * File name
     */
    private final String FILEPATH="ProjectList.json";
    /**
     * Hashmap<Srting, Project> to hold importData();
     * Keep in mind that the key id.
     */
    private final HashMap<String, Project> listOfProject = new HashMap<String, Project>();


    /**
     * Import data from FILEPATH.json and map to hashMap.
     * @author Tin Phu
     *
     */
    public void importData(){
        try (FileReader fileReader = new FileReader(FILEPATH)) {
            JsonArray objects = Jsoner.deserializeMany(fileReader);
            JsonObject o = (JsonObject) objects.get(0);
            for (Map.Entry<String, Object> entry : o.entrySet()) {
                JsonObject currentProject = (JsonObject) entry.getValue();
                BigDecimal totalCost = BigDecimal.valueOf(0);
                if(currentProject.get("totalCost") != null){
                    totalCost = BigDecimal.valueOf(Double.valueOf(currentProject.get("totalCost").toString()));
                }

                BigDecimal budget = BigDecimal.valueOf(0);
                if(currentProject.get("budget") != null){
                    budget = BigDecimal.valueOf(Double.valueOf(currentProject.get("budget").toString()));
                }

                String ownerName = "notFound";
                if(currentProject.get("ownerName") != null){
                    ownerName = currentProject.get("ownerName").toString();
                }

                LocalDate date = null;
                if(currentProject.get("date") != null){
                    date = LocalDate.parse(currentProject.get("date").toString());
                }

                String projectName="notFound";
                if(currentProject.get("projectName") != null){
                    projectName = currentProject.get("projectName").toString();
                }

                String id = "notFound";
                if(currentProject.get("id") != null){
                    id = currentProject.get("id").toString();
                }

                this.listOfProject.put(entry.getKey(), new Project(id, projectName, totalCost, budget, ownerName, date));


            }

            System.out.println("Import Data from UserProfile.json");
            System.out.println(this.listOfProject.toString());


        } catch (IOException | JsonException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * export the hashmap to FILEPATH.json file.
     * @author Tin Phu
     */
    public void exportData(){
        try (FileWriter fileWriter = new FileWriter(this.FILEPATH)) {
            Jsoner.serialize(this.listOfProject, fileWriter);
            System.out.println("Export Data to UserProfile.json");
            System.out.println(this.listOfProject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * get FILEPATH
     * @return
     */
    public String getFILEPATH() {
        return FILEPATH;
    }

    /**
     * Return the reference of the hashmap.
     * @return
     */
    public HashMap<String, Project> getListOfProject() {
        return listOfProject;
    }

    /**
     * Add a project
     * @param theProject
     */
    public void addProject( Project theProject){
        this.listOfProject.put(theProject.getId(), theProject);
        this.exportData();
    }

    /**
     * Delete Project by id
     * @param id
     */
    public void deleteProject(String id){
        this.listOfProject.remove(id);
        this.exportData();
    }

    /**
     * Delete Project by Project
     * @param theProject
     */
    public void deleteProject(Project theProject){
        this.listOfProject.remove(theProject.getId());
        this.exportData();
    }

    /**
     * findProjectbyId
     * which return the reference of the element of the hashmap.
     * @param theId
     * @return
     */
    public Project findProjectbyId(String theId){
        return this.listOfProject.get(theId);
    }

}
