package org.example.core.application.subcategory.mapper;

import org.example.core.application.subcategory.dto.response.SubCategoryCapture;
import org.example.core.domain.subcategory.SubCategory;

public interface DomainToModelMapper {
    SubCategoryCapture toModel(SubCategory domain);
}
