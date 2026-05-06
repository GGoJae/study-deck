package org.example.core.application.subcategory.dto.request;

public record CreateSubCategoryRequest(
        Long ownerId,
        Long parentCategoryId,
        String name,
        Integer hopeSortKey
) {
    public CreateSubCategoryRequest {
        if (name.isBlank()) {
            throw new IllegalArgumentException();
        }
        name = name.trim();
    }
}
