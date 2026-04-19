package org.example.filestore.category.validator;

import org.example.core.category.domain.Category;
import org.example.filestore.FileFormatValidator;

import java.nio.file.Files;
import java.nio.file.Path;

public class CategoryValidatorV1 implements CategoryValidator{
    @Override
    public boolean canThisCategoryCreate(Category category) {
        if (FileFormatValidator.isWrongDirFormat(category.getName())) return false;

        Path path = Path.of(category.getName());
        if (Files.exists(path)) return false;

        return true;
    }
}
