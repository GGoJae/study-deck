package org.example.core.application.category.service;

import org.example.core.application.category.dto.request.CategoryQuery;
import org.example.core.application.category.mapper.DomainToModelMapper;
import org.example.core.application.category.dto.response.CategoryCapture;
import org.example.core.application.category.usecase.CategoryQueryUseCase;
import org.example.core.domain.category.CategoryPort;

import java.util.List;
import java.util.Optional;

public class CategoryQueryServiceV1 implements CategoryQueryUseCase {

    private final CategoryPort categoryPort;
    private final DomainToModelMapper domainToModelMapper;

    public CategoryQueryServiceV1(CategoryPort categoryPort, DomainToModelMapper domainToModelMapper) {
        this.categoryPort = categoryPort;
        this.domainToModelMapper = domainToModelMapper;
    }


    @Override
    public Optional<CategoryCapture> getCategoryForDisplay(Long userId, Long categoryId) {
        return categoryPort.getCategoryEntity(userId, categoryId)
                .map(domainToModelMapper::toCapture);
    }

    @Override
    public List<CategoryCapture> getOwnCategoriesForDisplay(CategoryQuery query) {
        return categoryPort.getCategories(query.ownerId(), query.offSet(), query.limit())
                .stream()
                .map(domainToModelMapper::toCapture)
                .toList();
    }
}
