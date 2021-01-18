package com.nahorny.testwork.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Properties {
    /**
     * this class read properties from the file
     * call: Properties.get("key")
     * looks like singleton, but real goal is to read the file once
     * so there is no need to be safe in threads
     */
    private static java.util.Properties properties = null;
    private static final String PROPERTIES_PATH = "src/main/resources/test.properties";

    public static String get(String key) {
        if (properties == null) properties = read();
        return properties.getProperty(key);
    }

    private Properties() {
    }

    private static java.util.Properties read() {
        java.util.Properties properties = null;
        try {
            properties = new java.util.Properties();
            properties.load(new InputStreamReader(new FileInputStream(new File(PROPERTIES_PATH)), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
