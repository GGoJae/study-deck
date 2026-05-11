package org.example.core.application.category.usecase;

import org.example.core.application.category.dto.request.CategoryQuery;
import org.example.core.application.category.dto.response.CategoryCapture;

import java.util.List;
import java.util.Optional;

public interface CategoryQueryUseCase {
    Optional<CategoryCapture> getCategoryForDisplay(Long userId, Long categoryId);

    List<CategoryCapture> getOwnCategoriesForDisplay(CategoryQuery query);
}
