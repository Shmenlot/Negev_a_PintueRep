package com.example;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;


public class ProducerDemo {
    public static void main(String[] args) {
        final int NICE = 100;
        String bootstrapServer = "localhost:9092";
        //create producer properties
        Properties prop = new Properties();
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        //create the producer by the properties
        KafkaProducer<String,String> producer = new KafkaProducer<String,String>(prop);
        
        //create producer record
        ProducerRecord<String,String> record = new ProducerRecord<String,String>("second","🔌ඞ 🔫 ඞඞ🗡ඞ");
        
        //send the messege
        for (int i = 0; i < NICE; i++) {
            producer.send(record);
        }
        producer.flush();
        producer.close();
    }
}

