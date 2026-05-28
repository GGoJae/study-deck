package org.example.filestore.shared;

import java.io.IOException;
import java.nio.file.Path;

public interface FileNeedPathManager {
    void init(Path path) throws IOException;
}
