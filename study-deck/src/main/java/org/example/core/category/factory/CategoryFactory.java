package org.example.core.category.factory;

import org.example.core.category.domain.Category;

public interface CategoryFactory {
    Category create(Long ownerId, String name, Integer hopeSortKey);
}
