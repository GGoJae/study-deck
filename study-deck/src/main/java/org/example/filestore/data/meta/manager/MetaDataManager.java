package org.example.filestore.data.meta.manager;

import org.example.filestore.shared.FileManager;
import org.example.filestore.shared.Transactionable;

import java.io.IOException;

public interface MetaDataManager extends Transactionable, FileManager {

    Long nextCategoryId() throws IOException;

    void selectCategory(Long id) throws IOException;

    Long currentCategory() throws IOException;

    void ifCurrentCategoryReset(Long categoryId) throws IOException;
}
