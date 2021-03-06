package com.example;

import java.net.UnknownHostException;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MetadataAccesor{
    private static Finals finals;
    static MongoClient mongoClient;
    static DB database;
    static DBCollection metaDataCollection;

    public static void initialize() {
        finals = new Finals();

        // mongo stuff
        try {
            mongoClient = new MongoClient(new MongoClientURI(finals.MONGO_URL()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        // create database.
        database = mongoClient.getDB(finals.MONGO_DB_NAME());
        // create collection
        metaDataCollection = database.getCollection(finals.MONGO_META_DATA_COLLECTION());
        DBObject query = new BasicDBObject();
        // read from mongo
        DBCursor cursor = metaDataCollection.find(query);
        DBObject metadata = cursor.one();
        // if no messege has been generated start id from zero
        if (metadata == null) {

            BasicDBObject newMetadata = new BasicDBObject(finals.NEXT_REPORT_ID(), 0).append(finals.NEXT_METRIC_ID(), 0)
                    .append(finals.LAST_REDIS_TIME_STAMP(), new Date(0));
            metaDataCollection.insert(newMetadata);
        }
        // if some messages have been generated start id by the last id
    }

    public static Date getLastRedisTime() {
        DBObject metadata = getMetadataFile();
        return (Date) metadata.get(finals.LAST_REDIS_TIME_STAMP());
    }

    public static void setLastRedisTime(Date lastRedisTime) {
        DBObject metadata = getMetadataFile();
        BasicDBObject editLastTime = new BasicDBObject();
        editLastTime.append("$set", new BasicDBObject(finals.LAST_REDIS_TIME_STAMP(), lastRedisTime));
        metaDataCollection.update(metadata, editLastTime);
    }

    public static int getNextReportId() {
        DBObject metadata = getMetadataFile();
        return (Integer) metadata.get(finals.NEXT_REPORT_ID());
    }

    public static void setNextReportId(int nextReportId) {
        DBObject metadata = getMetadataFile();
        BasicDBObject editLastReportId = new BasicDBObject();
        editLastReportId.append("$set", new BasicDBObject(finals.NEXT_REPORT_ID(), nextReportId));
        metaDataCollection.update(metadata, editLastReportId);
    }

    public static int getNextMetricID() {
        DBObject metadata = getMetadataFile();
        return (Integer) metadata.get(finals.NEXT_METRIC_ID());
    }

    public static void setNextMetricID(int nextMetricID) {
        DBObject metadata = getMetadataFile();
        BasicDBObject editMetricId = new BasicDBObject();
        editMetricId.append("$set", new BasicDBObject(finals.NEXT_METRIC_ID(), nextMetricID));
        metaDataCollection.update(metadata, editMetricId);
    }

    private static DBObject getMetadataFile() {
        // empty wuery - get first
        DBObject query = new BasicDBObject();
        // read from mongo
        DBCursor cursor = metaDataCollection.find(query);
        return cursor.one();
    }
}