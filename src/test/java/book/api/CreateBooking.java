package book.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.constants.FileNameConstants;
import org.hamcrest.Matchers;
import org.pojo.Booking;
import org.pojo.BookingDates;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.core.BaseTest;

import java.io.File;
import java.io.IOException;

public class CreateBooking extends BaseTest{

    @Test
    public void createBookings() {

        BookingDates bookingdates = new BookingDates("2023-11-14", "2023-11-15");
        Booking booking = new Booking("test","user", 500, true, bookingdates, "");

        Response response = RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com/booking")
                .header("Content-Type", "application/json")
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
    }

    @Test
    public void jsonSchemaValidation() throws IOException {
        String jsonSchemaPath = FileUtils.readFileToString(new File(FileNameConstants.JSON_SCHEMA), "UTF-8");

        BookingDates bookingdates = new BookingDates("2023-11-14", "2023-11-15");
        Booking booking = new Booking("test","user", 500, true, bookingdates, "");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(booking)
                .baseUri("https://restful-booker.herokuapp.com/booking")
                .when().post()
                .then()
                .assertThat().statusCode(200)

                .body(JsonSchemaValidator.matchesJsonSchema(jsonSchemaPath))

                .extract().response();
    }
}
