package org.example.core.application.subcategory.service;

import org.example.core.application.subcategory.dto.request.CreateSubCategoryRequest;
import org.example.core.application.subcategory.usecase.SubCategoryCommandUseCase;

public class SubCategoryCommandServiceV1 implements SubCategoryCommandUseCase {
    @Override
    public Long createSubCategory(CreateSubCategoryRequest createSubCategoryRequest) {
        // TODO 서브카테고리 생성 로직
        return null;
    }

    @Override
    public void delete(Long requesterId, long subCategoryId) {
        // TODO 서브카테고리 제거 로직
    }

    @Override
    public void rename(Long requesterId, long subCategoryId, String newName) {
        // TODO 서브카테고리 이름 변경 로직 (현재 카테고리에 속해있는지 확인)
    }
}
