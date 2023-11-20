package book.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.constants.FileNameConstants;
import org.core.BaseTest;
import org.hamcrest.Matchers;
import org.pojo.Booking;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class Put_Api_Request extends BaseTest {

    @Test
    public void createAndUpdateBook() throws IOException {
        String tokenFile = FileUtils.readFileToString(new File(FileNameConstants.TOKEN_REQUEST_BODY), "UTF-8");
        String putRequestBody = FileUtils.readFileToString(new File(FileNameConstants.UPDATE_REQUEST_BODY), "UTF-8");
        String createRequestBody = FileUtils.readFileToString(new File(FileNameConstants.POST_REQUEST_BODY), "UTF-8");

        Response response = given()
                .contentType(ContentType.JSON)
                .basePath("booking")
                .body(createRequestBody)
                .when()
                .post().then().assertThat().statusCode(200)
                .body("booking.totalprice", Matchers.equalTo(1000))
                .extract().response();

        JsonPath jsonPathEvaluator = response.jsonPath();
        Assert.assertEquals(jsonPathEvaluator.get("booking.depositpaid"), true);

        String bookingId = jsonPathEvaluator.getString("bookingid");

        // Generate token
        Response tokenResponse = given()
                .basePath("auth")
                .contentType(ContentType.JSON)
                .body(tokenFile)
                .when()
                .post().then().assertThat().statusCode(200)
                .and().extract().response();

        String tokenId = tokenResponse.jsonPath().get("token");
        Assert.assertFalse(tokenId.isEmpty());

        // Update book
        Response updateResponse = given()
                .header("Content-Type","application/json")
                .header("Cookie","token="+tokenId)
                .body(putRequestBody)
                .when()
                .put("/booking/" + bookingId)
                .then().assertThat().statusCode(200)
                .body("totalprice", Matchers.equalTo(111))
                .and().extract().response();

        JsonPath updatedJson = updateResponse.jsonPath();
        System.out.println(updatedJson);
    }
}
