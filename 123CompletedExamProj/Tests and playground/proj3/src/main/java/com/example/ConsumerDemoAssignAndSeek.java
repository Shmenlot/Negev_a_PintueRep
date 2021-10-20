package com.example;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jdk.internal.org.jline.utils.Log;

public class ConsumerDemoAssignAndSeek {
    public static void main(String[] args) {
        String server = "localhost:9092";
        String groupID = "Amongus3.0";
        String topic = "second";
        Logger logger = LoggerFactory.getLogger(ConsumerDemo.class.getName());
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());// bytes
                                                                                                                 // to
                                                                                                                 // string
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // create consume
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        // assign and seek are used to replay data and fetch specific messages
        TopicPartition partitionToReadFrom = new TopicPartition(topic, 0);
        consumer.assign(Arrays.asList(partitionToReadFrom));

        long offsetToReadFrom = 15L;
        consumer.seek(partitionToReadFrom, offsetToReadFrom);

        int numberOfMessagesToRead = 5;
        boolean keepOnReading = true;
        int numberOfMessagesRead = 0;
        while (keepOnReading) {

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10));

            for (ConsumerRecord record : records) {
                logger.info("Key: " + record.key() + "Value:" + record.value());
                logger.info("Partition: " + record.partition() + " Offset:" + record.offset());
                if (numberOfMessagesRead >= numberOfMessagesToRead) {
                    keepOnReading = false;
                    break;
                }
            }
            
        }
        logger.info("The cruemates have won no imposters left ):");

    }
}
