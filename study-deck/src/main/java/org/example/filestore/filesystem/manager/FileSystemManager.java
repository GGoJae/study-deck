package org.example.filestore.filesystem.manager;

import org.example.filestore.shared.FileManager;
import org.example.filestore.shared.Transactionable;

import java.io.IOException;
import java.nio.file.Path;

public interface FileSystemManager extends Transactionable, FileManager {
    String createCategoryFile() throws IOException;

    void deleteCategory(String filename) throws IOException;

    String createSubCategory(String categoryFilename) throws IOException;

    void deleteSubCategory(String categoryFilename, String subCategoryFilename) throws IOException;

    Path currentPath() throws IOException;
}
