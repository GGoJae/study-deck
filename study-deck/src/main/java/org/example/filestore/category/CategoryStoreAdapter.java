package org.example.filestore.category;

import org.example.core.category.domain.Category;
import org.example.core.category.port.out.CategoryStore;

public class CategoryStoreAdapter implements CategoryStore {
    @Override
    public boolean isExistName(Long userId, String name) {
        return false;
    }

    @Override
    public Category save(Category category) {
        return null;
    }
}
