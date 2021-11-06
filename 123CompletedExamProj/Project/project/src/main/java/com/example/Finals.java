package com.example;
// This interface is used to centralize the finals.
public interface Finals {
    /**
     * these are the kafka Finals
     * in it are the sever and topic names.
     */
    public final static String BOOTSTRAP_SERVER = "localhost:9092";
    public final static String TOPIC = "sussy_baka3";
    
    /**
     * these are the mongoDB Finals
     * in here we save the connection, collection and db names.
     */
    public final static String MONGO_URL = "mongodb://localhost:27017";
    public final static String MONGO_DB_NAME = "FirstDataBase";
    public final static String MONGO_EVENTS_COLLECTION = "Events";
    public final static String MONGO_META_DATA_COLLECTION = "MetaData";

    //metadata keys
    public final static String NEXT_REPORT_ID = "nextReporterID";
    public final static String NEXT_METRIC_ID = "nextMetricID";
    public final static String LAST_REDIS_TIME_STAMP = "lastRedisTimeStamp";

    //time units
    public final static int DELAY_BETWEEN_EVENTS_CREATED = 1;
    public final static int DELAY_BETWEEN_MOVING_TO_REDIS = 30;
    
    // Events keys.
    public final static String REPORTERID_ID = "reporterId";
    public final static String TIMESTAMP_ID = "timestamp";

    // redis Finals
    public final static String HOST = "localhost";
    public final static int REDIS_PORT = 6379;

} 
