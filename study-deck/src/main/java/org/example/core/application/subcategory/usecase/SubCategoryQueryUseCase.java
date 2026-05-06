package org.example.core.application.subcategory.usecase;

import org.example.core.application.subcategory.dto.response.SubCategoryCapture;

import java.util.List;
import java.util.Optional;

public interface SubCategoryQueryUseCase {
    List<SubCategoryCapture> getSubCategories(Long requesterId, Long currentCategory);

    Optional<SubCategoryCapture> getSubCategory(Long requesterId, Long subCategoryId);
}
