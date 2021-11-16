package com.example;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaProducerMain{

    private static Finals finals;
    private static Properties prop;
    private static KafkaProducer<String, Event> producer;
    private static Logger logger;
    
    public static void main(String[] args) {

        boolean keepOnSending = true;
        initialize();
        EventFactory.initialize();
        
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
        finals = new Finals();
        prop = new Properties();
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, finals.BOOTSTRAP_SERVER());
        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, EventSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, EventSerializer.class.getName());
        producer = new KafkaProducer<String, Event>(prop);
        logger = LoggerFactory.getLogger(KafkaProducerMain.class);
    }

    /**
     * send randomly generated event
     * 
     * @RUNBEFORE {@link KafkaProducerMain.initialize}
     */
    private static void sendEvent() {

        // generate EventJSon String
        Event event = generateRandomEvent();

        // create record
        ProducerRecord<String, Event> record = new ProducerRecord<String, Event>(finals.TOPIC(), event);

        // send and flush
        producer.send(record, new Callback() {
            public void onCompletion(RecordMetadata metadata, Exception e) {
                // when record sent successfully
                if (e == null) {
                    logger.info("Recived info\n" + "Topic " + metadata.topic() + "\nPartition " + metadata.partition()
                            + "\nOffset " + metadata.offset() + "\nTimestamp " + metadata.timestamp() + "\n ");
                //when there was problem in sending the 
                } else {
                    logger.error("Error while producing ", e);
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