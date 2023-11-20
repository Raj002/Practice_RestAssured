package book.api;

import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.constants.FileNameConstants;
import org.core.BaseTest;
import org.hamcrest.Matchers;
import org.listeners.RestAssuredListener;
import org.pojo.Booking;
import org.pojo.BookingDates;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;

@Epic("Epic-01")
@Feature("Create, update and get")
public class E2E_Tests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(E2E_Tests.class);

    @Story("Story 1")
    @Description("Create a new book")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void createBookings() {
        logger.info("Create booking API is started");

        BookingDates bookingdates = new BookingDates("2023-11-14", "2023-11-15");
        Booking booking = new Booking("test","user", 500, true, bookingdates, "");

        Response response = given().filter(new AllureRestAssured()).filter(new RestAssuredListener())
                .header("Content-Type", "application/json")
                .basePath("booking")
                .body(booking)
                .when()
                .post()
                .then()
                .assertThat().statusCode(200)
                .body("booking.totalprice", Matchers.equalTo(500))
                .body("booking.depositpaid", Matchers.not(false))
                .and()
                .extract().response();

        JsonPath jsonPathEvaluator = response.jsonPath();
        Assert.assertEquals(jsonPathEvaluator.get("booking.firstname"), "test");

        logger.info("Create booking API is ended");
    }

    @Story("Story 2")
    @Description("Json schema validation")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void jsonSchemaValidation() throws IOException {
        String jsonSchemaPath = FileUtils.readFileToString(new File(FileNameConstants.JSON_SCHEMA), "UTF-8");

        BookingDates bookingdates = new BookingDates("2023-11-14", "2023-11-15");
        Booking booking = new Booking("test","user", 500, true, bookingdates, "");

        Response response = RestAssured.given().filter(new AllureRestAssured()).filter(new RestAssuredListener())
                .contentType(ContentType.JSON)
                .body(booking)
                .basePath("booking")
                .when().post()
                .then()
                .assertThat().statusCode(200)

                .body(JsonSchemaValidator.matchesJsonSchema(jsonSchemaPath))

                .extract().response();
    }

    @Story("Story 3")
    @Description("Get all book ids")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void getAllBookIds() {
        Response response = RestAssured.given().filter(new AllureRestAssured()).filter(new RestAssuredListener())
                .basePath("booking")
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(200)
                .header("Content-Type","application/json; charset=utf-8")
                .header("Server","Cowboy")
                .extract().response();

        Assert.assertTrue(response.getBody().asString().contains("bookingid"));
    }

    @Story("Story 1")
    @Description("Create and update the book details")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void createAndUpdateBook() throws IOException {
        String tokenFile = FileUtils.readFileToString(new File(FileNameConstants.TOKEN_REQUEST_BODY), "UTF-8");
        String putRequestBody = FileUtils.readFileToString(new File(FileNameConstants.UPDATE_REQUEST_BODY), "UTF-8");
        String createRequestBody = FileUtils.readFileToString(new File(FileNameConstants.POST_REQUEST_BODY), "UTF-8");

        Response response = given().filter(new AllureRestAssured()).filter(new RestAssuredListener())
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
        Response tokenResponse = given().filter(new AllureRestAssured()).filter(new RestAssuredListener())
                .basePath("auth")
                .contentType(ContentType.JSON)
                .body(tokenFile)
                .when()
                .post().then().assertThat().statusCode(200)
                .and().extract().response();

        String tokenId = tokenResponse.jsonPath().get("token");
        Assert.assertFalse(tokenId.isEmpty());

        // Update book
        Response updateResponse = given().filter(new AllureRestAssured()).filter(new RestAssuredListener())
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
