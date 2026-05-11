package org.example.core.application.subcategory.service;

import org.example.core.application.subcategory.dto.request.CreateSubCategoryRequest;
import org.example.core.application.subcategory.factory.SubCategoryFactory;
import org.example.core.application.subcategory.usecase.SubCategoryCommandUseCase;
import org.example.core.domain.category.Category;
import org.example.core.domain.category.CategoryPort;
import org.example.core.domain.subcategory.SubCategory;
import org.example.core.domain.subcategory.SubCategoryStore;

import java.util.Optional;

public class SubCategoryCommandServiceV1 implements SubCategoryCommandUseCase {

    private final CategoryPort categoryPort;
    private final SubCategoryStore store;
    private final SubCategoryFactory factory;

    public SubCategoryCommandServiceV1(CategoryPort categoryPort, SubCategoryStore store, SubCategoryFactory factory) {
        this.categoryPort = categoryPort;
        this.store = store;
        this.factory = factory;
    }

    @Override
    public Long createSubCategory(CreateSubCategoryRequest request) {
        Category category = categoryPort.getCategoryEntity(request.ownerId(), request.parentCategoryId()).orElseThrow();
        category.permissionCheck(request.ownerId());
        SubCategory subCategory = factory.create(request.ownerId(), request.parentCategoryId(), request.name(), request.hopeSortKey());
        return store.save(subCategory).getId();
    }

    @Override
    public void delete(Long requesterId, long subCategoryId) {
        SubCategory subCategory = store.findById(subCategoryId).orElseThrow();
        subCategory.permissionCheck(requesterId);
        store.delete(subCategoryId);
    }

    @Override
    public void rename(Long requesterId, long subCategoryId, String newName) {
        SubCategory subCategory = store.findById(subCategoryId).orElseThrow();
        SubCategory updated = subCategory.rename(newName, requesterId);
        store.update(updated);
    }
}
