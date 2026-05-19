package org.example.filestore.data.meta.manager;

import org.example.filestore.shared.FileManager;
import org.example.filestore.shared.Transactionable;

import java.io.IOException;
import java.nio.file.Path;

public interface MetaDataManager extends Transactionable, FileManager {

    Long nextCategoryId() throws IOException;

    void selectCategory(Long id) throws IOException;

    void selectSubCategory(Long subCategoryId) throws IOException;

    Long currentCategory() throws IOException;

    Long currentSubCategory() throws IOException;

    void ifCurrentCategoryReset(Long categoryId) throws IOException;

    void ifCurrentSubCategoryReset(Long subCategoryId) throws IOException;

    Long nextSubCategoryId() throws IOException;

    Long nextCardId() throws IOException;
}
