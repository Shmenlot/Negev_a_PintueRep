package com.example;

import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

// This interface is used to centralize the
public class Finals {

    public static void main(String[] args) {
        initialize();
    }
    private static final String CONSTANTS = "Constants.yml";
    private static Yaml yaml;
    /**
     * these are the kafka Finals
     * in it are the sever and topic names.
     */
    public static String BOOTSTRAP_SERVER;
    public static String TOPIC;

    /**
     * these are the mongoDB Finals in here we save the connection, collection and
     * db names.
     */
    public static String MONGO_URL;
    public static String MONGO_DB_NAME;
    public static String MONGO_EVENTS_COLLECTION;
    public static String MONGO_META_DATA_COLLECTION;

    // metadata keys
    public static String NEXT_REPORT_ID;
    public static String NEXT_METRIC_ID;
    public static String LAST_REDIS_TIME_STAMP;

    // time units
    public static int DELAY_BETWEEN_EVENTS_CREATED;
    public static int DELAY_BETWEEN_MOVING_TO_REDIS;

    // Events keys.
    public static String REPORTID_ID;
    public static String TIMESTAMP_ID;

    // redis Finals
    public static String HOST;
    public static int REDIS_PORT;

    public static void initialize() {
        yaml = new Yaml();
        InputStream inputStream = Finals.class.getClassLoader().getResourceAsStream(CONSTANTS);
        Map<String, Object> obj = yaml.load(inputStream);
        System.out.println(obj);
    }
}
