package org.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzerImpl implements IRetryAnalyzer {
    private int count = 0;
    private static int maxRetry = 3;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            if (count < maxRetry) {
                count++;
                iTestResult.setStatus(ITestResult.FAILURE);
                return true;
            } else {
                iTestResult.setStatus(ITestResult.FAILURE);
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS);
        }
        return false;
    }
}


//build.gradle
//        implementation 'io.opentracing.contrib:opentracing-spring-cloud:0.1.15'
//        implementation 'datadog:dd-trace-api:0.76.0'
//        implementation 'datadog:dd-java-agent:0.76.0'

// ```
//     -javaagent:/path/to/dd-java-agent.jar
//     -Ddd.service.name=your-service-name
//     -Ddd.agent.host=localhost
//     -Ddd.agent.port=8126
//     -Ddd.api.key=your-api-key
