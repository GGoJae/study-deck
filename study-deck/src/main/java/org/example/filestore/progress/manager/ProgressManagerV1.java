package org.example.filestore.progress.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.application.progress.metadata.Deck;
import org.example.filestore.common.JsonMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.example.filestore.shared.Constant.*;
import static org.example.filestore.shared.PathConfig.PROGRESS_TMP_PATH;
import static org.example.filestore.shared.PathConfig.PROGRESS_WORK_PATH;

public class ProgressManagerV1 implements ProgressManager{

    private final ObjectMapper mapper = JsonMapper.getInstance();

    @Override
    public Optional<Deck> findBySubCategoryId(Long subCategoryId) throws IOException {
        return getDecksFromWork()
                .stream()
                .filter(d -> Objects.equals(subCategoryId, d.subCategoryId()))
                .findAny();
    }

    @Override
    public List<Deck> findAll() throws IOException {
        return getDecksFromWork();
    }

    @Override
    public void update(Deck target) throws IOException {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        List<Deck> decks = mapper.readValue(PROGRESS_TMP_PATH.toFile(), new TypeReference<List<Deck>>() {
        });

        List<Deck> updated = decks.stream().map(d -> Objects.equals(d.subCategoryId(), target.subCategoryId()) ? target : d).toList();
        mapper.writeValue(PROGRESS_TMP_PATH.toFile(), updated);
    }

    private List<Deck> getDecksFromWork() throws IOException {
        return mapper.readValue(PROGRESS_WORK_PATH.toFile(), new TypeReference<List<Deck>>() {
        });
    }

    @Override
    public void init() throws IOException {
        Files.createFile(PROGRESS_WORK_PATH);
        mapper.writeValue(PROGRESS_WORK_PATH.toFile(), List.of());
    }

    @Override
    public void transaction() {
        if (isTransactionOn()) throw new IllegalStateException(ALREADY_STARTED_TRANSACTION);

        try {
            Files.copy(PROGRESS_WORK_PATH, PROGRESS_TMP_PATH);
        } catch (IOException e) {
            System.out.println(TRANSACTION_FAILED);
            throw new RuntimeException(e);
        }
    }
    @Override
    public void commit() {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        try {
            Files.move(PROGRESS_TMP_PATH, PROGRESS_WORK_PATH, REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(COMMIT_FAILED);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollback() {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        try {
            Files.delete(PROGRESS_TMP_PATH);
        } catch (IOException e) {
            System.out.println(ROLLBACK_FAILED);
            throw new RuntimeException(e);
        }
    }

    private static boolean isTransactionOff() {
        return Files.notExists(PROGRESS_TMP_PATH);
    }

    private static boolean isTransactionOn() {
        return Files.exists(PROGRESS_TMP_PATH);
    }

}
