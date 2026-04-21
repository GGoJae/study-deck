package org.example.core.category.service;

import org.example.core.category.domain.Category;
import org.example.core.category.dto.CategoryQuery;
import org.example.core.category.mapper.DomainToModelMapper;
import org.example.core.category.model.CategoryCapture;
import org.example.core.category.permission.CategoryPermissionChecker;
import org.example.core.category.port.in.CategoryQueryPort;
import org.example.core.category.port.out.CategoryStore;

import java.util.List;

public class CategoryQueryServiceV1 implements CategoryQueryPort {

    private final CategoryStore categoryStore;
    private final CategoryPermissionChecker permissionChecker;
    private final DomainToModelMapper domainToModelMapper;

    public CategoryQueryServiceV1(CategoryStore categoryStore, CategoryPermissionChecker permissionChecker, DomainToModelMapper domainToModelMapper) {
        this.categoryStore = categoryStore;
        this.permissionChecker = permissionChecker;
        this.domainToModelMapper = domainToModelMapper;
    }

    @Override
    public CategoryCapture getCategory(Long userId, Long categoryId) {
        permissionChecker.canReadThisCategory(userId, categoryId);
        Category category = categoryStore.findById(categoryId).orElseThrow();
        return domainToModelMapper.toCapture(category);
    }

    @Override
    public List<CategoryCapture> getOwnCategories(CategoryQuery query) {
        List<Category> categories = categoryStore.findByOwnerId(query.ownerId(), query.offSet(), query.limit());
        return domainToModelMapper.toCapture(categories);
    }
}
