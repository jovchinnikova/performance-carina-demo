package com.performance.demo.performance.ios.pojo.system;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.performance.demo.performance.ios.pojo.EventType;

import java.time.Instant;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({ @JsonSubTypes.Type(value = ConnectionUpdateEvent.class, name = "ConnectionUpdateEvent"),
        @JsonSubTypes.Type(value = ConnectionDetectionEvent.class, name = "ConnectionDetectionEvent"),
        @JsonSubTypes.Type(value = InterfaceDetectionEvent.class, name = "InterfaceDetectionEvent")})
public interface Event {

    void convertTime();

    void setEventType(EventType eventType);

    Instant getTime();

}
