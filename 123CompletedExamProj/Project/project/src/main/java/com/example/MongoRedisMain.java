package com.example;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import redis.clients.jedis.Jedis;

public class MongoRedisMain extends Thread implements Finals {

    private static MongoClient mongoClient;
    private static DB database;
    private static DBCollection metaDataCollection;
    private static DBCollection eventsCollection;
    private static Jedis chashud;

    public static void main(String[] args) {
        MongoRedisMain mrm = new MongoRedisMain();
        while (true) {
            mrm.start();
            while (mrm.isAlive()) {  
            }
            try {
                TimeUnit.SECONDS.sleep(DELAY_BETWEEN_MOVING_TO_REDIS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void run() {
        // Jedis j = new Jedis("localhost", 6379);
        // j.set("Poo-Poo", "Pretty flyu for a wifi");

        initialize();

        // read "redis lastest timestamp" mongo metadata collection.
        // empty query get first
        DBObject tempQuery = new BasicDBObject();

        DBCursor tempCursor = metaDataCollection.find(tempQuery);
        DBObject metadata = tempCursor.one();
        Date lastRedisTime = (Date) metadata.get(LAST_REDIS_TIME_STAMP);// read from mongo

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
            System.out.println(currentReportID + ":" + currentTimeStamp);
            System.out.println(currentEvent.toString());
            System.out.println();
            BasicDBObject setLastRedisTime = new BasicDBObject();
            setLastRedisTime.append("$set", new BasicDBObject(LAST_REDIS_TIME_STAMP, currentEvent.get(TIMESTAMP_ID)));
            metaDataCollection.update(new BasicDBObject(), setLastRedisTime);

        }

    }

    public static void initialize() {
        // Redis stauff
        chashud = new Jedis(HOST, REDIS_PORT);
        // mongo stuff
        try {
            mongoClient = new MongoClient(new MongoClientURI(Finals.MONGO_URL));
            // create database.
            database = mongoClient.getDB(MONGO_DB_NAME);
            // create collection
            metaDataCollection = database.getCollection(MONGO_META_DATA_COLLECTION);
            eventsCollection = database.getCollection(MONGO_EVENTS_COLLECTION);
            // empty wuery - get first
            DBObject query = new BasicDBObject();
            // read from mongo
            DBCursor cursor = metaDataCollection.find(query);
            DBObject metadata = cursor.one();
            // if metadata wasent created there is error
            if (metadata == null) {
                System.out.println("Error: MetaDataHasentBeenInitilaized");
            }
            // if metadata dosent contain time field add one
            else if (!metadata.containsField(LAST_REDIS_TIME_STAMP)) {
                BasicDBObject addTimeField = new BasicDBObject();
                addTimeField.append("$set", new BasicDBObject(LAST_REDIS_TIME_STAMP, new Date(0)));
                metaDataCollection.update(query, addTimeField);
            }

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

    public static void test() {
        initialize();
        System.out.println(toFromDateQuery(new Date(1636040482000L)).toString());
        DBCursor cursor = eventsCollection.find(toFromDateQuery(new Date(1636040482000L)));

        System.out.println(cursor.one());
    }
}