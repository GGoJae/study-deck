package org.example.core.application.category.factory.sortcalcultor;

public interface CategorySortCalculator {
    int newSortKey(Long ownerId, Integer hopeSortKey);
}
