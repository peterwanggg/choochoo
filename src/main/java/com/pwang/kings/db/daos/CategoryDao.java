package com.pwang.kings.db.daos;

import com.pwang.kings.db.util.SqlLogger;
import com.pwang.kings.objects.model.Category;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import java.util.List;
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

    @SqlQuery(
            "SELECT * from common.category WHERE location_id = :location_id AND category_type = :category_type"
    )
    List<Category> getByLocationCategoryType(@Bind("location_id") Long locationId, @Bind("category_type") String categoryType);

    @GetGeneratedKeys
    @SqlUpdate(
            "INSERT INTO common.category "
                    + "(location_id, category_name, category_type, api_provider_type, api_provider_id) VALUES "
                    + "(:category.locationId, :category.categoryName, :category.categoryType, :category.apiProviderType, :category.apiProviderId) "
                    + "ON CONFLICT ON CONSTRAINT location_category_key DO NOTHING"

    )
    Long create(@BindBean("category") Category category);

}
