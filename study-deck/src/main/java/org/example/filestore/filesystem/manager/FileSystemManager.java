package org.example.filestore.filesystem.manager;

import org.example.filestore.shared.Transactionable;

import java.io.IOException;

public interface FileSystemManager extends Transactionable {
    String createCategoryFile() throws IOException;
}
