package org.example.core.category.factory;

import org.example.core.category.domain.Category;
import org.example.core.category.factory.sortcalcultor.CategorySortCalculator;

import java.time.Instant;

public class CategoryFactoryV1 implements CategoryFactory{

    private final CategorySortCalculator sortCalculator;

    public CategoryFactoryV1(CategorySortCalculator sortCalculator) {
        this.sortCalculator = sortCalculator;
    }

    public Category create(Long ownerId, String name, Integer hopeSortKey) {
        int sortKey = sortCalculator.newSortKey(ownerId, hopeSortKey);
        Instant now = Instant.now();
        return new Category(null, ownerId, name, sortKey, now, now, ownerId, ownerId);
    }

}
