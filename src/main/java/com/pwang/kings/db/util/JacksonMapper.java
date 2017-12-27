package com.pwang.kings.db.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.pwang.kings.serde.ObjectMappers;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JacksonMapper<T> implements ResultSetMapper<T> {
    private final Class<T> clazz;
    private final ObjectMapper mapper = ObjectMappers.DB_MAPPER;

    public JacksonMapper(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public T map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        ResultSetMetaData meta = r.getMetaData();
        int columnCount = meta.getColumnCount();
        Map<String, Object> resultsMap = new HashMap<>();
        for (int i = 1; i <= columnCount; ++i) {
            String columnName = meta.getColumnName(i);
            Object object = r.getObject(i);
            resultsMap.put(columnName, object);
        }

        return mapper.convertValue(resultsMap, clazz);
    }

}