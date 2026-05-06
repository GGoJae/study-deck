package org.example.core.application.subcategory.factory;

import org.example.core.domain.subcategory.SubCategory;

public interface SubCategoryFactory {
    SubCategory create(Long ownerId, Long parentCategoryId, String name, Integer hopeSortKey);
}
