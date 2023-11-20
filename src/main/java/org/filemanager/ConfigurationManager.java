package org.filemanager;

import org.frameworkexception.APIFrameworkException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {

    public Properties initProp() {
        Properties properties = new Properties();

        String environmentName = System.getProperty("env");
        FileInputStream fileInputStream;
        try {
            if (environmentName == null) {
                System.out.println("No environment is given. Hence running the tests in QA environment...");
                fileInputStream = new FileInputStream("./src/test/resources/config/qa.config.properties");
            } else {
                System.out.println("Running tests in " + environmentName);

                switch (environmentName.toLowerCase().trim()) {
                    case "qa" ->
                            fileInputStream = new FileInputStream("./src/test/resources/config/qa.config.properties");
                    case "dev" ->
                            fileInputStream = new FileInputStream("./src/test/resources/config/dev.config.properties");
                    case "stage" ->
                            fileInputStream = new FileInputStream("./src/test/resources/config/stage.config.properties");
                    case "prod" ->
                            fileInputStream = new FileInputStream("./src/test/resources/config/prod.config.properties");
                    default -> {
                        System.out.println("Please provide the correct environment name: " + environmentName);
                        throw new APIFrameworkException("WRONG ENVIRONMENT NAME IS GIVEN.");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
