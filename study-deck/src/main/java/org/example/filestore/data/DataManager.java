package org.example.filestore.data;

import org.example.filestore.shared.model.Focus;

import java.io.IOException;

public interface DataManager {
    String getFileName(Focus focus) throws IOException;
}
