package org.example.filestore.filesystem.path;

import org.example.filestore.data.DataManager;
import org.example.filestore.data.category.manager.CategoryManager;
import org.example.filestore.shared.model.Focus;

import java.io.IOException;
import java.nio.file.Path;

import static org.example.filestore.shared.PathConfig.FILE_SYSTEM_WORK_PATH;

public class PathCalculatorV1 implements PathCalculator{

    private final CategoryManager categoryManager;

    private final DataManager dataManager;

    public PathCalculatorV1(CategoryManager categoryManager, DataManager dataManager) {
        this.categoryManager = categoryManager;
        this.dataManager = dataManager;
    }

    @Override
    public Path getPath(Focus focus) throws IOException {
        Path basePath = FILE_SYSTEM_WORK_PATH;

        Long categoryId = focus.categoryId();
        if (categoryId == null) return basePath;
        Path categoryPath = basePath.resolve(categoryManager.getFilename(categoryId));

        Long subCategoryId = focus.subCategoryId();
        if (subCategoryId == null) return categoryPath;

        // TODO subCategory filename 등등 계속 추가해서 리턴해주기

        return null;
    }
}
