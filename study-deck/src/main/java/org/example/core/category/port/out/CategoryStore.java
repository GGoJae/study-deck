package org.example.core.category.port.out;

import org.example.core.category.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryStore {
    boolean isExistName(Long userId, String name);
    Category save(Category category);

    Optional<Category> findById(Long categoryId);

    List<Category> findByOwnerId(Long ownerId, int offset, int limit);

    List<Integer> findAllCategorySortKey(Long ownerId);
}
