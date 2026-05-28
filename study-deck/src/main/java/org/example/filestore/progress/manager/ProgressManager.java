package org.example.filestore.progress.manager;

import org.example.core.application.progress.metadata.Deck;
import org.example.filestore.shared.FileManager;
import org.example.filestore.shared.Transactionable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProgressManager extends FileManager, Transactionable {
    Optional<Deck> findBySubCategoryId(Long subCategoryId) throws IOException;

    List<Deck> findAll() throws IOException;

    void update(Deck updated) throws IOException;
}
