package org.example.cli.output;

import org.example.cli.model.display.Question;
import org.example.core.application.card.dto.response.CardCapture;
import org.example.core.application.category.dto.response.CategoryCapture;
import org.example.core.application.subcategory.dto.response.SubCategoryCapture;

import java.util.List;

public interface Output {
    void categoriesAndCurrentCategory(List<CategoryCapture> categories, Long focusCategoryId);

    void errorMessage(String message);

    void subAndCurrentSub(List<SubCategoryCapture> subCategories, Long focusSubCatId);

    void showCards(List<CardCapture> cards, Long focusCardId);

    void showQuestion(Question question);
}
