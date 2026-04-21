package org.example.filestore.category.adapter;

import org.example.core.category.domain.Category;
import org.example.core.category.port.out.CategoryStore;
import org.example.filestore.category.manager.CategoryManager;
import org.example.filestore.category.mapper.ModelToDomainMapper;
import org.example.filestore.data.meta.manager.MetaDataManager;
import org.example.filestore.filesystem.manager.FileSystemManager;
import org.example.filestore.category.model.CategoryModel;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class CategoryStoreAdapter implements CategoryStore {
    private final FileSystemManager fileSystemManager;
    private final CategoryManager categoryManager;
    private final MetaDataManager metaDataManager;
    private final ModelToDomainMapper modelToDomainMapper;

    public CategoryStoreAdapter(FileSystemManager fileSystemManager, CategoryManager categoryManager, MetaDataManager metaDataManager, ModelToDomainMapper modelToDomainMapper) {
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
        fileSystemManager.transaction();
        categoryManager.transaction();
        metaDataManager.transaction();

        try {
            categoryAssemble(category);
            String fileName = fileSystemManager.createCategoryFile();

            CategoryModel categoryModel =
                    new CategoryModel(
                        category.getId(), fileName, category.getOwnerId(), category.getName(),
                        category.getSortKey(), category.getCreatedAt(), category.getUpdatedAt(),
                        category.getCreatedUser(), category.getUpdatedUser()
                    );

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

    private void categoryAssemble(Category category) throws IOException {
        Long nextId = metaDataManager.nextCategoryId();
        Instant now = Instant.now();
        category.setId(nextId);
        category.setCreatedAt(now);
        category.setUpdatedAt(now);
    }
}
