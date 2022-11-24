package liveProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static au.com.dius.pact.consumer.dsl.Matchers.equalTo;
import static io.restassured.RestAssured.given;
import static org.testng.AssertJUnit.assertEquals;

public class GitHub_RestAssured_Project {
    // Declare request specification
    RequestSpecification requestSpec;

    // Declare response specification
    ResponseSpecification responseSpec;
    String sshKey="ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCCp8HK2/Kq22OqwOVMoiJXijKCco2mED+mT1G6WuJyTZGN4sKPx98GIQfJlBOquyKhNclhAZwTOzyPAWl1yGwnaEbC7df6Cp77RVnU3yNzm3Bpziq5YE4h2pI0P6+npHiNMsYOAPVvIUri6pIDlqrEk+kcg77ubd9mdUWEGVV8XoUDpPFYHQL8Nkwph8SsFlV56XAPOPKxEj3kIjBBkrzspmcM/RLrjlNQpudLnRSV9XHA1BoB5R7gd1F2tiElWvO3ljTLLTXu/jqKH+oV8lXovwoSZbsvTXmqxAUYugi3AIAGf4otL5qdPiM8R5yMM0oUef9twEudrVlwN5MvbWy9";
    int idsshKey;

    @BeforeClass
    public void setUpRequest() {
        // Create request specification
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAuth("token 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")
                .setBaseUri("https://api.github.com")
                .build();
    }


    @BeforeClass
    public void setUpResponse() {
// Create response specification
        responseSpec = new ResponseSpecBuilder()
                // Check status code in response
                .expectStatusCode(201)
                // Check response content type
                .expectContentType("application/json")
                // Build response specification
                .build();
    }

    @Test(priority = 1)
    public void postSshKey() {

        Map<String , Object> reqBody = new HashMap<>();
        reqBody.put("title","TestAPIKey");
        reqBody.put("key",sshKey);

        Response res=given().spec(requestSpec).log().all().body(reqBody)
                .when().request("POST","/user/keys");

        idsshKey=res.then().extract().path("id");

        assertEquals(res.statusCode(),"201");

    }
    @Test(priority = 2)
    public void getSshKey() {

        Response response=given().spec(requestSpec).when().get("/user/keys/"+idsshKey);

        response.then().log().all().statusCode(Integer.parseInt("200"));

    }
    @Test(priority = 3)
    public void deleteSshKey() {
        Response response1=given().spec(requestSpec).when().pathParam("idsshKey",idsshKey).delete("/user/keys"+idsshKey);
        assertEquals(response1.statusCode(),"204");
        Reporter.log(response1.asPrettyString());
    }






}
