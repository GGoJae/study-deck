package org.example.filestore.data.meta.manager;

import org.example.filestore.shared.FileManager;
import org.example.filestore.shared.Transactionable;
import org.example.filestore.shared.model.Focus;
import org.example.filestore.shared.model.Type;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface MetaDataManager extends Transactionable, FileManager {

    Long nextCategoryId() throws IOException;

    void selectCategory(Long categoryId) throws IOException;

    void selectSubCategory(Long subCategoryId) throws IOException;

    void selectCard(Long cardId) throws IOException;

    Optional<Long> currentCategory() throws IOException;

    Optional<Long> currentSubCategory() throws IOException;

    void ifCurrentCategoryReset(Long categoryId) throws IOException;

    void ifCurrentSubCategoryReset(Long subCategoryId) throws IOException;

    Long nextSubCategoryId() throws IOException;

    Long nextCardId() throws IOException;

    Long nextAnswerId() throws IOException;

    boolean isCurrentContentCard() throws IOException;

    Optional<Long> contentId() throws IOException;

    Focus currentFocus() throws IOException;

    void ifCurrentContentReset(Type type, Long id) throws IOException;
}
