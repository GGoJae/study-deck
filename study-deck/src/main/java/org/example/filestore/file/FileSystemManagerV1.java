package org.example.filestore.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystemManagerV1 implements FileSystemManager{
    @Override
    public void createCategoryFile(Path path) throws IOException {
        Files.createDirectory(path);
    }

    @Override
    public void trashFileCleanUp(Path path) {

    }
}
