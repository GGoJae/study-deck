package org.example.core.application.category.service;

import org.example.core.application.category.factory.CategoryFactory;
import org.example.core.domain.category.Category;
import org.example.core.application.category.dto.request.CreateCategoryRequest;
import org.example.core.application.category.usecase.CategoryCommandUseCase;
import org.example.core.domain.category.CategoryStore;

public class CategoryCommandServiceV1 implements CategoryCommandUseCase {
    private final CategoryStore store;
    private final CategoryFactory categoryFactory;

    public CategoryCommandServiceV1(CategoryStore store, CategoryFactory categoryFactory) {
        this.store = store;
        this.categoryFactory = categoryFactory;
    }

    @Override
    public Long create(CreateCategoryRequest model) {
        Category category = categoryFactory.create(model.ownerId(), model.name(), model.sortKey());
        Category save = store.save(category);

        return save.getId();
    }

    @Override
    public void rename(Long requesterId, Long categoryId, String newName) {
        Category category = store.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        Category renamed = category.rename(newName, requesterId);

        store.update(renamed);
    }

    @Override
    public void delete(Long requesterId, Long categoryId) {
        Category category = store.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        if (category.isOwner(requesterId)) {
            store.delete(categoryId);
        } else {
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }
    }
}
