package org.example.core.category.port.in;

import org.example.core.category.dto.CategoryQuery;
import org.example.core.category.model.CategoryCapture;

import java.util.List;

public interface CategoryQueryPort {
    CategoryCapture getCategory(Long userId, Long categoryId);

    List<CategoryCapture> getOwnCategories(CategoryQuery query);
}
