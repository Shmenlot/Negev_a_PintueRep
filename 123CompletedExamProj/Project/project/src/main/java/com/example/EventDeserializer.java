package com.example;

import org.apache.kafka.common.serialization.Deserializer;

public class EventDeserializer implements Deserializer<Event> {

    @Override
    public Event deserialize(String topic, byte[] data) {
        return EventFactory.createFromJson(new String(data));
    }
    
}
