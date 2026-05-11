package org.example.filestore.category.mapper;

import org.example.core.domain.category.Category;
import org.example.filestore.category.model.CategoryModel;
import org.example.filestore.shared.ModelToDomainMapper;

import java.util.List;

public class ModelToDomainMapperV1 implements ModelToDomainMapper<Category, CategoryModel> {
    @Override
    public Category toDomain(CategoryModel model) {
        return new Category(
                model.id(), model.ownerId(), model.name(),
                model.sortKey(), model.createdAt(), model.updatedAt(),
                model.createdUser(), model.updatedUser()
        );
    }

    @Override
    public List<Category> toDomain(List<CategoryModel> models) {
        return models.stream().map(this::toDomain).toList();
    }
}
