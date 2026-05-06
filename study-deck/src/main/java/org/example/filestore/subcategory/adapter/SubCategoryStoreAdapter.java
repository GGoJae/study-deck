package org.example.filestore.subcategory.adapter;

import org.example.core.domain.subcategory.SubCategory;
import org.example.core.domain.subcategory.SubCategoryStore;
import org.example.filestore.category.manager.CategoryManager;
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
}
