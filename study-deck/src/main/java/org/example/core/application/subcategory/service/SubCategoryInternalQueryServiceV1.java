package org.example.core.application.subcategory.service;

import org.example.core.domain.subcategory.SubCategory;
import org.example.core.domain.subcategory.SubCategoryPort;
import org.example.core.domain.subcategory.SubCategoryStore;

import java.util.List;
import java.util.Optional;

public class SubCategoryInternalQueryServiceV1 implements SubCategoryPort {
    private final SubCategoryStore store;

    public SubCategoryInternalQueryServiceV1(SubCategoryStore store) {
        this.store = store;
    }

    @Override
    public List<SubCategory> getSubCategories(Long requesterId, Long parentCategoryId) {
        return store.findByParentCategoryId(parentCategoryId)
                .stream()
                .filter(s -> s.isOwner(requesterId))
                .toList();
    }

    @Override
    public Optional<SubCategory> getSubCategory(Long requesterId, Long subCategoryId) {
        return store.findById(subCategoryId)
                .filter(s -> s.isOwner(requesterId));
    }
}
