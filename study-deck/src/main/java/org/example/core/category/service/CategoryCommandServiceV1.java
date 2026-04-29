package org.example.core.category.service;

import org.example.core.category.domain.Category;
import org.example.core.category.factory.CategoryFactory;
import org.example.core.category.model.CreateCategory;
import org.example.core.category.port.in.CategoryCommandPort;
import org.example.core.category.port.out.CategoryStore;

public class CategoryCommandServiceV1 implements CategoryCommandPort {
    private final CategoryStore store;
    private final CategoryFactory categoryFactory;

    public CategoryCommandServiceV1(CategoryStore store, CategoryFactory categoryFactory) {
        this.store = store;
        this.categoryFactory = categoryFactory;
    }

    @Override
    public Long create(CreateCategory model) {
        Category category = categoryFactory.create(model.ownerId(), model.name(), model.sortKey());
        Category save = store.save(category);

        return save.getId();
    }
}
