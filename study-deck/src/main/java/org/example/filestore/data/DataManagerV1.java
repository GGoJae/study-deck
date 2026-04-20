package org.example.filestore.data;

import org.example.filestore.data.category.manager.CategoryManager;
import org.example.filestore.shared.model.Focus;

import java.io.IOException;

import static org.example.filestore.shared.PathConfig.FILE_SYSTEM_WORK_PATH;

public class DataManagerV1 implements DataManager{

    private final CategoryManager categoryManager;

    public DataManagerV1(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    @Override
    public String getFileName(Focus focus) throws IOException {

        Long categoryId = focus.categoryId();
        if (categoryId == null) return FILE_SYSTEM_WORK_PATH.getFileName().toString();

        Long subCategoryId = focus.subCategoryId();
        if (subCategoryId == null) {
            return categoryManager.getFilename(categoryId);
        }

        // TODO 나중에 subcategoryManager, NoteManager 등등 나오면 더 분기

        return null;
    }
}
