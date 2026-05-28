package org.example.cli.output;

import org.example.cli.model.display.Question;
import org.example.core.application.card.dto.response.CardCapture;
import org.example.core.application.category.dto.response.CategoryCapture;
import org.example.core.application.subcategory.dto.response.SubCategoryCapture;

import java.util.List;
import java.util.Objects;

public class SystemOutOutput implements Output{
    @Override
    public void categoriesAndCurrentCategory(List<CategoryCapture> categories, Long focusCategoryId) {
        categories.stream()
                .map(c -> {
                    if (Objects.equals(c.id(), focusCategoryId)) {
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
                    if (Objects.equals(c.id(), focusSubCatId)) {
                        return ConsoleColor.GREEN + "[" + c.id() + "] " + c.name() + ConsoleColor.RESET;
                    }
                    return "[" + c.id() + "] " + c.name();
                })
                .forEach(System.out::println);
    }

    @Override
    public void showCards(List<CardCapture> cards, Long focusCardId) {
        cards.stream()
                .map(c -> {
                    if (Objects.equals(c.id(), focusCardId)) {
                        return ConsoleColor.GREEN + "[" + c.id() + "] " + c.displayName() + ConsoleColor.RESET;
                    }
                    return "[" + c.id() + "] " + c.displayName();
                })
                .forEach(System.out::println);
    }

    @Override
    public void showQuestion(Question question) {
        System.out.println(question.id());
        System.out.println(question.title() + "\n");
        System.out.println(question.question());
    }
}
