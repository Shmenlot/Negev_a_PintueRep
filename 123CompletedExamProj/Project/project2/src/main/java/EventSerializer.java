
import org.apache.kafka.common.serialization.Serializer;

public class EventSerializer implements Serializer<Event> {

    @Override
    public byte[] serialize(String topic, Event data) {

        return data.toJson().getBytes();
    }
    
}
