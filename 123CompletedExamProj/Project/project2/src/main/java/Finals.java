
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

// This interface is used to centralize the
public class Finals {

    private static final String CONSTANTS = "./src/main/resources/Constants.yml";
    private static Yaml yaml;
    /**
     * The keys of the constants's class.
     */
    private static final String KAFKAFINALS = "KafkaFinals";
    private static final String MONGODB_FINALS = "MongoDB_Finals";
    private static final String METADATAKEYS = "MetaDataKeys";
    private static final String TIMEUNITS = "TimeUnits";
    private static final String EVENTSKEYS = "EventsKeys";
    private static final String REDISFINALS = "RedisFinals";

    public static String BOOTSTRAP_SERVER = "localhost:9092";
    public static String TOPIC = "default_topic";

    /**
     * these are the mongoDB Finals in here we save the connection, collection and
     * db names.
     */
    public static String MONGO_URL = "mongodb://localhost:27017";
    public static String MONGO_DB_NAME = "default_data_base";
    public static String MONGO_EVENTS_COLLECTION = "Events";
    public static String MONGO_META_DATA_COLLECTION = "MetaData";

    // metadata keys
    public static String NEXT_REPORT_ID = "nextReportID";
    public static String NEXT_METRIC_ID = "nextMetricID";
    public static String LAST_REDIS_TIME_STAMP = "lastRedisTimeStamp";

    // time units
    public static int DELAY_BETWEEN_EVENTS_CREATED = 1;
    public static int DELAY_BETWEEN_MOVING_TO_REDIS = 10;

    // Events keys.
    public static String REPORTID_ID = "reportId";
    public static String TIMESTAMP_ID = "timestamp";
    public static String METRICID_ID = "metricId";
    public static String METRIC_VALUE_ID = "metricValue";
    public static String MESSAGE_ID = "message";

    // redis Finals
    public static String HOST = "localhost";
    public static int REDIS_PORT = 6379;

    public static void intiliaze() {
        try {
            // load constants as yaml
            yaml = new Yaml();
            Map<String, Object> finalsMap;
            finalsMap = yaml.load(new FileReader(new File(CONSTANTS)));


            // divide to subMaps
            Map<String, Object> kafkaMapFinals = (Map<String, Object>) finalsMap.get(KAFKAFINALS);
            Map<String, Object> mongoMapFinals = (Map<String, Object>) finalsMap.get(MONGODB_FINALS);
            Map<String, Object> metaDataKeys = (Map<String, Object>) finalsMap.get(METADATAKEYS);
            Map<String, Object> timeUnitesMapFinals = (Map<String, Object>) finalsMap.get(TIMEUNITS);
            Map<String, Object> eventsKeysMap = (Map<String, Object>) finalsMap.get(EVENTSKEYS);
            Map<String, Object> redisMapFinals = (Map<String, Object>) finalsMap.get(REDISFINALS);

            // read all finals

            // kafka finals
            BOOTSTRAP_SERVER = (String) kafkaMapFinals.get("BOOTSTRAP_SERVER");
            TOPIC = (String) kafkaMapFinals.get("TOPIC");

            // mongodb finals
            MONGO_URL = (String) mongoMapFinals.get("MONGO_URL");
            MONGO_DB_NAME = (String) mongoMapFinals.get("MONGO_DB_NAME");
            MONGO_EVENTS_COLLECTION = (String) mongoMapFinals.get("MONGO_EVENTS_COLLECTION");
            MONGO_META_DATA_COLLECTION = (String) mongoMapFinals.get("MONGO_META_DATA_COLLECTION");

            // metadata keys
            NEXT_REPORT_ID = (String) metaDataKeys.get("NEXT_REPORT_ID");
            NEXT_METRIC_ID = (String) metaDataKeys.get("NEXT_METRIC_ID");
            LAST_REDIS_TIME_STAMP = (String) metaDataKeys.get("LAST_REDIS_TIME_STAMP");

            // time units
            DELAY_BETWEEN_EVENTS_CREATED = (Integer) timeUnitesMapFinals.get("DELAY_BETWEEN_EVENTS_CREATED");
            DELAY_BETWEEN_MOVING_TO_REDIS = (Integer) timeUnitesMapFinals.get("DELAY_BETWEEN_MOVING_TO_REDIS");

            // event keys
            REPORTID_ID = (String) eventsKeysMap.get("REPORTID_ID");
            TIMESTAMP_ID = (String) eventsKeysMap.get("TIMESTAMP_ID");
            METRICID_ID = (String) eventsKeysMap.get("METRICID_ID");
            METRIC_VALUE_ID = (String) eventsKeysMap.get("METRICVALUE_ID");
            MESSAGE_ID = (String) eventsKeysMap.get("MESSAGE_ID");


            // redis finals
            HOST = (String) redisMapFinals.get("HOST");
            REDIS_PORT = (Integer) redisMapFinals.get("REDIS_PORT");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



}
