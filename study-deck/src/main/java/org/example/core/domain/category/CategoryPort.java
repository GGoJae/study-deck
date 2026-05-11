package org.example.core.domain.category;

import java.util.List;
import java.util.Optional;

public interface CategoryPort {
    Optional<Category> getCategoryEntity(Long ownerId, Long categoryId);

    List<Category> getCategories(Long ownerId, int offset, int limit);
}
