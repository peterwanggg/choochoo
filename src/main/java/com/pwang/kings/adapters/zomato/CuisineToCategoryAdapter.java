package com.pwang.kings.adapters.zomato;

import com.pwang.kings.objects.api.zomato.Cuisine;
import com.pwang.kings.objects.model.ApiProviderType;
import com.pwang.kings.objects.model.Category;
import com.pwang.kings.objects.model.CategoryType;
import com.pwang.kings.objects.model.ImmutableCategory;

/**
 * @author pwang on 12/27/17.
 */
public final class CuisineToCategoryAdapter implements ZomatoAdapter<Cuisine, Category> {
    @Override
    public Category adapt(Cuisine apiObject) {
        return ImmutableCategory.builder()
                .categoryName(apiObject.cuisine().cuisineName())
                .categoryType(CategoryType.restaurant)
                .apiProviderId(String.valueOf(apiObject.cuisine().cuisineId()))
                .apiProviderType(ApiProviderType.zomato.toString())
                .build();
    }
}
