package com.example;

import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.mongodb.BasicDBObject;
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

public class KafkaConsumerMain implements Finals{
    // kafka stuff.
    private static String groupID;
    private static Logger logger;
    private static Properties properties;
    private static KafkaConsumer<String, String> consumer;
    private static boolean keepOnReading = true;
    // mongo stuff.
    private static MongoClient mongoClient;
    private static DBCollection eventsCollection;
    private static DB database;

    public static void main(String[] args) {
        try {
            initialize();
            setKeepOnReading(true);

            while (keepOnReading) {
                // get from kafka
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    logger.info("Recived message to Kafkaconsumer\n Key: \n" + record.key() + "Value:\n" + record.value());
                    logger.info("Partition: \n" + record.partition() + " Offset:\n" + record.offset());

                    Event currEvent = EventFactory.createFromJson(record.value());
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


    /**
     * initialize the consumer
     */
    private static void initialize() throws UnknownHostException{
        // initialize kafka consumer.
        logger = LoggerFactory.getLogger(KafkaConsumerMain.class.getName());
        
        properties = new Properties();
        groupID = "ABC";
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());// bytes to string                                                                                            
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
        eventsCollection = database.getCollection(Finals.MONGO_EVENTS_COLLECTION);

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
