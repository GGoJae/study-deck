package org.example.filestore.category.validator;

import org.example.core.category.domain.Category;

public interface CategoryValidator {
    boolean canThisCategoryCreate(Category category);
}
