//package com.pwang.kings.db.mapper;
//
//import com.pwang.kings.db.util.JacksonMapper;
//import com.pwang.kings.objects.model.Category;
//import com.pwang.kings.objects.model.ImmutableCategory;
//import org.skife.jdbi.v2.StatementContext;
//import org.skife.jdbi.v2.tweak.ResultSetMapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
///**x
// * @author pwang on 12/27/17.
// */
//public class CategoryMapper implements ResultSetMapper<Category> {
//
//    @Override
//    public Category map(int index, ResultSet r, StatementContext ctx) throws SQLException {
//        return ImmutableCategory.builder()
//                .categoryId(r.getLong("category_id"))
//                .categoryName(r.getString("category_name"))
//                .build();
//    }
//}
