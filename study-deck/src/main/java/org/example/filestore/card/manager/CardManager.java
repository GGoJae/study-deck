package org.example.filestore.card.manager;

import org.example.filestore.card.model.CardModel;
import org.example.filestore.shared.FileManager;
import org.example.filestore.shared.Transactionable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CardManager extends FileManager, Transactionable {
    void save(CardModel jsonModel) throws IOException;
    Optional<CardModel> findById(Long cardId) throws IOException;

    List<CardModel> findBySubCategoryId(Long subCategoryId) throws IOException;
}
