package com.example;

import java.util.Properties;
import java.util.Random;
import com.google.gson.Gson;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaProducerMain {

    final static int RAND_STR_OPT = 4;
    final static int MAX_STR_SIZE = 100;
    final static int MAX_METRIC_VAL = 1000;

    private static void sendEvent(){
        //create producer properties
        Properties prop = new Properties();
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Finals.BOOTSTRAP_SERVER);
        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        //generate EventJSon String
        Event event = generateRandomEvent();
        String jsonEvent = event.toJson();
        
        //create the producer by the properties
        KafkaProducer<String,String> producer = new KafkaProducer<String,String>(prop);
        ProducerRecord<String,String> record = new ProducerRecord<String,String>(Finals.TOPIC, jsonEvent);
    }


    /**generates random string */
    private static String generateRandomSUString() {
        Random rnd = new Random();
        int length = rnd.nextInt(MAX_STR_SIZE) + 1;
        String str = "";
        for (int i = 0; i < length; i++) {
            switch (rnd.nextInt(RAND_STR_OPT)) {
                case 0:
                    str += " ";
                    break;
                case 1:
                    str += "ඞ";
                case 2:
                    str += "🗡";
                case 3:
                    str += "🔫";
                default:
                    break;
            }
        }
        return str;
    }
    private static int generateRandMetricVal(){
        Random rnd = new Random();
        return rnd.nextInt(MAX_METRIC_VAL) +1;
        
    }
    
    /**
     * generates random event
     * @return the random event that has been generated
     */
    private static Event generateRandomEvent(){
        return Event.create(() -> generateRandomSUString(), () -> MAX_METRIC_VAL);
    }
}