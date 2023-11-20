package book.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.constants.FileNameConstants;
import org.core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class CreateBookingWithFileBodyRequest extends BaseTest {

    @Test
    public void createBookingWithFile() throws IOException {
        String requestBody = FileUtils.readFileToString(new File(FileNameConstants.POST_REQUEST_BODY), "UTF-8");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .baseUri("https://restful-booker.herokuapp.com/booking")
                .body(requestBody)
                .when()
                .post()
                .then()
                .log().body()
                .assertThat().statusCode(200)
                .and().extract().response();

        JsonPath jsonPath = response.jsonPath();
    }

}
