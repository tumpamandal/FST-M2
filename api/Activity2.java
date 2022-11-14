package activities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Activity2 {
    String baseURI = "https://petstore.swagger.io/v2/user";

    @Test(priority=1)
    public void addNewUserFromExternalFile() throws IOException
    {
        // Import JSON file
        FileInputStream inputJson = new FileInputStream("src/test/java/resources/Activity2Input.json");

        // Read JSON file as String
        String reqBody = new String(inputJson.readAllBytes());
        Response response = given().contentType(ContentType.JSON)
                .body(reqBody)
                .when().post(baseURI);

        inputJson.close();
        //Assertion
        response.then().body("code",equalTo(200));
        response.then().body("message",equalTo("1146"));

    }
    @Test(priority=2)
    public void getUserAddedUsingPathParam(){
        File outputJSON = new File("src/test/java/resources/Activity2Input.json");
        Response response1 = given().contentType(ContentType.JSON).pathParam("username","tumpamndl")
                .when().get(baseURI +"/{username}");

        String resBody = response1.asPrettyString();
        try {
            // Create JSON file
            outputJSON.createNewFile();
            // Write response body to external file
            FileWriter writer = new FileWriter(outputJSON.getPath());
            writer.write(resBody);
            writer.close();
        } catch (IOException excp) {
            excp.printStackTrace();
        }

        // Assertion
        response1.then().body("id", equalTo(1146));
        response1.then().body("username", equalTo("tumpamndl"));
        response1.then().body("firstName", equalTo("tumpa"));
        response1.then().body("lastName", equalTo("mandal"));
        response1.then().body("email", equalTo("tumpa.mndl@mail.com"));
        response1.then().body("password", equalTo("password123"));
        response1.then().body("phone", equalTo("9812763450"));

        System.out.println("print reposne"+resBody);
    }
    @Test(priority=3)
    public void deleteUser() throws IOException {
        Response response =
                given().contentType(ContentType.JSON) // Set headers
                        .pathParam("username", "tumpamndl")
                        .when().delete(baseURI + "/{username}");

        // Assertion
        response.then().body("code", equalTo(200));
        response.then().body("message", equalTo("tumpamndl"));
    }
}
