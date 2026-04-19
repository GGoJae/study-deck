package org.example.filestore.file;

import java.io.IOException;
import java.nio.file.Path;

public interface FileSystemManager {
    void createCategoryFile(Path path) throws IOException;
    void trashFileCleanUp(Path path);
}
