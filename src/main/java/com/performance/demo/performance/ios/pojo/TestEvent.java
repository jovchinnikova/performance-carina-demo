package com.performance.demo.performance.ios.pojo;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "test_event")
public class TestEvent {

    @Column(name = "event_type")
    private EventType eventType;

    @Column(name = "element_name")
    private String elementName;

    @Column(timestamp = true)
    private Instant time;

    public TestEvent(EventType eventType, String elementName, Instant time) {
        this.eventType = eventType;
        this.time = time;
        this.elementName = elementName;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Instant getTime() {
        return time;
    }

    public String getElementName() {
        return elementName;
    }
}
