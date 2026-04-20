package org.example.core.category.port.in;

import org.example.core.category.model.CreateCategory;

public interface CategoryCommandPort {
    Long create(CreateCategory model);
}
