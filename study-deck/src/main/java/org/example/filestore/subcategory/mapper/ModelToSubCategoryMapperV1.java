package org.example.filestore.subcategory.mapper;

import org.example.core.domain.subcategory.SubCategory;
import org.example.filestore.shared.ModelToDomainMapper;
import org.example.filestore.subcategory.model.SubCategoryModel;

import java.util.List;

public class ModelToSubCategoryMapperV1 implements ModelToDomainMapper<SubCategory, SubCategoryModel> {
    @Override
    public SubCategory toDomain(SubCategoryModel model) {
        return new SubCategory(
                model.id(), model.ownerId(), model.parentCategoryId(),
                model.name(), model.sortKey(), model.createdAt(),
                model.updatedAt(), model.createdUser(), model.updatedUser()
        );
    }

    @Override
    public List<SubCategory> toDomain(List<SubCategoryModel> models) {
        return models.stream().map(this::toDomain).toList();
    }
}
