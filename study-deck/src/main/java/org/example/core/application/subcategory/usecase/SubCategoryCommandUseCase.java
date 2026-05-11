package org.example.core.application.subcategory.usecase;

import org.example.core.application.subcategory.dto.request.CreateSubCategoryRequest;

public interface SubCategoryCommandUseCase {
    Long createSubCategory(CreateSubCategoryRequest createSubCategoryRequest);

    void delete(Long requesterId, long subCategoryId);

    void rename(Long requesterId, long subCategoryId, String newName);
}
