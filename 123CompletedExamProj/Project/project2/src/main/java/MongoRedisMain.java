
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

public class MongoRedisMain extends Thread{

    private static MongoClient mongoClient;
    private static DBCollection eventsCollection;
    private static Jedis jedis;
    private static DB database;

    private static Logger logger;

    public static void main(String[] args) {
        initialize();
        int i = 0;
        MongoRedisMain mrm = null;
        while (true) {
            if (mrm == null || !mrm.isAlive()) {
                mrm = new MongoRedisMain();
                logger.info("Starts Iteration " + i);
                mrm.start();
                i++;
            }
            try {
                TimeUnit.SECONDS.sleep(Config.DELAY_BETWEEN_MOVING_TO_REDIS);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }

        }
    }

    @Override
    public void run() {
        Date lastRedisTime = MetadataAccesor.getLastRedisTime();// read date from mongo
        // read from the current time stamp
        BasicDBObject timeQuery = toFromDateQuery(lastRedisTime);
        DBCursor timeCursor = eventsCollection.find(timeQuery);
        String currentTimeStamp, currentReportID;
        timeCursor.sort(new BasicDBObject(Config.TIMESTAMP_ID, 1));
        // Insert the latest data from mongo into redis
        while (timeCursor.hasNext()) {
            // Add data by cursor to redis
            DBObject currentEvent = timeCursor.next();
            currentReportID = Integer.toString((Integer) currentEvent.get(Config.REPORTID_ID));
            currentTimeStamp = ((Date) timeCursor.one().get(Config.TIMESTAMP_ID)).toInstant().toString();
            jedis.set(currentReportID + ":" + currentTimeStamp, currentEvent.toString());

            // Send to redis and then update date for avoiding losing masseges (worst case
            // Overide himself)
            logger.info("Recived masage from mongo and sends to redis by]\n" + "Key:" + currentReportID + ":"
                    + currentTimeStamp + "\nValue:" + currentEvent + "\n");
            // Update Latest date in metadata.
            MetadataAccesor.setLastRedisTime((Date) (currentEvent.get(Config.TIMESTAMP_ID)));
        }
    }

    public static void initialize() {
        try {
            Config.intiliaze();
            // create logger
            logger = LoggerFactory.getLogger(KafkaProducerMain.class.getName());
            // Redis stauff
            jedis = new Jedis(Config.HOST, Config.REDIS_PORT);
            // mongo stuff
            mongoClient = new MongoClient(new MongoClientURI(Config.MONGO_URL));
            // create database.
            database = mongoClient.getDB(Config.MONGO_DB_NAME);
            // create collection
            eventsCollection = database.getCollection(Config.MONGO_EVENTS_COLLECTION);
            MetadataAccesor.initialize();
        } catch (UnknownHostException e) {

            e.printStackTrace();
        }

    }

    /**
     * transfer date to db object that represent query for finding things from the
     * query and forwoar
     * 
     * @param d the date
     * @return the query
     */
    private static BasicDBObject toFromDateQuery(Date d) {

        return new BasicDBObject("timestamp", new BasicDBObject("$gt", d));

    }
}
