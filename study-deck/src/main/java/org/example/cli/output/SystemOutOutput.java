package org.example.cli.output;

import org.example.core.application.category.dto.response.CategoryCapture;
import org.example.core.application.subcategory.dto.response.SubCategoryCapture;

import java.util.List;

public class SystemOutOutput implements Output{
    @Override
    public void categoriesAndCurrentCategory(List<CategoryCapture> categories, Long focusCategoryId) {
        categories.stream()
                .map(c -> {
                    if (c.id().equals(focusCategoryId)) {
                        return ConsoleColor.GREEN + "[" + c.id() + "] " + c.name() + ConsoleColor.RESET;
                    }
                    return "[" + c.id() + "] " + c.name();
                })
                .forEach(System.out::println);
    }

    @Override
    public void errorMessage(String message) {
        System.out.println(ConsoleColor.RED + "[ERROR] " + message + ConsoleColor.RESET );
    }

    @Override
    public void subAndCurrentSub(List<SubCategoryCapture> subCategories, Long focusSubCatId) {
        subCategories.stream()
                .map(c -> {
                    if (c.id().equals(focusSubCatId)) {
                        return ConsoleColor.GREEN + "[" + c.id() + "] " + c.name() + ConsoleColor.RESET;
                    }
                    return "[" + c.id() + "] " + c.name();
                })
                .forEach(System.out::println);
    }
}
