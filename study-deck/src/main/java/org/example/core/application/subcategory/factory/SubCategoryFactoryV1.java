package org.example.core.application.subcategory.factory;

import org.example.core.application.subcategory.factory.sortcalculator.SubCategorySortCalculator;
import org.example.core.domain.subcategory.SubCategory;

import java.time.Instant;

public class SubCategoryFactoryV1 implements SubCategoryFactory{

    public SubCategoryFactoryV1(SubCategorySortCalculator sortKeyCalculator) {
        this.sortKeyCalculator = sortKeyCalculator;
    }

    private final SubCategorySortCalculator sortKeyCalculator;

    @Override
    public SubCategory create(Long ownerId, Long parentCategoryId, String name, Integer hopeSortKey) {
        int sortKey = sortKeyCalculator.newSortKey(parentCategoryId, hopeSortKey);
        Instant now = Instant.now();
        return new SubCategory(
                null, ownerId, parentCategoryId,
                name, sortKey, now, now,
                ownerId, ownerId);
    }
}
