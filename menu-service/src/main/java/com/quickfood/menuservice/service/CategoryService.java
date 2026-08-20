package com.quickfood.menuservice.service;

import com.quickfood.menuservice.dto.request.CategoryRequest;
import com.quickfood.menuservice.dto.response.CategoryResponse;
import com.quickfood.menuservice.entity.Category;
import com.quickfood.menuservice.mapper.MenuMapper;
import com.quickfood.menuservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MenuMapper menuMapper;

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = menuMapper.toCategory(request);
        if (category.getSortOrder() == null) category.setSortOrder(0);
        
        Category saved = categoryRepository.save(category);
        return menuMapper.toCategoryResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getMenuByRestaurant(Long restaurantId) {
        List<Category> categories = categoryRepository.findByRestaurantIdOrderBySortOrderAsc(restaurantId);
        return menuMapper.toCategoryResponseList(categories);
    }
}