package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

// This interface is used to centralize the
public class Finals {

    private static final String CONSTANTS = ".\\src\\main\\java\\com\\example\\Constants.yml";
    private static Yaml yaml;
    /**
     * The keys of the constants's class.
     */
    private static final String KAFKAFINALS = "kafkaFinals";
    private static final String MONGODB_FINALS = "MongoDB_Finals";
    private static final String METADATAKEYS = "MetaDataKeys";
    private static final String TIMEUNITS = "TimeUnits";
    private static final String EVENTSKEYS = "EventsKeys";
    private static final String REDISFINALS = "RedisFinals";

    private String _BOOTSTRAP_SERVER;
    private String _TOPIC;

    /**
     * these are the mongoDB Finals in here we save the connection, collection and
     * db names.
     */
    private String _MONGO_URL;
    private String _MONGO_DB_NAME;
    private String _MONGO_EVENTS_COLLECTION;
    private String _MONGO_META_DATA_COLLECTION;

    // metadata keys
    private String _NEXT_REPORT_ID;
    private String _NEXT_METRIC_ID;
    private String _LAST_REDIS_TIME_STAMP;

    // time units
    private int _DELAY_BETWEEN_EVENTS_CREATED;
    private int _DELAY_BETWEEN_MOVING_TO_REDIS;

    // Events keys.
    private String _REPORTID_ID;
    private String _TIMESTAMP_ID;

    // redis Finals
    private String _HOST;
    private int _REDIS_PORT;

    public Finals() {
        try {
            // load constants as yaml
            yaml = new Yaml();
            Map<String, Object> finalsMap;
            finalsMap = yaml.load(new FileReader(new File(CONSTANTS)));

            // divide to subMaps
            Map<String, Object> kafkaMapFinals = (Map) finalsMap.get(KAFKAFINALS);
            Map<String, Object> mongoMapFinals = (Map) finalsMap.get(MONGODB_FINALS);
            Map<String, Object> metaDataKeys = (Map) finalsMap.get(METADATAKEYS);
            Map<String, Object> timeUnitesMapFinals = (Map) finalsMap.get(TIMEUNITS);
            Map<String, Object> eventsKeysMap = (Map) finalsMap.get(EVENTSKEYS);
            Map<String, Object> redisMapFinals = (Map) finalsMap.get(REDISFINALS);

            // read all finals

            // kafka finals
            _BOOTSTRAP_SERVER = (String) kafkaMapFinals.get("BOOTSTRAP_SERVER");
            _TOPIC = (String) kafkaMapFinals.get("TOPIC");

            // mongodb finals
            _MONGO_URL = (String) mongoMapFinals.get("MONGO_URL");
            _MONGO_DB_NAME = (String) mongoMapFinals.get("MONGO_DB_NAME");
            _MONGO_EVENTS_COLLECTION = (String) mongoMapFinals.get("MONGO_EVENTS_COLLECTION");
            _MONGO_META_DATA_COLLECTION = (String) mongoMapFinals.get("MONGO_META_DATA_COLLECTION");

            // metadata keys
            _NEXT_REPORT_ID = (String) metaDataKeys.get("NEXT_REPORT_ID");
            _NEXT_METRIC_ID = (String) metaDataKeys.get("NEXT_METRIC_ID");
            _LAST_REDIS_TIME_STAMP = (String) metaDataKeys.get("LAST_REDIS_TIME_STAMP");

            // time units
            _DELAY_BETWEEN_EVENTS_CREATED = (Integer) metaDataKeys.get("DELAY_BETWEEN_EVENTS_CREATED");
            _DELAY_BETWEEN_MOVING_TO_REDIS = (Integer) timeUnitesMapFinals.get("DELAY_BETWEEN_MOVING_TO_REDIS");

            // event keys
            _REPORTID_ID = (String) eventsKeysMap.get("REPORTID_ID");
            _TIMESTAMP_ID = (String) eventsKeysMap.get("TIMESTAMP_ID");

            // redis finals
            _HOST = (String) redisMapFinals.get("HOST");
            _REDIS_PORT = (Integer) redisMapFinals.get("REDIS_PORT");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String BOOTSTRAP_SERVER() {
        return this._BOOTSTRAP_SERVER;
    }

    public String TOPIC() {
        return this._TOPIC;
    }

    public String MONGO_URL() {
        return this._MONGO_URL;
    }

    public String MONGO_DB_NAME() {
        return this._MONGO_DB_NAME;
    }

    public String MONGO_EVENTS_COLLECTION() {
        return this._MONGO_EVENTS_COLLECTION;
    }

    public String MONGO_META_DATA_COLLECTION() {
        return this._MONGO_META_DATA_COLLECTION;
    }

    public String NEXT_REPORT_ID() {
        return this._NEXT_REPORT_ID;
    }

    public String NEXT_METRIC_ID() {
        return this._NEXT_METRIC_ID;
    }

    public String LAST_REDIS_TIME_STAMP() {
        return this._LAST_REDIS_TIME_STAMP;
    }

    public int DELAY_BETWEEN_EVENTS_CREATED() {
        return this._DELAY_BETWEEN_EVENTS_CREATED;
    }

    public int DELAY_BETWEEN_MOVING_TO_REDIS() {
        return this._DELAY_BETWEEN_MOVING_TO_REDIS;
    }

    public String REPORTID_ID() {
        return this._REPORTID_ID;
    }

    public String TIMESTAMP_ID() {
        return this._TIMESTAMP_ID;
    }

    public String HOST() {
        return this._HOST;
    }

    public int REDIS_PORT() {
        return this._REDIS_PORT;
    }

}
