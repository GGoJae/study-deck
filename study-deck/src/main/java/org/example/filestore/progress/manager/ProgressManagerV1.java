package org.example.filestore.progress.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.application.progress.metadata.CardProgress;
import org.example.core.application.progress.metadata.Deck;
import org.example.filestore.common.JsonMapper;
import org.example.filestore.filesystem.manager.FileSystemManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.example.filestore.shared.Constant.*;
import static org.example.filestore.shared.PathConfig.PROGRESS_TMP_NAME;
import static org.example.filestore.shared.PathConfig.PROGRESS_WORK_NAME;

public class ProgressManagerV1 implements ProgressManager{

    private final ObjectMapper mapper = JsonMapper.getInstance();
    private final FileSystemManager fileSystemManager;

    public ProgressManagerV1(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
    }


    @Override
    public List<CardProgress> findBySubCategoryId(Long subCategoryId) throws IOException {
        Path path = fileSystemManager.subCategoryPath(subCategoryId).orElseThrow();
        return mapper.readValue(path.resolve(PROGRESS_WORK_NAME).toFile(), new TypeReference<List<CardProgress>>() {});
    }

    @Override
    public void update(Deck target) throws IOException {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);
        Path path = fileSystemManager.subCategoryPath(target.subCategoryId()).orElseThrow();
        mapper.writeValue(path.resolve(PROGRESS_TMP_NAME).toFile(), target.progresses());
    }

    @Override
    public void init(Path path) throws IOException {
        Path resolve = Files.createFile(path.resolve(PROGRESS_WORK_NAME));
        mapper.writeValue(resolve.toFile(), List.of());
    }

    @Override
    public void transaction() {
        try {
            if (isTransactionOn()) throw new IllegalStateException(ALREADY_STARTED_TRANSACTION);
            Path path = fileSystemManager.currentPath();
            Files.copy(path.resolve(PROGRESS_WORK_NAME), path.resolve(PROGRESS_TMP_NAME));
        } catch (IOException e) {
            System.out.println(TRANSACTION_FAILED);
            throw new RuntimeException(e);
        }
    }
    @Override
    public void commit() {
        try {
            if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);
            Path path = fileSystemManager.currentPath();
            Files.move(path.resolve(PROGRESS_TMP_NAME), path.resolve(PROGRESS_WORK_NAME), REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(COMMIT_FAILED);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollback() {
        try {
            if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);
            Path path = fileSystemManager.currentPath();
            Files.delete(path.resolve(PROGRESS_TMP_NAME));
        } catch (IOException e) {
            System.out.println(ROLLBACK_FAILED);
            throw new RuntimeException(e);
        }
    }

    private boolean isTransactionOff() throws IOException {
        Path path = fileSystemManager.currentPath();
        return Files.notExists(path.resolve(PROGRESS_TMP_NAME));
    }

    private boolean isTransactionOn() throws IOException {
        Path path = fileSystemManager.currentPath();
        return Files.exists(path.resolve(PROGRESS_TMP_NAME));
    }

}
