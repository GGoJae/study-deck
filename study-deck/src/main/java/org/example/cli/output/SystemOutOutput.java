package org.example.cli.output;

import org.example.core.category.model.CategoryCapture;

import java.util.List;

public class SystemOutOutput implements Output{
    @Override
    public void categoriesAndCurrentCategory(List<CategoryCapture> categories, Long focusCategoryId) {
        categories.stream()
                .map(c -> {
                    if (c.id().equals(focusCategoryId)) {
                        return " * " + c.name();
                    }
                    return c.name();
                })
                .forEach(System.out::println);
    }
}
