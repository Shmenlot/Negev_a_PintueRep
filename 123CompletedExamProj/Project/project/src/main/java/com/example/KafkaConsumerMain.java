package com.example;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.util.JSON;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonParseException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

public class KafkaConsumerMain {
    private static String groupID;
    private static Logger logger;
    private static Properties properties;
    private static KafkaConsumer<String, String> consumer;
    private static boolean keepOnReading = true;
    private static MongoClient mongoClient;
    private static DB database;
    private static DBCollection eventsCollection;

    public static void main(String[] args) {
        try {
            initialize();
            setKeepOnReading(true);

            while (keepOnReading) {
                // get from kafka
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    Gson g = new Gson();
                    logger.info("Key: " + record.key() + "Value:" + record.value());
                    logger.info("Partition: " + record.partition() + " Offset:" + record.offset());
                    Event currEvent = g.fromJson(record.value(), Event.class);
                    Map<String, Object> curEventMap = new HashMap<>();
                    curEventMap.put("reportId", currEvent.getReportId());
                    curEventMap.put("timestamp", currEvent.getTimestamp());
                    curEventMap.put("metricId", currEvent.getMetricId());
                    curEventMap.put("metricValue", currEvent.getMetricValue());
                    curEventMap.put("message", currEvent.getMessage());
                    BasicDBObject eventObj = new BasicDBObject(curEventMap);
                    eventsCollection.insert(eventObj);
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        KafkaConsumerMain.closeConsumer();
    }

    public static void test() {
        try {
            initialize();

            String jso = "\"timestamp\" : {\"$gte\" : ISODate(\"2021-10-31T00:00:00Z\"), \"$lt\" : ISODate(\"2030-07-03T00:00:00Z\") }";
            ObjectMapper mapper = new ObjectMapper();
            BasicDBObject query = mapper.readValue(jso, BasicDBObject.class);
            DBCursor cursor = eventsCollection.find(query);
            System.out.println(cursor.one());
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static void initialize() throws UnknownHostException {
        // initialize variables
        logger = LoggerFactory.getLogger(KafkaConsumerMain.class.getName());
        properties = new Properties();
        groupID = "ABC";
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Finals.BOOTSTRAP_SERVER);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());// bytes
                                                                                                                 // to
                                                                                                                 // string
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupID);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer = new KafkaConsumer<String, String>(properties);
        // subscrive to our topics
        consumer.subscribe(Collections.singleton(Finals.TOPIC));

        // mongo stuff
        mongoClient = new MongoClient(new MongoClientURI(Finals.MONGO_URL));
        // create database.
        database = mongoClient.getDB(Finals.MONGO_DB_NAME);
        // create collection
        eventsCollection = database.getCollection(Finals.MONGO_COLLECTION_NAME);

    }

    public static void setKeepOnReading(boolean keepOnReading) {
        KafkaConsumerMain.keepOnReading = keepOnReading;
    }

    public static boolean getKeepOnReading() {
        return KafkaConsumerMain.keepOnReading;
    }

    private static void closeConsumer() {
        consumer.close();
    }
}
