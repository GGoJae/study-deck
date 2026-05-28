package org.example.filestore.progress.manager;

import org.example.core.application.progress.metadata.CardProgress;
import org.example.core.application.progress.metadata.Deck;
import org.example.filestore.shared.FileManager;
import org.example.filestore.shared.FileNeedPathManager;
import org.example.filestore.shared.Transactionable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProgressManager extends FileNeedPathManager, Transactionable {
    List<CardProgress> findBySubCategoryId(Long subCategoryId) throws IOException;

    void update(Deck updated) throws IOException;
}
