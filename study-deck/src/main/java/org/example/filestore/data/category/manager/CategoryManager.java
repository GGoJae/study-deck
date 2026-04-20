package org.example.filestore.data.category.manager;

import org.example.filestore.shared.FileManager;
import org.example.filestore.shared.Transactionable;
import org.example.filestore.shared.model.CategoryModel;

import java.io.IOException;

public interface CategoryManager extends Transactionable, FileManager {

    void save(CategoryModel categoryModel) throws IOException;

    String getFilename(Long categoryId) throws IOException;
}
