package com.pwang.kings.db.daos;

import com.pwang.kings.db.util.SqlLogger;
import com.pwang.kings.objects.model.Category;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import java.util.Optional;

/**
 * @author pwang on 12/26/17.
 */
@SqlLogger
public interface CategoryDao {

    @SingleValueResult(Category.class)
    @SqlQuery(
            "SELECT * FROM common.category WHERE category_id = :category_id"
    )
    Optional<Category> getById(@Bind("category_id") Long id);

    @GetGeneratedKeys
    @SqlUpdate(
            "INSERT INTO common.category (category_name) values (:category.categoryName)"
    )
    Long create(@BindBean("category") Category category);

}
