package com.example;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class MongoDB {
    public static MongoClient mongoClient;
    public static MongoDatabase database;
    public static MongoCollection<Document> test;
    public static void main(String[] args) {
        // establish conection with database.
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("FirstDB");
        test = database.getCollection("test");
        
    }

    public static DBObject convert(TestObject testObj){
        return new BasicDBObject("XP", testObj.getXp()).append("Timer", testObj.getTimer()).append("MemberID", testObj.getMemberID());
    }
    
}