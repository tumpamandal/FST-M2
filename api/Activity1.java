package activities;



import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.AssertJUnit.assertEquals;

public class Activity1 {
    //set the Base URI
    final static String baseURI = "https://petstore.swagger.io/v2/pet";

    @Test (priority=1)
    public void addNewPet() {
        //write the req body
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", 77232);
        reqBody.put("name", "Riley");
        reqBody.put("status", "alive");

        //generate response and save it
        Response response = given().header("Content-Type", "application/json").
                body(reqBody).
                when().post(baseURI);

        //print the response body
        System.out.println(response.getBody().asString());

        //Assertions
        response.then().statusCode(200).body("id", equalTo(77232)).body("name", equalTo("Riley")).
                body("status", equalTo("alive"));

    }

    @Test (priority=2)
    public void getRequestWithPathParam(){
        //generate response and write assertions
        //https://petstore.swagger.io/v2/pet/{petId}
        Response response1 = given().header("Content-Type","application/json").pathParam("petId","77232").
                when().get(baseURI + "/{petId}");

        //print the response body for get operation
        System.out.println("res1=" + response1.getBody().asString());

        //Add assertions
        //int petId = response1.then().extract().parseInt(path("id"); getting type cast error as id is int and path() takes string
        String petName = response1.then().extract().path("name");
        String petStatus = response1.then().extract().path("status");

        //assertEquals(petId,77232);
        assertEquals(petName,"Riley");
        assertEquals(petStatus,"alive");
    }
    @Test (priority=3)
    public void deletePetWithPathParam(){
        Response response2 = given().header("Content-Type","application/json").pathParam("petId","77232").
                when().delete(baseURI +"/{petId}");

        System.out.println(response2.getBody().asString());

        response2.then().body("code", equalTo(200));
        response2.then().body("message", equalTo("77232"));
    }

}
