package com.example;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaProducerMain {


    private static Properties prop;
    private static KafkaProducer<String, String> producer;
    private static Logger log;
    
    public static void main(String[] args) {

        boolean keepOnSending = true;
        
        KafkaProducerMain.initialize();
        while(keepOnSending){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            KafkaProducerMain.sendEvent();
        }
        KafkaProducerMain.finishProducer();

    }
    
    public static void initialize() {
        // create producer properties
        prop = new Properties();
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Finals.BOOTSTRAP_SERVER);
        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<String, String>(prop);
        log = LoggerFactory.getLogger(KafkaProducerMain.class);
    }

    /**
     * send randomly generated event
     * 
     * @RUNBEFORE {@link KafkaProducerMain.initialize}
     */
    private static void sendEvent() {

        // generate EventJSon String
        Event event = generateRandomEvent();
        String jsonEvent = EventFactory.toJson(event);

        // create record
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(Finals.TOPIC, jsonEvent);

        // send and flush
        producer.send(record, new Callback() {
            public void onCompletion(RecordMetadata metadata, Exception e) {
                // when record sent successfully
                if (e == null) {
                    log.info("Recived info\n" + "Topic " + metadata.topic() + "\nPartition " + metadata.partition()
                            + "\nOffset " + metadata.offset() + "\nTimestamp " + metadata.timestamp() + "\n");
                //when there was problem in sending the 
                } else {
                    log.error("Error while producing ", e);
                }
            }
        });
        producer.flush();
    }

    /* close producer. */
    private static void finishProducer() {
        producer.close();
    }

    

    

    /**
     * generates random event
     *
     * @return the random event that has been generated
     */
    private static Event generateRandomEvent() {
        return EventFactory.create();
    }
}