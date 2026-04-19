package org.example.filestore.meta.manager;

import org.example.core.category.domain.Category;

import java.io.IOException;

public interface MetaDataManager {

    Category createCategory(Category category) throws IOException;
}
