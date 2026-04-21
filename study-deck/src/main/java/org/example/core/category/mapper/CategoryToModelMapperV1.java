package org.example.core.category.mapper;

import org.example.core.category.domain.Category;
import org.example.core.category.model.CategoryCapture;

import java.util.List;
import java.util.Objects;

public class CategoryToModelMapperV1 implements DomainToModelMapper{

    @Override
    public CategoryCapture toCapture(Category domain) {
        return new CategoryCapture(
                domain.getId(), domain.getOwnerId(), domain.getName(),
                domain.getSortKey(), domain.getCreatedAt(), !Objects.equals(domain.getCreatedAt(), domain.getUpdatedAt())
        );
    }

    @Override
    public List<CategoryCapture> toCapture(List<Category> domain) {
        return domain.stream().map(this::toCapture).toList();
    }
}
