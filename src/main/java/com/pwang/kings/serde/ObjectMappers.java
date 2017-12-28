package com.pwang.kings.serde;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

/**
 * @author pwang on 12/26/17.
 */
public interface ObjectMappers {
    //    ObjectMapper RETROFIT_MAPPER = new ObjectMapper();
    ObjectMapper RETROFIT_MAPPER = (new ObjectMapper()).registerModule(new GuavaModule())
            .registerModule(new Jdk8Module().configureAbsentsAsNulls(true))
            .registerModule(new AfterburnerModule())
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            .disable(DeserializationFeature.WRAP_EXCEPTIONS)
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
//            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
//            .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);


    ObjectMapper DB_MAPPER = (new ObjectMapper()).registerModule(new GuavaModule())
            .registerModule(new Jdk8Module().configureAbsentsAsNulls(true))
            .registerModule(new AfterburnerModule())
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            .disable(DeserializationFeature.WRAP_EXCEPTIONS)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
}
