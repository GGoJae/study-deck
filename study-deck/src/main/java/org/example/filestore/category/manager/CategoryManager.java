package org.example.filestore.category.manager;

import org.example.core.category.domain.Category;
import org.example.filestore.shared.FileManager;
import org.example.filestore.shared.Transactionable;
import org.example.filestore.category.model.CategoryModel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CategoryManager extends Transactionable, FileManager {

    void save(CategoryModel categoryModel) throws IOException;

    String getFilename(Long categoryId) throws IOException;

    Optional<CategoryModel> findById(Long categoryId) throws IOException;

    List<CategoryModel> findByOwnerId(Long ownerId, int offset, int limit) throws  IOException;
}
