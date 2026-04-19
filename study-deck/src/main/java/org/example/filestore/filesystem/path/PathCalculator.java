package org.example.filestore.filesystem.path;

import org.example.filestore.shared.model.Focus;

import java.io.IOException;
import java.nio.file.Path;

public interface PathCalculator {
    Path getPath(Focus focus) throws IOException;
}
