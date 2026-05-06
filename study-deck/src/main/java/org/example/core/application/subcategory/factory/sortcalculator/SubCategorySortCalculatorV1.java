package org.example.core.application.subcategory.factory.sortcalculator;

import org.example.core.domain.subcategory.SubCategory;
import org.example.core.domain.subcategory.SubCategoryStore;

import java.util.List;
import java.util.Objects;

public class SubCategorySortCalculatorV1 implements SubCategorySortCalculator{

    private final SubCategoryStore store;
    private static final int AMOUNT = 1000;

    public SubCategorySortCalculatorV1(SubCategoryStore store) {
        this.store = store;
    }

    @Override
    public int newSortKey(Long parentCategory, Integer hopeSortKey) {
        List<Integer> sortKeys = store.findByParentCategoryId(parentCategory)
                .stream()
                .map(SubCategory::getSortKey)
                .sorted(Integer::compareTo)
                .toList();

        if (Objects.isNull(hopeSortKey)) {
            return sortKeys.stream().max(Integer::compareTo).orElse(0) + AMOUNT;
        }

        if (sortKeys.isEmpty() || !sortKeys.contains(hopeSortKey)) {
            return hopeSortKey;
        }

        int sameValueIdx = sortKeys.indexOf(hopeSortKey);
        Integer nextValue = sortKeys.get(sameValueIdx + 1);
        if (Objects.equals(hopeSortKey, nextValue)) {
            while (sortKeys.contains(hopeSortKey)) {
                hopeSortKey += AMOUNT;
            }
            return hopeSortKey;
        }

        return (hopeSortKey + nextValue) / 2;
    }
}
