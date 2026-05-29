package org.example.filestore.card.manager;

import org.example.filestore.card.model.AnswerModel;
import org.example.filestore.shared.FileNeedPathManager;
import org.example.filestore.shared.Transactionable;

import java.io.IOException;

public interface AnswerManager extends FileNeedPathManager, Transactionable {
    void save(AnswerModel answerModel) throws IOException;
}
