
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaProducerMain {

    private static Properties prop;
    private static KafkaProducer<String, Event> producer;
    private static Logger logger;

    public static void main(String[] args) {
        boolean keepOnSending = true;
        initialize();

        while (keepOnSending) {
            
            try {
                TimeUnit.SECONDS.sleep(Config.DELAY_BETWEEN_EVENTS_CREATED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            KafkaProducerMain.sendEvent();
        }
        
        KafkaProducerMain.finishProducer();

    }

    public static void initialize() {
        // classes initialzation.
        Config.intiliaze();
        EventFactory.initialize();
        

        // create producer properties.
        prop = new Properties();
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.BOOTSTRAP_SERVER);
        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, EventSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, EventSerializer.class.getName());
        producer = new KafkaProducer<String, Event>(prop);
        
        // creating logger for sending info.
        logger = LoggerFactory.getLogger(KafkaProducerMain.class);

    }

    /**
     * send randomly generated event
     * 
     * @RUNBEFORE {@link KafkaProducerMain#initialize()}
     */
    private static void sendEvent() {

        // generate EventJSon String
        Event event = generateRandomEvent();

        // create record
        ProducerRecord<String, Event> record = new ProducerRecord<String, Event>(Config.TOPIC, event);

        // send and flush
        producer.send(record, new Callback() {
            public void onCompletion(RecordMetadata metadata, Exception e) {

                // when record sent successfully
                if (e == null) {
                    logger.info("Recived info\n" + "Topic " + metadata.topic() + "\nPartition " + metadata.partition()
                            + "\nOffset " + metadata.offset() + "\nTimestamp " + metadata.timestamp() + "\n ");

                } else { // when there was problem in sending the event.
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