package org.example.core.application.subcategory.mapper;

import org.example.core.application.subcategory.dto.response.SubCategoryCapture;
import org.example.core.domain.subcategory.SubCategory;

public class SubCategoryToModelMapper implements DomainToModelMapper {
    @Override
    public SubCategoryCapture toModel(SubCategory domain) {
        return new SubCategoryCapture(
                domain.getId(), domain.getOwnerId(), domain.getParentCategoryId(),
                domain.getName(), domain.getSortKey(), domain.getCreatedAt(),
                domain.getUpdatedAt(), domain.getCreatedUser(), domain.getUpdatedUser()
        );
    }
}
