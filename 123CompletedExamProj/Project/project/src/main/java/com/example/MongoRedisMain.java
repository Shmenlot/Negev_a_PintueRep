package com.example;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Date;
import java.util.Calendar;
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

public class MongoRedisMain implements Finals {

    private static MongoClient mongoClient;
    private static DB database;
    private static DBCollection metaDataCollection;
    private static DBCollection eventsCollection;

    public static void main(String[] args) {
        // Jedis j = new Jedis("localhost", 6379);
        // j.set("Poo-Poo", "Pretty flyu for a wifi");
        try {
            boolean keepOnSending = true;
            initialize();
            while (keepOnSending) {

                // read redis last time from mongo
                // empty query get first
                DBObject tempQuery = new BasicDBObject();
                // read from mongo
                DBCursor tempCursor = metaDataCollection.find(tempQuery);
                DBObject metadata = tempCursor.one();
                Date lastRedisTime = (Date) metadata.get(LAST_REDIS_TIME_STAMP);
                // read from the current time stamp
                
                BasicDBObject timeQuery = toFromDateQuery(lastRedisTime);
                DBCursor timeCursor = eventsCollection.find(timeQuery);
                while (timeCursor.hasNext()) {
                    
                }
                TimeUnit.SECONDS.sleep(30);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
     * transfer date to db object that represent query for finding things from the query and forwoar
     * @param d
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    private static BasicDBObject toFromDateQuery(Date d) throws JsonParseException, JsonMappingException, IOException {

        // make format match (adding zeros if nedded)
        String year = "";
        if (d.getYear() / 10 < 1) {
            year = "0";
        }
        if (d.getYear() / 100 < 1) {
            year = year + "0";
        }
        if (d.getYear() / 1000 < 1) {
            year = year + "0";
        }
        year = year + d.getYear();

        String month = (d.getMonth() / 10 < 1) ? "0" + d.getMonth() : Integer.toString(d.getMonth());
        String day = (d.getDay() / 10 < 1) ? "0" + d.getDay() : Integer.toString(d.getDay());
        String hour = (d.getMinutes() / 10 < 1) ? "0" + d.getHours() : Integer.toString(d.getHours());
        String minute = (d.getMinutes() / 10 < 1) ? "0" + d.getMinutes() : Integer.toString(d.getMinutes());
        String second = (d.getSeconds() / 10 < 1) ? "0" + d.getSeconds() : Integer.toString(d.getSeconds());
        String isoFormat =  year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":" + second + "Z";
        String strQuery = "\"timestamp\" : {\"$gte\" : ISODate(\"" + isoFormat
                        + "\"), \"$lt\" : ISODate(\"" + MAX_DATE + "\") }";
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(strQuery, BasicDBObject.class);
    }
}