package org.example.core.application.category.usecase;

import org.example.core.application.category.dto.request.CreateCategoryRequest;

public interface CategoryCommandUseCase {
    Long create(CreateCategoryRequest model);

    void rename(Long requesterId, Long categoryId, String newName);

    void delete(Long requesterId, Long categoryId);
}
