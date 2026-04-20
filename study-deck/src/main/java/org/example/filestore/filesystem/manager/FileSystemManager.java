package org.example.filestore.filesystem.manager;

import org.example.filestore.shared.FileManager;
import org.example.filestore.shared.Transactionable;

import java.io.IOException;

public interface FileSystemManager extends Transactionable, FileManager {
    String createCategoryFile() throws IOException;
}
