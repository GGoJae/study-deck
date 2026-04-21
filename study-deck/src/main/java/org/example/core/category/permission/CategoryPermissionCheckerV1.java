package org.example.core.category.permission;

import org.example.core.category.domain.Category;
import org.example.core.category.port.out.CategoryStore;

import java.util.Objects;

public class CategoryPermissionCheckerV1 implements CategoryPermissionChecker {

    private final CategoryStore categoryStore;

    public CategoryPermissionCheckerV1(CategoryStore categoryStore) {
        this.categoryStore = categoryStore;
    }

    @Override
    public void canReadThisCategory(Long userId, Long categoryId) {
        Category category = categoryStore.findById(categoryId).orElseThrow();// TODO Throw 들 만들어지면 적절한 Throw 추가
        if (!Objects.equals(category.getOwnerId(), userId)) {
            throw new IllegalStateException();
        }
    }
}
