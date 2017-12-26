package com.pwang.serde;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author pwang on 12/26/17.
 */
@Provider
public class JsonParamConverterProvider implements ParamConverterProvider {
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {

        return new ParamConverter<T>() {
//            @SuppressWarnings("unchecked")
            @Override
            public T fromString(String value) {
                try {
                    return ObjectMappers.OBJECT_MAPPER.readValue(value, rawType);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public String toString(T bean) {
                try {
                    return ObjectMappers.OBJECT_MAPPER.writeValueAsString(bean);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return "";
                }
            }

        };
    }
}
