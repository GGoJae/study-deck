package org.example.core.category.factory.sortcalcultor;

public interface CategorySortCalculator {
    int newSortKey(Long ownerId, Integer hopeSortKey);
}
