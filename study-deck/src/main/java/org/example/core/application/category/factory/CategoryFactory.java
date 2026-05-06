package org.example.core.application.category.factory;

import org.example.core.domain.category.Category;

public interface CategoryFactory {
    Category create(Long ownerId, String name, Integer hopeSortKey);
}
