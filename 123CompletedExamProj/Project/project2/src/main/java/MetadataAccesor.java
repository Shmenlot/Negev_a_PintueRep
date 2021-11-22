
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
    static MongoClient mongoClient;
    static DB database;
    static DBCollection metaDataCollection;

    public static void initialize() {
        Config.intiliaze();

        // mongo stuff
        try {
            mongoClient = new MongoClient(new MongoClientURI(Config.MONGO_URL));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        // create database.
        database = mongoClient.getDB(Config.MONGO_DB_NAME);
        // create collection
        metaDataCollection = database.getCollection(Config.MONGO_META_DATA_COLLECTION);
        DBObject query = new BasicDBObject();
        // read from mongo
        DBCursor cursor = metaDataCollection.find(query);
        DBObject metadata = cursor.one();
        // if no messege has been generated start id from zero
        if (metadata == null) {

            BasicDBObject newMetadata = new BasicDBObject(Config.NEXT_REPORT_ID, 0)
                    .append(Config.NEXT_METRIC_ID, 0)
                    .append(Config.LAST_REDIS_TIME_STAMP, new Date(0));
            metaDataCollection.insert(newMetadata);
        }
        // if some messages have been generated start id by the last id
    }

    public static Date getLastRedisTime() {
        DBObject metadata = getMetadataFile();
        return (Date) metadata.get(Config.LAST_REDIS_TIME_STAMP);
    }

    public static void setLastRedisTime(Date lastRedisTime) {
        DBObject metadata = getMetadataFile();
        BasicDBObject editLastTime = new BasicDBObject();
        editLastTime.append("$set", new BasicDBObject(Config.LAST_REDIS_TIME_STAMP, lastRedisTime));
        metaDataCollection.update(metadata, editLastTime);
    }

    public static int getNextReportId() {
        DBObject metadata = getMetadataFile();
        return (Integer) metadata.get(Config.NEXT_REPORT_ID);
    }

    public static void setNextReportId(int nextReportId) {
        DBObject metadata = getMetadataFile();
        BasicDBObject editLastReportId = new BasicDBObject();
        editLastReportId.append("$set", new BasicDBObject(Config.NEXT_REPORT_ID, nextReportId));
        metaDataCollection.update(metadata, editLastReportId);
    }

    public static int getNextMetricID() {
        DBObject metadata = getMetadataFile();
        return (Integer) metadata.get(Config.NEXT_METRIC_ID);
    }

    public static void setNextMetricID(int nextMetricID) {
        DBObject metadata = getMetadataFile();
        BasicDBObject editMetricId = new BasicDBObject();
        editMetricId.append("$set", new BasicDBObject(Config.NEXT_METRIC_ID, nextMetricID));
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