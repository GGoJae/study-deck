package org.example.core.application.subcategory.service;

import org.example.core.application.subcategory.mapper.DomainToModelMapper;
import org.example.core.application.subcategory.dto.response.SubCategoryCapture;
import org.example.core.application.subcategory.usecase.SubCategoryQueryUseCase;
import org.example.core.domain.subcategory.SubCategoryPort;

import java.util.List;
import java.util.Optional;

public class SubCategoryQueryServiceV1 implements SubCategoryQueryUseCase {

    private final SubCategoryPort subCategoryPort;

    private final DomainToModelMapper mapper;

    public SubCategoryQueryServiceV1(SubCategoryPort subCategoryPort, DomainToModelMapper mapper) {
        this.subCategoryPort = subCategoryPort;
        this.mapper = mapper;
    }


    @Override
    public List<SubCategoryCapture> getSubCategories(Long requesterId, Long parentCategoryId) {
        return subCategoryPort.getSubCategories(requesterId, parentCategoryId)
                .stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<SubCategoryCapture> getSubCategory(Long requesterId, Long subCategoryId) {
        return subCategoryPort.getSubCategory(requesterId, subCategoryId)
                .map(mapper::toModel);
    }
}
