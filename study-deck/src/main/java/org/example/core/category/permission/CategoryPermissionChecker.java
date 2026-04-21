package org.example.core.category.permission;

public interface CategoryPermissionChecker {

    void canReadThisCategory(Long userId, Long categoryId);
}
