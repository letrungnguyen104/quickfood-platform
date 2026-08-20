package com.quickfood.menuservice.service;

import com.quickfood.menuservice.dto.request.MenuItemRequest;
import com.quickfood.menuservice.dto.request.UpdateMenuItemRequest;
import com.quickfood.menuservice.dto.response.MenuItemResponse;
import com.quickfood.menuservice.entity.Category;
import com.quickfood.menuservice.entity.MenuItem;
import com.quickfood.menuservice.exception.BusinessException;
import com.quickfood.menuservice.exception.ErrorCode;
import com.quickfood.menuservice.mapper.MenuMapper;
import com.quickfood.menuservice.repository.CategoryRepository;
import com.quickfood.menuservice.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    private final MenuMapper menuMapper;

    @Transactional
    public MenuItemResponse addMenuItem(Long categoryId, MenuItemRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        MenuItem menuItem = menuMapper.toMenuItem(request);
        menuItem.setCategory(category);
        if (request.getIsAvailable() == null) menuItem.setAvailable(true);

        MenuItem saved = menuItemRepository.save(menuItem);
        return menuMapper.toMenuItemResponse(saved);
    }

    @Transactional
    public MenuItemResponse updateMenuItem(Long itemId, UpdateMenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MENU_ITEM_NOT_FOUND));

        menuMapper.updateMenuItemFromDto(request, menuItem);
        
        if (request.getIsAvailable() != null) {
            menuItem.setAvailable(request.getIsAvailable());
        }

        MenuItem updated = menuItemRepository.save(menuItem);
        return menuMapper.toMenuItemResponse(updated);
    }
}