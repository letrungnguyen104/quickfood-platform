package com.quickfood.menuservice.service;

import com.quickfood.menuservice.dto.request.CreateCategoryRequest;
import com.quickfood.menuservice.dto.request.UpdateCategoryRequest;
import com.quickfood.menuservice.dto.response.CategoryResponse;
import com.quickfood.menuservice.entity.Category;
import com.quickfood.menuservice.exception.BusinessException;
import com.quickfood.menuservice.exception.ErrorCode;
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
    public CategoryResponse createCategory(CreateCategoryRequest request) {
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

    @Transactional
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        
        menuMapper.updateCategoryFromDto(request, category);
        
        return menuMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        categoryRepository.deleteById(id);
    }
}