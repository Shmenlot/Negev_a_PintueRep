package com.example;


import java.net.UnknownHostException;

import com.mongodb.*;


public class MongoDB {
    public static MongoClient mongoClient;
    public static DB database;
    public static DBCollection test;
    public static void main(String[] args) throws UnknownHostException {
        // establish conection with database.
        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        // create database.
        database = mongoClient.getDB("FirstDB");
        // create collection
        test = database.getCollection("test");
        TestObject testObj = new TestObject("69", 43, 12);
        // insert an object into the test collection.
        test.insert(convert(testObj));

        // find object by the amount of xp.
        DBObject query = new BasicDBObject("XP", 69);
        DBCursor cursor = test.find(query);
        System.out.println(cursor.one());

    }

    public static DBObject convert(TestObject testObj){
        return new BasicDBObject("XP", testObj.getXp()).append("Timer", testObj.getTimer()).append("MemberID", testObj.getMemberID());
    }
    
}