package org.core;

import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.StringWriter;

public class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeMethod
    public void setup() {

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        RestAssured.baseURI = "https://restful-booker.herokuapp.com/";
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws FileNotFoundException {
        if (result.getStatus() == result.FAILURE) {
            Throwable t = result.getThrowable();
            StringWriter error = new StringWriter();
            t.printStackTrace(new PrintStream(String.valueOf(error)));
            logger.info(error.toString());
        }
    }

}
