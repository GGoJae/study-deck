package org.example.filestore.category.adapter;

import org.example.core.category.domain.Category;
import org.example.core.category.port.out.CategoryStore;
import org.example.filestore.data.category.manager.CategoryManager;
import org.example.filestore.data.meta.manager.MetaDataManager;
import org.example.filestore.filesystem.manager.FileSystemManager;
import org.example.filestore.shared.model.CategoryModel;

import java.io.IOException;
import java.time.Instant;

public class CategoryStoreAdapter implements CategoryStore {
    private final FileSystemManager fileSystemManager;
    private final CategoryManager categoryManager;
    private final MetaDataManager metaDataManager;

    public CategoryStoreAdapter(FileSystemManager fileSystemManager, CategoryManager categoryManager, MetaDataManager metaDataManager) {
        this.fileSystemManager = fileSystemManager;
        this.categoryManager = categoryManager;
        this.metaDataManager = metaDataManager;
    }

    @Override
    public boolean isExistName(Long userId, String name) {
        return false;
    }

    @Override
    public Category save(Category category) {
        fileSystemManager.transaction();
        categoryManager.transaction();
        metaDataManager.transaction();

        try {
            categoryAssemble(category);
            String fileName = fileSystemManager.createCategoryFile();

            CategoryModel categoryModel = new CategoryModel(category.getId(), fileName, category.getOwnerId(), category.getName(),
                    category.getSortKey(), category.getCreatedAt(), category.getUpdatedAt());

            categoryManager.save(categoryModel);
            metaDataManager.selectCategory(category.getId());

            fileSystemManager.commit();
            categoryManager.commit();
            metaDataManager.commit();
            return category;
        } catch (IOException e) {
            fileSystemManager.rollback();
            categoryManager.rollback();
            metaDataManager.rollback();
            throw new RuntimeException(e);
        }
    }

    private void categoryAssemble(Category category) throws IOException {
        Long nextId = metaDataManager.nextCategoryId();
        Instant now = Instant.now();
        category.setId(nextId);
        category.setCreatedAt(now);
        category.setUpdatedAt(now);
    }
}
