package org.example.filestore.card.manager;

import org.example.filestore.card.model.CardModel;
import org.example.filestore.shared.FileManager;
import org.example.filestore.shared.Transactionable;

import java.io.IOException;

public interface CardManager extends FileManager, Transactionable {
    void save(CardModel jsonModel) throws IOException;
}
