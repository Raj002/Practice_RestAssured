package org.core;

import java.util.HashMap;
import java.util.Map;

public class TestContext {
    private static Map<String, Object> testContext = new HashMap<>();

    public static void setContext(String key, Object value){
        testContext.put(key, value);
    }

    public static Object getContext(String key){
        return testContext.get(key);
    }

    public static void clearMap(){
        testContext.clear();
    }

    public static Boolean isContains(String key){
        return testContext.containsKey(key);
    }
}
