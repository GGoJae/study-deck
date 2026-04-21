package org.example.filestore.category.mapper;

import org.example.core.category.domain.Category;
import org.example.filestore.category.model.CategoryModel;

import java.util.List;

public interface ModelToDomainMapper {
    Category toDomain(CategoryModel model);

    List<Category> toDomain(List<CategoryModel> models);
}
