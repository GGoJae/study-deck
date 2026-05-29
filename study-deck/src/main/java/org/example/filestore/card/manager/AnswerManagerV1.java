package org.example.filestore.card.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.filestore.card.model.AnswerModel;
import org.example.filestore.common.JsonMapper;
import org.example.filestore.filesystem.manager.FileSystemManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.example.filestore.shared.Constant.*;
import static org.example.filestore.shared.PathConfig.ANSWER_TMP_NAME;
import static org.example.filestore.shared.PathConfig.ANSWER_WORK_NAME;

public class AnswerManagerV1 implements AnswerManager{
    private final FileSystemManager fileSystemManager;
    private final ObjectMapper mapper = JsonMapper.getInstance();

    public AnswerManagerV1(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
    }

    @Override
    public void save(AnswerModel answerModel) throws IOException {
        Objects.requireNonNull(answerModel);

        Path currentPath = fileSystemManager.currentPath();
        if (isTransactionOff(currentPath)) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        List<AnswerModel> answerModels = mapper.readValue(currentPath.resolve(ANSWER_TMP_NAME).toFile(), new TypeReference<List<AnswerModel>>() {
        });

        Set<Long> ids = answerModels.stream().map(AnswerModel::id).collect(Collectors.toSet());
        if (ids.contains(answerModel.id())) {
            throw new IllegalArgumentException();
        }

        ArrayList<AnswerModel> newList = new ArrayList<>(answerModels.stream().toList());
        newList.add(answerModel);

        mapper.writeValue(currentPath.resolve(ANSWER_TMP_NAME).toFile(), newList);
    }

    @Override
    public void init(Path path) throws IOException {
        Objects.requireNonNull(path);

        Path resolve = Files.createFile(path.resolve(ANSWER_WORK_NAME));
        mapper.writeValue(resolve.toFile(), List.of());
    }

    @Override
    public void transaction() {
        try {
            Path currentPath = fileSystemManager.currentPath();
            if (isTransactionOn(currentPath)) throw new IllegalStateException(ALREADY_STARTED_TRANSACTION);
            Files.copy(currentPath.resolve(ANSWER_WORK_NAME), currentPath.resolve(ANSWER_TMP_NAME), COPY_ATTRIBUTES);
        } catch (IOException e) {
            System.out.println(TRANSACTION_FAILED);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commit() {
        try {
            Path currentPath = fileSystemManager.currentPath();
            if (isTransactionOff(currentPath)) throw new IllegalStateException(NOT_STARTED_TRANSACTION);
            Files.move(currentPath.resolve(ANSWER_TMP_NAME), currentPath.resolve(ANSWER_WORK_NAME), REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(COMMIT_FAILED);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollback() {
        try {
            Path currentPath = fileSystemManager.currentPath();
            if (isTransactionOff(currentPath)) throw new IllegalStateException(NOT_STARTED_TRANSACTION);
            Files.delete(currentPath.resolve(ANSWER_TMP_NAME));
        } catch (IOException e) {
            System.out.println(ROLLBACK_FAILED);
            throw new RuntimeException(e);
        }
    }

    private boolean isTransactionOn(Path path) {
        return Files.exists(path.resolve(ANSWER_TMP_NAME));
    }

    private boolean isTransactionOff(Path path) {
        return Files.notExists(path.resolve(ANSWER_TMP_NAME));
    }
}
