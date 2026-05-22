package org.example.core.application.category.mapper;

import org.example.core.application.category.dto.response.CategoryCapture;
import org.example.core.application.mapper.ToResponseMapper;
import org.example.core.domain.category.Category;

public class CategoryToModelMapperV1 implements ToResponseMapper<Category, CategoryCapture> {
    @Override
    public CategoryCapture toResponse(Category domain) {
        return new CategoryCapture(
                domain.getId(), domain.getOwnerId(), domain.getName(),
                domain.getSortKey(), domain.getCreatedAt(), domain.getUpdatedAt(),
                domain.getCreatedUser(), domain.getUpdatedUser()
        );
    }

}
