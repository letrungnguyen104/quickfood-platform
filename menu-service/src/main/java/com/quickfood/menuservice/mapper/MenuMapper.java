package com.quickfood.menuservice.mapper;

import com.quickfood.menuservice.dto.request.CategoryRequest;
import com.quickfood.menuservice.dto.request.MenuItemRequest;
import com.quickfood.menuservice.dto.request.UpdateMenuItemRequest;
import com.quickfood.menuservice.dto.response.CategoryResponse;
import com.quickfood.menuservice.dto.response.MenuItemResponse;
import com.quickfood.menuservice.entity.Category;
import com.quickfood.menuservice.entity.MenuItem;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuMapper {

    CategoryResponse toCategoryResponse(Category entity);
    List<CategoryResponse> toCategoryResponseList(List<Category> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "menuItems", ignore = true)
    Category toCategory(CategoryRequest request);

    MenuItemResponse toMenuItemResponse(MenuItem entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    MenuItem toMenuItem(MenuItemRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateMenuItemFromDto(UpdateMenuItemRequest request, @MappingTarget MenuItem entity);
}