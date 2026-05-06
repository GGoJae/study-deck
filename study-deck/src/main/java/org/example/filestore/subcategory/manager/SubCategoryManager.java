package org.example.filestore.subcategory.manager;

import org.example.filestore.shared.FileManager;
import org.example.filestore.shared.Transactionable;
import org.example.filestore.subcategory.model.SubCategoryModel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SubCategoryManager extends Transactionable, FileManager {
    List<SubCategoryModel> findByParentCategoryId(Long parentCategoryId) throws IOException;

    Optional<SubCategoryModel> findById(Long subCategoryId) throws IOException;
}
