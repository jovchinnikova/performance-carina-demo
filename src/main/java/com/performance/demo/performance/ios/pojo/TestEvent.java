package com.performance.demo.performance.ios.pojo;

import java.time.Instant;

public class TestEvent {

    private EventType eventType;

    private String elementName;

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
