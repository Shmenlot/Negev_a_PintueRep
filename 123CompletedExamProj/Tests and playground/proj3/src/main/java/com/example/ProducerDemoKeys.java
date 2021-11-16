package com.example;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemoKeys {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Logger logger = LoggerFactory.getLogger(ProducerDemoCallBack.class);
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
            String topic = "second";
            String value = "there are " + i + " imposters among us ඞ";
            String key = "id_" + Integer.toString(i);
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic,key, value);

            logger.info("key: " + key);
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    // when record successfully set or exception is thrown
                    if (e == null) {
                        logger.info("Recived info\n" + "Topic " + metadata.topic() + "\n" + "Partition "
                                + metadata.partition() + "\n" + "Offset " + metadata.offset() + "\n" + "Timestamp "
                                + metadata.timestamp() + "\n");
                    } else {
                        logger.error("Error while producing ", e);
                    }
                }
            }).get();
        }
        producer.flush();
        producer.close();
    }
}
