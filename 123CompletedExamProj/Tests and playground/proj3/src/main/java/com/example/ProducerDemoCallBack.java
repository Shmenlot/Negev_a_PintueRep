package com.example;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemoCallBack {
    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger(ProducerDemoCallBack.class);
        final int NICE = 100;
        String bootstrapServer = "localhost:9092";

        // create producer properties
        Properties prop = new Properties();
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create the producer by the properties
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(prop);

        // create producer record
        for (int i = NICE; i >=0; i--) {

            ProducerRecord<String, String> record = new ProducerRecord<String, String>("second", "there are " + i + " imposters among us ඞ");

            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    // when record successfully set or exception is thrown
                    if (e == null) {
                        log.info("Recived info\n" + "Topic " + metadata.topic() + "\n" + "Partition "
                                + metadata.partition() + "\n" + "Offset " + metadata.offset() + "\n" + "Timestamp "
                                + metadata.timestamp() + "\n");
                    } else {
                        log.error("Error while producing ", e);
                    }
                }
            });
        }
        producer.flush();
        producer.close();
    }
}
