package org.example.core.category.service;

import org.example.core.category.domain.Category;
import org.example.core.category.model.CreateCategory;
import org.example.core.category.port.in.CategoryCommandPort;
import org.example.core.category.port.out.CategoryStore;

public class CategoryCommandServiceV1 implements CategoryCommandPort {
    private final CategoryStore store;

    public CategoryCommandServiceV1(CategoryStore store) {
        this.store = store;
    }

    public Long create(CreateCategory model) {
        Category category = new Category();
        category.setName(model.name());
        category.setSortKey(model.sortKey());

        Category save = store.save(category);

        return save.getId();
    }
}
