package org.example.filestore.subcategory.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.filestore.common.JsonMapper;
import org.example.filestore.subcategory.model.SubCategoryModel;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.example.filestore.shared.Constant.*;
import static org.example.filestore.shared.PathConfig.SUBCATEGORY_TMP_PATH;
import static org.example.filestore.shared.PathConfig.SUBCATEGORY_WORK_PATH;

public class SubCategoryManagerV1 implements SubCategoryManager {

    private final ObjectMapper mapper = JsonMapper.getInstance();
    @Override
    public List<SubCategoryModel> findByParentCategoryId(Long parentCategoryId) throws IOException {
        return mapper.readValue(SUBCATEGORY_WORK_PATH.toFile(), new TypeReference<List<SubCategoryModel>>() {})
                .stream()
                .filter(s -> Objects.equals(s.parentCategoryId(), parentCategoryId))
                .toList();
    }

    @Override
    public Optional<SubCategoryModel> findById(Long subCategoryId) throws IOException {
        return mapper.readValue(SUBCATEGORY_WORK_PATH.toFile(), new TypeReference<List<SubCategoryModel>>() {
                })
                .stream()
                .filter(s -> Objects.equals(subCategoryId, s.id()))
                .findAny();
    }

    @Override
    public void transaction() {
        if (isTransactionOn()) throw new IllegalStateException(ALREADY_STARTED_TRANSACTION);

        try {
            Files.copy(SUBCATEGORY_WORK_PATH, SUBCATEGORY_TMP_PATH, COPY_ATTRIBUTES);
        } catch (IOException e) {
            System.out.println(TRANSACTION_FAILED);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commit() {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        try {
            Files.move(SUBCATEGORY_TMP_PATH, SUBCATEGORY_WORK_PATH, REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(COMMIT_FAILED);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollback() {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        try {
            Files.delete(SUBCATEGORY_TMP_PATH);
        } catch (IOException e) {
            System.out.println(ROLLBACK_FAILED);
            throw new RuntimeException(e);
        }
    }

    private static boolean isTransactionOff() {
        return Files.notExists(SUBCATEGORY_TMP_PATH);
    }

    private static boolean isTransactionOn() {
        return Files.exists(SUBCATEGORY_TMP_PATH);
    }

    @Override
    public void init() throws IOException {
        Files.createFile(SUBCATEGORY_WORK_PATH);
        mapper.writeValue(SUBCATEGORY_WORK_PATH.toFile(), List.of());
    }
}
