package org.example.filestore.category.mapper;

import org.example.core.category.domain.Category;
import org.example.filestore.category.model.CategoryModel;

import java.util.List;

public class ModelToDomainMapperV1 implements ModelToDomainMapper{
    @Override
    public Category toDomain(CategoryModel model) {
        Category category = new Category();
        category.setId(model.id());
        category.setOwnerId(model.ownerId());
        category.setName(model.name());
        category.setSortKey(model.sortKey());
        category.setCreatedAt(model.createdAt());
        category.setUpdatedAt(model.updatedAt());
        category.setCreatedUser(model.createdUser());
        category.setUpdatedUser(model.updatedUser());

        return category;
    }

    @Override
    public List<Category> toDomain(List<CategoryModel> models) {
        return models.stream().map(this::toDomain).toList();
    }
}
