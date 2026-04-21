package org.example.cli.output;

import org.example.core.category.model.CategoryCapture;

import java.util.List;

public interface Output {
    void categoriesAndCurrentCategory(List<CategoryCapture> categories, Long focusCategoryId);
}
