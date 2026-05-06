package org.example.core.application.subcategory.factory.sortcalculator;

public interface SubCategorySortCalculator {
    int newSortKey(Long parentCategory, Integer hopeSortKey);
}
