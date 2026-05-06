package org.example.core.application.category.mapper;

import org.example.core.application.category.dto.response.CategoryCapture;
import org.example.core.domain.category.Category;

import java.util.List;

public interface DomainToModelMapper {
    CategoryCapture toCapture(Category domain);

    List<CategoryCapture> toCapture(List<Category> domain);
}
