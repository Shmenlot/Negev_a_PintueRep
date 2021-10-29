package com.example;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


public class MongoDB {
    public static MongoClient mongoClient;
    public static MongoDatabase database;
    public static DBCollection test;
    public static void main(String[] args) {
        // establish conection with database.
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("FirstDB");
        // create collection
        test = (DBCollection) database.getCollection("test");
        TestObject testObj = new TestObject("69", 43, 12);
        test.insert(convert(testObj));
        DBObject query = new BasicDBObject();
        DBCursor cursor = test.find(query);
        System.out.println(cursor.one());

    }

    public static DBObject convert(TestObject testObj){
        return new BasicDBObject("XP", testObj.getXp()).append("Timer", testObj.getTimer()).append("MemberID", testObj.getMemberID());
    }
    
}