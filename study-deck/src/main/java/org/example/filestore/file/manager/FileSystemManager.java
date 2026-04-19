package org.example.filestore.file.manager;

import org.example.core.category.domain.Category;
import org.example.filestore.shared.Transactionable;
import org.example.filestore.shared.model.CategoryModel;

import java.io.IOException;

public interface FileSystemManager extends Transactionable {
    CategoryModel createCategoryFile(Category category) throws IOException;
}
