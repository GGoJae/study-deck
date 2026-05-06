package org.example.core.domain.subcategory;

import java.util.List;
import java.util.Optional;

public interface SubCategoryPort {
    List<SubCategory> getSubCategories(Long requesterId, Long parentCategoryId);

    Optional<SubCategory> getSubCategory(Long requesterId, Long subCategoryId);
}
