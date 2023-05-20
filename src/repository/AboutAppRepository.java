package repository;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import model.Account;
import model.AppInfo;
import model.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author  Tin Phu
 * @version 0.1
 * AboutAppRepository reads and writes to/from AppInfo.json file
 * com.github.cliftonlabs.json_simple library is used to work with Json.
 *
 */
public class AboutAppRepository {

    /**
     * Path to the AppInfo.json
     */
     private final String FILEPATH="AppInfo.json";
    /**
     * appInfo holds data.
     */
    private final AppInfo appInfo = new AppInfo();

    /**
     * No-arg constructor.
     * which will simply importData() and populate data from json file to this.appInfo.
     */
    public AboutAppRepository()  {
        importData();
    }

    /**
     * Data is imported and then mapped to this.appInfo
     * @Author Tin Phu
     */
    public void importData() {

        try (FileReader fileReader = new FileReader(FILEPATH)) {
            JsonArray objects = Jsoner.deserializeMany(fileReader);
//            System.out.println(objects.toString());
//            System.out.println(objects);


                JsonObject o = (JsonObject) objects.get(0);
//                System.out.println(o);
                if(o.get("versionNumber") != null){
                    appInfo.setVersionNumber(Double.parseDouble(o.get("versionNumber").toString()));
                }

                //Mapping CurrentUser
                 if(o.get("user") != null){
                    JsonObject currentUserJObject =  (JsonObject) o.get("user");
                    appInfo.setUser(new User(currentUserJObject.get("name").toString(),currentUserJObject.get("email").toString()));
                }



                JsonArray  arr = (JsonArray)o.get("teamMembers");
                 if(arr != null){
                     //Mapping List of Devs information.
                     for(int i = 0; i < arr.size(); i++){
                         String currentMemberName = (String) ((JsonObject)arr.get(i)).get("name");
                         String currentMemberEmail = (String) ((JsonObject)arr.get(i)).get("email");
                         appInfo.addNewDeveloper(new Account(currentMemberName,currentMemberEmail ));
                     }
                 }



        } catch (IOException | JsonException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * exporting or serialize this.appInfo to AppInfor.json.
     * @Author Tin Phu
     */
    public void exportData(){


//        String json = Jsoner.serialize(this.appInfo);
//        json = Jsoner.prettyPrint(json);
//        System.out.println(json);   // print out JSON to check before writing to json file.
        try (FileWriter fileWriter = new FileWriter(this.FILEPATH)) {
            Jsoner.serialize(this.appInfo, fileWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * @Author Tin Phu
     * @return
     */
    public AppInfo getAppInfo() {
        return appInfo;
    }


}
