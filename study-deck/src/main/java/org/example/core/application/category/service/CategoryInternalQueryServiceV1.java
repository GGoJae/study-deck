package org.example.core.application.category.service;

import org.example.core.domain.category.Category;
import org.example.core.domain.category.CategoryPort;
import org.example.core.domain.category.CategoryStore;

import java.util.List;
import java.util.Optional;

public class CategoryInternalQueryServiceV1 implements CategoryPort {

    private final CategoryStore store;

    public CategoryInternalQueryServiceV1(CategoryStore store) {
        this.store = store;
    }

    @Override
    public Optional<Category> getCategoryEntity(Long ownerId, Long categoryId) {
        return store.findById(categoryId)
                .filter(c -> c.isOwner(ownerId));
    }

    @Override
    public List<Category> getCategories(Long ownerId, int offset, int limit) {
        return store.findByOwnerId(ownerId, offset, limit);
    }
}
