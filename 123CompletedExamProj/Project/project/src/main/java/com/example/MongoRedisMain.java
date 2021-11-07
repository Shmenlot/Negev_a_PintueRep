package com.example;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

public class MongoRedisMain extends Thread implements Finals {

    private static MongoClient mongoClient;
    private static DBCollection eventsCollection;
    private static Jedis chashud;
    private static DB database;

    private static Logger logger;

    public static void main(String[] args) {
        int i = 0;
        MongoRedisMain mrm = null;
        while (true) {
            if (mrm == null || !mrm.isAlive()) {
                mrm = new MongoRedisMain();
                logger.info("Starts Iteration " + i);
                mrm.start();
            }
            try {
                TimeUnit.SECONDS.sleep(DELAY_BETWEEN_MOVING_TO_REDIS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
    }

    @Override
    public void run() {
        initialize();
        Date lastRedisTime = MetadataAccesor.getLastRedisTime();// read date from mongo
        // read from the current time stamp
        BasicDBObject timeQuery = toFromDateQuery(lastRedisTime);
        DBCursor timeCursor = eventsCollection.find(timeQuery);
        String currentTimeStamp, currentReportID;
        timeCursor.sort(new BasicDBObject(TIMESTAMP_ID, 1));
        while (timeCursor.hasNext()) {
            DBObject currentEvent = timeCursor.next();
            currentReportID = Integer.toString((Integer) currentEvent.get(REPORTID_ID));
            currentTimeStamp = ((Date) timeCursor.one().get(TIMESTAMP_ID)).toInstant().toString();
            chashud.set(currentReportID + ":" + currentTimeStamp, currentEvent.toString());

            // send to redis and then update date for avoiding losing masseges (worst case
            // ovveride himself)
            logger.info("Recived masage from mongo and sends to redis by]\n" + "Key:" + currentReportID + ":"
                    + currentTimeStamp + "\nValue:" + currentEvent.toString() + "\n");
            MetadataAccesor.setLastRedisTime((Date) (currentEvent.get(TIMESTAMP_ID)));
        }
    }

    public static void initialize() {
        try {
            // create logger
            logger = LoggerFactory.getLogger(KafkaConsumerMain.class.getName());
            // Redis stauff
            chashud = new Jedis(HOST, REDIS_PORT);
            // mongo stuff
            mongoClient = new MongoClient(new MongoClientURI(MONGO_URL));
            // create database.
            database = mongoClient.getDB(MONGO_DB_NAME);
            // create collection
            eventsCollection = database.getCollection(MONGO_EVENTS_COLLECTION);
            MetadataAccesor.initialize();
        } catch (UnknownHostException e) {

            e.printStackTrace();
        }

    }

    /**
     * transfer date to db object that represent query for finding things from the
     * query and forwoar
     * 
     * @param d
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    private static BasicDBObject toFromDateQuery(Date d) {

        return new BasicDBObject("timestamp", new BasicDBObject("$gt", d));

    }
}
