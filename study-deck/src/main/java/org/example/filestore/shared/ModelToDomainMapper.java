package org.example.filestore.shared;

import org.example.core.domain.category.Category;
import org.example.filestore.category.model.CategoryModel;

import java.util.List;

public interface  ModelToDomainMapper<D, M> {
    D toDomain(M model);

    List<D> toDomain(List<M> models);
}
