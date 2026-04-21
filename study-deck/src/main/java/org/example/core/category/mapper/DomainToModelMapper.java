package org.example.core.category.mapper;

import org.example.core.category.domain.Category;
import org.example.core.category.model.CategoryCapture;

import java.util.List;

public interface DomainToModelMapper {
    CategoryCapture toCapture(Category domain);

    List<CategoryCapture> toCapture(List<Category> domain);
}
