package org.example.filestore.card.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.filestore.card.model.CardModel;
import org.example.filestore.common.JsonMapper;
import org.example.filestore.filesystem.manager.FileSystemManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.example.filestore.shared.Constant.*;
import static org.example.filestore.shared.PathConfig.CARD_TMP_NAME;
import static org.example.filestore.shared.PathConfig.CARD_WORK_NAME;

public class CardManagerV1 implements CardManager {

    private final FileSystemManager fileSystemManager;
    private final ObjectMapper mapper = JsonMapper.getInstance();

    public CardManagerV1(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
    }

    @Override
    public void save(CardModel jsonModel) throws IOException {
        Path currentPath = fileSystemManager.currentPath();
        if (isTransactionOff(currentPath)) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        List<CardModel> cardModels = getCardsAtTmp(currentPath);
        cardModels.add(jsonModel);
        mapper.writeValue(currentPath.resolve(CARD_TMP_NAME).toFile(), cardModels);
    }

    @Override
    public void update(CardModel updated) throws IOException {
        Path currentPath = fileSystemManager.currentPath();
        if (isTransactionOff(currentPath)) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        List<CardModel> cards = getCardsAtTmp(currentPath);
        List<CardModel> afterUpdate = cards.stream().map(c -> {
            if (Objects.equals(c.id(), updated.id())) return updated;
            return c;
        }).toList();

        mapper.writeValue(currentPath.resolve(CARD_TMP_NAME).toFile(), afterUpdate);
    }

    @Override
    public void delete(Long cardId) throws IOException {
        Path currentPath = fileSystemManager.currentPath();
        if (isTransactionOff(currentPath)) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        List<CardModel> cards = getCardsAtTmp(currentPath);
        List<CardModel> deleted = cards.stream().filter(c -> !Objects.equals(c.id(), cardId)).toList();

        mapper.writeValue(currentPath.resolve(CARD_TMP_NAME).toFile(), deleted);
    }

    @Override
    public Optional<CardModel> findById(Long cardId) throws IOException {
        Path currentPath = fileSystemManager.currentPath();
        List<CardModel> cardModels = getCardsAtWork(currentPath);
        return cardModels.stream()
                .filter(c -> Objects.equals(c.id(), cardId))
                .findAny();
    }

    @Override
    public List<CardModel> findBySubCategoryId(Long subCategoryId) throws IOException {
        Path currentPath = fileSystemManager.currentPath();
        List<CardModel> cardModels = getCardsAtWork(currentPath);
        return cardModels.stream()
                .filter(c -> Objects.equals(c.subCategoryId(), subCategoryId))
                .toList();
    }

    @Override
    public void init(Path path) throws IOException {
        Path resolve = Files.createFile(path.resolve(CARD_WORK_NAME));
        mapper.writeValue(resolve.toFile(), List.of());
    }

    @Override
    public void transaction() {
        try {
            Path currentPath = fileSystemManager.currentPath();
            if (isTransactionOn(currentPath)) throw new IllegalStateException(ALREADY_STARTED_TRANSACTION);
            Files.copy(currentPath.resolve(CARD_WORK_NAME), currentPath.resolve(CARD_TMP_NAME), COPY_ATTRIBUTES);
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
            Files.move(currentPath.resolve(CARD_TMP_NAME), currentPath.resolve(CARD_WORK_NAME), REPLACE_EXISTING);
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
            Files.delete(currentPath.resolve(CARD_TMP_NAME));
        } catch (IOException e) {
            System.out.println(ROLLBACK_FAILED);
            throw new RuntimeException(e);
        }
    }

    private boolean isTransactionOff(Path currentPath) {
        return !this.isTransactionOn(currentPath);
    }

    private boolean isTransactionOn(Path currentPath) {
        return Files.exists(currentPath.resolve(CARD_TMP_NAME));
    }

    private List<CardModel> getCardsAtTmp(Path currentPath) throws IOException {
        return mapper.readValue(currentPath.resolve(CARD_TMP_NAME).toFile(), new TypeReference<List<CardModel>>() {
        });
    }

    private List<CardModel> getCardsAtWork(Path currentPath) throws IOException {
        return mapper.readValue(currentPath.resolve(CARD_WORK_NAME).toFile(), new TypeReference<List<CardModel>>() {
        });
    }
}
