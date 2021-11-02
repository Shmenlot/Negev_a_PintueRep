package com.example;

public interface Finals {
    //kafka
    public final static String BOOTSTRAP_SERVER = "localhost:9092";
    public final static String TOPIC = "sussy_baka3";
    
    //mongodb
    public final static String MONGO_URL = "mongodb://localhost:27017";
    public final static String MONGO_DB_NAME = "FirstDataBase";
    public final static String MONGO_COLLECTION_NAME = "Events";
    public final static String MONGO_METADATA_NAME = "MetaData";

    //metadata keys
    public final static String NEXT_REPORT_ID = "nextReporterID";
    public final static String NEXT_METRIC_ID = "nextMetricID";
    
} 
