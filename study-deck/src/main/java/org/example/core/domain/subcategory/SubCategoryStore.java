package org.example.core.domain.subcategory;

import java.util.List;
import java.util.Optional;

public interface SubCategoryStore {
    List<SubCategory> findByParentCategoryId(Long parentCategoryId);

    Optional<SubCategory> findById(Long subCategoryId);
}
