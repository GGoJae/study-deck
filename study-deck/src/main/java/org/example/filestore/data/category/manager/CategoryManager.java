package org.example.filestore.data.category.manager;

import org.example.filestore.shared.Transactionable;
import org.example.filestore.shared.model.CategoryModel;

public interface CategoryManager extends Transactionable {

    void save(CategoryModel categoryModel);
}
