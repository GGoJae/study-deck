package org.example.filestore.subcategory.adapter;

import org.example.core.domain.subcategory.SubCategory;
import org.example.core.domain.subcategory.SubCategoryStore;
import org.example.filestore.category.manager.CategoryManager;
import org.example.filestore.category.model.CategoryModel;
import org.example.filestore.data.meta.manager.MetaDataManager;
import org.example.filestore.filesystem.manager.FileSystemManager;
import org.example.filestore.shared.ModelToDomainMapper;
import org.example.filestore.subcategory.manager.SubCategoryManager;
import org.example.filestore.subcategory.model.SubCategoryModel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SubCategoryStoreAdapter implements SubCategoryStore {

    private final FileSystemManager fileSystemManager;
    private final CategoryManager categoryManager;
    private final MetaDataManager metaDataManager;
    private final SubCategoryManager subCategoryManager;
    private final ModelToDomainMapper<SubCategory, SubCategoryModel> mapper;


    public SubCategoryStoreAdapter(FileSystemManager fileSystemManager, CategoryManager categoryManager, MetaDataManager metaDataManager, SubCategoryManager subCategoryManager, ModelToDomainMapper<SubCategory, SubCategoryModel> mapper) {
        this.fileSystemManager = fileSystemManager;
        this.categoryManager = categoryManager;
        this.metaDataManager = metaDataManager;
        this.subCategoryManager = subCategoryManager;
        this.mapper = mapper;
    }
    @Override
    public List<SubCategory> findByParentCategoryId(Long parentCategoryId) {
        try {
            return mapper.toDomain(
                    subCategoryManager.findByParentCategoryId(parentCategoryId)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SubCategory> findById(Long subCategoryId) {
        try {
            return subCategoryManager.findById(subCategoryId)
                    .map(mapper::toDomain);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SubCategory save(SubCategory subCategory) {
        try {
            metaDataManager.transaction();
            fileSystemManager.transaction();
            subCategoryManager.transaction();
            categoryManager.transaction();

            Long nextId = metaDataManager.nextSubCategoryId();
            SubCategory withId = subCategory.withId(nextId);
            String categoryFilename = categoryManager.findById(subCategory.getParentCategoryId()).map(CategoryModel::fileName).orElseThrow();
            String filename = fileSystemManager.createSubCategory(categoryFilename);
            SubCategoryModel model = SubCategoryModel.of(withId, filename);
            subCategoryManager.save(model);

            metaDataManager.commit();
            fileSystemManager.commit();
            subCategoryManager.commit();
            categoryManager.commit();
            return withId;
        } catch (IOException e) {
            metaDataManager.rollback();
            fileSystemManager.rollback();
            subCategoryManager.rollback();
            categoryManager.rollback();

            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long subCategoryId) {
        try {
            metaDataManager.transaction();
            fileSystemManager.transaction();
            subCategoryManager.transaction();
            categoryManager.transaction();

            metaDataManager.ifCurrentSubCategoryReset(subCategoryId);
            SubCategoryModel target = subCategoryManager.delete(subCategoryId);
            String categoryFilename = categoryManager.findById(target.parentCategoryId()).map(CategoryModel::fileName).orElseThrow();
            String subCategoryFilename = target.filename();
            fileSystemManager.deleteSubCategory(categoryFilename, subCategoryFilename);

            metaDataManager.commit();
            fileSystemManager.commit();
            subCategoryManager.commit();
            categoryManager.commit();

        } catch (IOException e) {
            metaDataManager.rollback();
            fileSystemManager.rollback();
            subCategoryManager.rollback();
            categoryManager.rollback();

            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(SubCategory updated) {
        try {
            subCategoryManager.transaction();

            subCategoryManager.update(updated);

            subCategoryManager.commit();

        } catch (IOException e) {
            subCategoryManager.rollback();

            throw new RuntimeException(e);
        }
    }
}
