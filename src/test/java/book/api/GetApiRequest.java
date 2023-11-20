package book.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;


public class GetApiRequest {

    private static final Logger logger = LogManager.getLogger(GetApiRequest.class);
    @Test
    public void getAllBookIds() {
        logger.info("Test execution is started...");
        Response response = RestAssured.given().baseUri("https://restful-booker.herokuapp.com/booking")
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(200)
                .header("Content-Type","application/json; charset=utf-8")
                .header("Server","Cowboy")
                .extract().response();

        Assert.assertTrue(response.getBody().asString().contains("bookingid"));
        logger.info("Test execution is ended...");
    }

}
