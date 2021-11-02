package com.example;

import java.net.UnknownHostException;
import java.sql.Date;
import java.util.concurrent.TimeUnit;

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
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
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

            DBObject query = new BasicDBObject();
            // read from mongo
            DBCursor cursor = metaDataCollection.find(query);
            DBObject metadata = cursor.one();
            if (metadata == null) {
                System.out.println("Error: MetaDataHasentBeenInitilaized");
            } else if (!metadata.containsField(LAST_REDIS_TIME_STAMP)) {
                BasicDBObject addTimeField = new BasicDBObject();

                addTimeField.append("$set", new BasicDBObject(LAST_REDIS_TIME_STAMP, new Date(0)));

                metaDataCollection.update(query, addTimeField);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
