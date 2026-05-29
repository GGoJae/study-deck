package org.example.filestore.filesystem.manager;

import org.apache.commons.io.FileUtils;
import org.example.filestore.category.manager.CategoryManager;
import org.example.filestore.category.model.CategoryModel;
import org.example.filestore.data.meta.manager.MetaDataManager;
import org.example.filestore.filesystem.naming.FileNameGenerator;
import org.example.filestore.subcategory.manager.SubCategoryManager;
import org.example.filestore.subcategory.model.SubCategoryModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

import static org.example.filestore.shared.Constant.*;
import static org.example.filestore.shared.PathConfig.FILE_SYSTEM_TMP_PATH;
import static org.example.filestore.shared.PathConfig.FILE_SYSTEM_WORK_PATH;

public class FileSystemManagerV1 implements FileSystemManager {

    private final MetaDataManager metaDataManager;
    private final FileNameGenerator fileNameGenerator;
    private final CategoryManager categoryManager;
    private final SubCategoryManager subCategoryManager;

    public FileSystemManagerV1(MetaDataManager metaDataManager, FileNameGenerator fileNameGenerator, CategoryManager categoryManager, SubCategoryManager subCategoryManager) {
        this.metaDataManager = metaDataManager;
        this.fileNameGenerator = fileNameGenerator;
        this.categoryManager = categoryManager;
        this.subCategoryManager = subCategoryManager;
    }

    @Override
    public String createCategoryFile() throws IOException {
        String filename = fileNameGenerator.getFileName();
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        Path categoryDirPath = FILE_SYSTEM_TMP_PATH.resolve(filename);
        Files.createDirectory(categoryDirPath);

        return filename;
    }

    @Override
    public void deleteCategory(String filename) throws IOException {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        Path target = FILE_SYSTEM_TMP_PATH.resolve(filename);
        FileUtils.deleteDirectory(target.toFile());
    }

    @Override
    public String createSubCategory(String categoryFilename) throws IOException {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        String filename = fileNameGenerator.getFileName();

        Path subCategoryDirPath = FILE_SYSTEM_TMP_PATH.resolve(categoryFilename).resolve(filename);
        Files.createDirectory(subCategoryDirPath);

        return filename;
    }

    @Override
    public void deleteSubCategory(String categoryFilename, String subCategoryFilename) throws IOException {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        Path target = FILE_SYSTEM_TMP_PATH.resolve(categoryFilename).resolve(subCategoryFilename);
        FileUtils.deleteDirectory(target.toFile());
    }

    @Override
    public Path currentPath() throws IOException {
        Optional<Long> currentCategory = metaDataManager.currentCategory();
        Optional<Long> currentSubCategory = metaDataManager.currentSubCategory();

        if (isTransactionOn()) {
            if (currentCategory.isEmpty()) {
                return FILE_SYSTEM_TMP_PATH;
            } else if (currentSubCategory.isEmpty()) {
                String categoryFilename = categoryManager.findById(currentCategory.orElseThrow()).map(CategoryModel::fileName).orElseThrow();
                return FILE_SYSTEM_TMP_PATH.resolve(categoryFilename);
            } else {
                String categoryFilename = categoryManager.findById(currentCategory.orElseThrow()).map(CategoryModel::fileName).orElseThrow();
                String subCategoryFilename = subCategoryManager.findById(currentSubCategory.orElseThrow()).map(SubCategoryModel::filename).orElseThrow();
                return FILE_SYSTEM_TMP_PATH.resolve(categoryFilename).resolve(subCategoryFilename);
            }
        } else {
            if (currentCategory.isEmpty()) {
                return FILE_SYSTEM_WORK_PATH;
            } else if (currentSubCategory.isEmpty()) {
                String categoryFilename = categoryManager.findById(currentCategory.orElseThrow()).map(CategoryModel::fileName).orElseThrow();
                return FILE_SYSTEM_WORK_PATH.resolve(categoryFilename);
            } else {
                String categoryFilename = categoryManager.findById(currentCategory.orElseThrow()).map(CategoryModel::fileName).orElseThrow();
                String subCategoryFilename = subCategoryManager.findById(currentSubCategory.orElseThrow()).map(SubCategoryModel::filename).orElseThrow();
                return FILE_SYSTEM_WORK_PATH.resolve(categoryFilename).resolve(subCategoryFilename);
            }
        }
    }

    @Override
    public Optional<Path> subCategoryPath(long subCategoryId) throws IOException {
        Optional<SubCategoryModel> subCategoryOpt = subCategoryManager.findById(subCategoryId);
        if (subCategoryOpt.isEmpty()) return Optional.empty();

        SubCategoryModel subCategory = subCategoryOpt.orElseThrow();
        CategoryModel category = categoryManager.findById(subCategory.parentCategoryId()).orElseThrow();
        if (isTransactionOn()) {
            return Optional.of(FILE_SYSTEM_TMP_PATH.resolve(category.fileName()).resolve(subCategory.filename()));
        }
        return Optional.of(FILE_SYSTEM_WORK_PATH.resolve(category.fileName()).resolve(subCategory.filename()));
    }

    @Override
    public void transaction() {
        if (isTransactionOn()) throw new IllegalStateException(ALREADY_STARTED_TRANSACTION);
        try {
            FileUtils.copyDirectory(FILE_SYSTEM_WORK_PATH.toFile(), FILE_SYSTEM_TMP_PATH.toFile());
        } catch (IOException e) {
            System.out.println(TRANSACTION_FAILED);
            try {
                FileUtils.deleteDirectory(FILE_SYSTEM_TMP_PATH.toFile());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commit() {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);
        Path backupPath = FILE_SYSTEM_WORK_PATH.resolveSibling("work_bak");
        try {
            if (Files.exists(FILE_SYSTEM_WORK_PATH)) {
                Files.move(FILE_SYSTEM_WORK_PATH, backupPath, StandardCopyOption.REPLACE_EXISTING);
            }

            Files.move(FILE_SYSTEM_TMP_PATH, FILE_SYSTEM_WORK_PATH, StandardCopyOption.ATOMIC_MOVE);

            FileUtils.deleteQuietly(backupPath.toFile());
        } catch (IOException e) {
            System.out.println(COMMIT_FAILED);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollback() {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        try {
            FileUtils.deleteDirectory(FILE_SYSTEM_TMP_PATH.toFile());
        } catch (IOException e) {
            System.out.println(ROLLBACK_FAILED);
            throw new RuntimeException(e);
        }
    }

    private boolean isTransactionOff() {
        return Files.notExists(FILE_SYSTEM_TMP_PATH);
    }

    private boolean isTransactionOn() {
        return Files.exists(FILE_SYSTEM_TMP_PATH);
    }

    @Override
    public void init() throws IOException {
        Files.createDirectory(FILE_SYSTEM_WORK_PATH);
    }
}
