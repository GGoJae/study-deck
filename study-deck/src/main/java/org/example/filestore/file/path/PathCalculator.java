package org.example.filestore.file.path;

import org.example.filestore.shared.model.Focus;

import java.nio.file.Path;

public interface PathCalculator {
    Path getPath(Focus focus);
}
