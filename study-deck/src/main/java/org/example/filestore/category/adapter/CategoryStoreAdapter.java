package org.example.filestore.category.adapter;

import org.example.core.domain.category.Category;
import org.example.core.domain.category.CategoryStore;
import org.example.filestore.category.manager.CategoryManager;
import org.example.filestore.shared.ModelToDomainMapper;
import org.example.filestore.data.meta.manager.MetaDataManager;
import org.example.filestore.filesystem.manager.FileSystemManager;
import org.example.filestore.category.model.CategoryModel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CategoryStoreAdapter implements CategoryStore {
    private final FileSystemManager fileSystemManager;
    private final CategoryManager categoryManager;
    private final MetaDataManager metaDataManager;
    private final ModelToDomainMapper<Category, CategoryModel> modelToDomainMapper;

    public CategoryStoreAdapter(FileSystemManager fileSystemManager, CategoryManager categoryManager, MetaDataManager metaDataManager, ModelToDomainMapper<Category, CategoryModel> modelToDomainMapper) {
        this.fileSystemManager = fileSystemManager;
        this.categoryManager = categoryManager;
        this.metaDataManager = metaDataManager;
        this.modelToDomainMapper = modelToDomainMapper;
    }

    @Override
    public boolean isExistName(Long userId, String name) {
        return false;
    }

    @Override
    public Category save(Category category) {
        try {
            fileSystemManager.transaction();
            categoryManager.transaction();
            metaDataManager.transaction();

            Long nextId = metaDataManager.nextCategoryId();
            category = category.withId(nextId);
            String fileName = fileSystemManager.createCategoryFile();

            CategoryModel categoryModel =
                    new CategoryModel(
                            category.getId(), fileName, category.getOwnerId(), category.getName(),
                            category.getSortKey(), category.getCreatedAt(), category.getUpdatedAt(),
                            category.getCreatedUser(), category.getUpdatedUser()
                    );

            categoryManager.save(categoryModel);

            fileSystemManager.commit();
            categoryManager.commit();
            metaDataManager.commit();
            return category;
        } catch (Exception e) {
            fileSystemManager.rollback();
            categoryManager.rollback();
            metaDataManager.rollback();
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Category> findById(Long categoryId) {
        try {
            return categoryManager.findById(categoryId)
                    .map(modelToDomainMapper::toDomain);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Category> findByOwnerId(Long ownerId, int offset, int limit) {
        try {
            List<CategoryModel> models = categoryManager.findByOwnerId(ownerId, offset, limit);
            return modelToDomainMapper.toDomain(models);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Integer> findAllCategorySortKey(Long ownerId) {
        try {
            return categoryManager.findByOwnerId(ownerId, 0, 100).stream()
                    .map(CategoryModel::sortKey).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Category category) {
        categoryManager.transaction();
        try {
            categoryManager.update(category);
            categoryManager.commit();
        } catch (Exception e) {
            categoryManager.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long categoryId) {
        metaDataManager.transaction();
        categoryManager.transaction();
        fileSystemManager.transaction();

        try {
            metaDataManager.ifCurrentCategoryReset(categoryId);
            String filename = categoryManager.delete(categoryId);
            fileSystemManager.deleteCategory(filename);

            metaDataManager.commit();
            categoryManager.commit();
            fileSystemManager.commit();
        } catch (Exception e) {
            metaDataManager.rollback();
            categoryManager.rollback();
            fileSystemManager.rollback();
            throw new RuntimeException(e);
        }
    }

}
