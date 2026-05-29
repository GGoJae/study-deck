package org.example.filestore.data.session;

import org.example.filestore.shared.FileManager;
import org.example.filestore.shared.model.AnswerSubmission;

import java.io.IOException;

public interface SubmitManager extends FileManager {
    AnswerSubmission getContext() throws IOException;

    void finish() throws IOException;

    boolean isWorkingOn();
}
