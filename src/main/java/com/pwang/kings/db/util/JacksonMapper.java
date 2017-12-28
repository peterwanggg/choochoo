package com.pwang.kings.db.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwang.kings.serde.ObjectMappers;
import org.postgresql.jdbc.PgArray;
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

            if (object instanceof PgArray) {
                object = ((PgArray) object).getArray();
            }

            resultsMap.put(columnName, object);
        }

//        mapper.readValue
        return mapper.convertValue(resultsMap, clazz);
    }


}