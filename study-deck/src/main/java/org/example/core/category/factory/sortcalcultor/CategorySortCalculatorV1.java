package org.example.core.category.factory.sortcalcultor;

import org.example.core.category.port.out.CategoryStore;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CategorySortCalculatorV1 implements CategorySortCalculator{

    private final CategoryStore store;

    private static final int AMOUNT = 1000;

    public CategorySortCalculatorV1(CategoryStore store) {
        this.store = store;
    }

    @Override
    public int newSortKey(Long ownerId, Integer hopeSortKey) {
        Objects.requireNonNull(ownerId);
        List<Integer> sortKeys = store.findAllCategorySortKey(ownerId);

        if (hopeSortKey == null) {
            return sortKeys.stream()
                    .max(Integer::compareTo)
                    .orElse(0) + AMOUNT;
        }

        if (!sortKeys.contains(hopeSortKey)) return hopeSortKey;

        Collections.sort(sortKeys);
        int sameValueIdx = sortKeys.indexOf(hopeSortKey);

        if (sameValueIdx == sortKeys.size() - 1) return hopeSortKey + AMOUNT;

        Integer nextValue = sortKeys.get(sameValueIdx + 1);

        int finalValue = (nextValue + hopeSortKey) / 2;

        while (!sortKeys.contains(finalValue)) {
            finalValue += AMOUNT;
        }

        return finalValue;
    }

}
