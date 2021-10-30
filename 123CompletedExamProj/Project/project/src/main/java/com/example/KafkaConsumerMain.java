package com.example;

import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaConsumerMain {
    private static String groupID;
    private static Logger logger;
    private static Properties properties;
    private static KafkaConsumer<String, String> consumer;
    private static boolean keepOnReading = true;
    private static MongoClient mongoClient;
    private static DB database;
    private static DBCollection test;
    public static void main(String[] args) {
        try {
            initialize();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        setKeepOnReading(true);
        while(keepOnReading){
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                logger.info("Key: " + record.key()+ "Value:" + record.value());
                logger.info("Partition: " + record.partition() + " Offset:" + record.offset());
            }            
        }

        KafkaConsumerMain.closeConsumer();
    }

    private static void initialize() throws UnknownHostException{
        // initialize variables
        logger = LoggerFactory.getLogger(KafkaConsumerMain.class.getName());
        properties = new Properties();
        groupID = "ABC";
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Finals.BOOTSTRAP_SERVER);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());//bytes to string
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupID);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer = new KafkaConsumer<String, String>(properties);
        //subscrive to our topics
        consumer.subscribe(Collections.singleton(Finals.TOPIC));

        //mongo stuff
        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        // create database.
        database = mongoClient.getDB("FirstDataBase");
        // create collection
        test = database.getCollection("Events");
      

        
    }

    public static void setKeepOnReading(boolean keepOnReading){
        KafkaConsumerMain.keepOnReading = keepOnReading;
    }

    public static boolean getKeepOnReading(){
        return KafkaConsumerMain.keepOnReading;
    }
    
    private static void closeConsumer(){
        consumer.close();
    }
}
