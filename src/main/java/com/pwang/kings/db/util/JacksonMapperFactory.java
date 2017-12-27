package com.pwang.kings.db.util;

import org.skife.jdbi.v2.BuiltInArgumentFactory;
import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class JacksonMapperFactory implements ResultSetMapperFactory {

    @Override
    public boolean accepts(Class type, StatementContext ctx) {
        if (BuiltInArgumentFactory.canAccept(type)) {
            // don't interfere with built-ins
            return false;
        }
        return true;
    }

    @Override
    public ResultSetMapper mapperFor(Class type, StatementContext ctx) {
        return new JacksonMapper(type);
    }

}