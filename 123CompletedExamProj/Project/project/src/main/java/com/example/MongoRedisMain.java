package com.example;

import java.net.UnknownHostException;
import java.sql.Date;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoRedisMain implements Finals {

    private static MongoClient mongoClient;
    private static DB database;
    private static DBCollection metaDataCollection;
    private static DBCollection eventsCollection;

    public static void main(String[] args) {
        // Jedis j = new Jedis("localhost", 6379);
        // j.set("Poo-Poo", "Pretty flyu for a wifi");
        boolean keepOnSending = true;
        initialize();
        while (keepOnSending) {
            try {
                //read redis last time from mongo
                //empty query get first
                DBObject tempQuery = new BasicDBObject();
                // read from mongo
                DBCursor tempCursor = metaDataCollection.find(tempQuery);
                DBObject metadata = tempCursor.one();
                Date lastRedisTime = (Date)metadata.get(LAST_REDIS_TIME_STAMP);

                //read from the current time stamp
                String jso = "\"timestamp\" : {\"$gte\" : ISODate(\"2021-10-31T00:00:00Z\"), \"$lt\" : ISODate(\"2030-07-03T00:00:00Z\") }";
                ObjectMapper mapper = new ObjectMapper();
                BasicDBObject timeQuery = mapper.readValue(jso, BasicDBObject.class);
                DBCursor timeCursor = eventsCollection.find(timeQuery);
                while (tempCursor.hasNext()) {
                    
                }
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        }

    }

    public static void initialize() {
        // mongo stuff
        try {
            mongoClient = new MongoClient(new MongoClientURI(Finals.MONGO_URL));
            // create database.
            database = mongoClient.getDB(MONGO_DB_NAME);
            // create collection
            metaDataCollection = database.getCollection(MONGO_META_DATA_COLLECTION);
            eventsCollection = database.getCollection(MONGO_EVENTS_COLLECTION);
            //empty wuery - get first
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
}
