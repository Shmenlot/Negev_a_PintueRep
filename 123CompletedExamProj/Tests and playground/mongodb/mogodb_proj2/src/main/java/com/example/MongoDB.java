package com.example;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoClient;
import com.mongodb.client.internal.MongoClientImpl;

public class MongoDB {
    public static MongoClient mongoClient;
    public static DB database;
    public static DBCollection test;
    public static void main(String[] args) {
        mongoClient = new MongoClien(new MongoClientURI("mongodb://localhost:27017"));
        DAT
    }
    
}