package org.example.core.category.port.out;

import org.example.core.category.domain.Category;

public interface CategoryStore {
    boolean isExistName(Long userId, String name);
    Category save(Category category);
}
