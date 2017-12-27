package com.pwang.clashbash.serde;

import com.google.maps.model.LatLng;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pwang on 12/26/17.
 */
public class LatLngConverterProvider implements ParamConverterProvider {
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {

        if (!rawType.equals(LatLng.class)) {
            return null;
        }

        return new ParamConverter<T>() {

            @Override
            public T fromString(String value) {
                try {
                    List<Double> cords = Arrays.stream(value.split(",")).map(Double::parseDouble).collect(Collectors.toList());
                    return (T) new LatLng(cords.get(0), cords.get(1));
                } catch (Exception e) {
                    throw new WebApplicationException(new IllegalArgumentException("could not parse location"),
                            Response.Status.BAD_REQUEST);
                }
            }

            @Override
            public String toString(T bean) {
                return bean.toString();
            }

        };
    }
}
