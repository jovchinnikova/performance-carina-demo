package com.performance.demo.performance.ios.pojo;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "test_event")
public class TestEvent {

    @Column(name = "event_type")
    private EventType eventType;

    @Column(timestamp = true)
    private Instant time;

    public TestEvent(EventType eventType, Instant time) {
        this.eventType = eventType;
        this.time = time;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Instant getTime() {
        return time;
    }
}
