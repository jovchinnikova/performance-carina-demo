package com.performance.demo.performance.ios.pojo.system;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.performance.demo.performance.ios.pojo.TestEvent;

import java.time.Instant;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({ @Type(value = ConnectionUpdateEvent.class, name = "ConnectionUpdateEvent"),
        @Type(value = ConnectionDetectionEvent.class, name = "ConnectionDetectionEvent"),
        @Type(value = InterfaceDetectionEvent.class, name = "InterfaceDetectionEvent")})
public interface Event {

    void convertTime();

    void setEventType(TestEvent testEvent);

    Instant getTime();

}
