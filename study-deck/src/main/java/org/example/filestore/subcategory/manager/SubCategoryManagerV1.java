package org.example.filestore.subcategory.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.domain.subcategory.SubCategory;
import org.example.filestore.common.JsonMapper;
import org.example.filestore.subcategory.model.SubCategoryModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        Path path = isTransactionOn() ? SUBCATEGORY_TMP_PATH : SUBCATEGORY_WORK_PATH;
        return mapper.readValue(path.toFile(), new TypeReference<List<SubCategoryModel>>() {})
                .stream()
                .filter(s -> Objects.equals(s.parentCategoryId(), parentCategoryId))
                .toList();
    }

    @Override
    public Optional<SubCategoryModel> findById(Long subCategoryId) throws IOException {
        Path path = isTransactionOn() ? SUBCATEGORY_TMP_PATH : SUBCATEGORY_WORK_PATH;
        return mapper.readValue(path.toFile(), new TypeReference<List<SubCategoryModel>>() {
                })
                .stream()
                .filter(s -> Objects.equals(subCategoryId, s.id()))
                .findAny();
    }

    @Override
    public void save(SubCategoryModel model) throws IOException {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        List<SubCategoryModel> models = mapper.readValue(SUBCATEGORY_TMP_PATH.toFile(), new TypeReference<List<SubCategoryModel>>() {});

        models.add(model);

        mapper.writeValue(SUBCATEGORY_TMP_PATH.toFile(), models);
    }

    @Override
    public SubCategoryModel delete(Long subCategoryId) throws IOException {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        List<SubCategoryModel> models = mapper.readValue(SUBCATEGORY_TMP_PATH.toFile(), new TypeReference<List<SubCategoryModel>>() {});
        SubCategoryModel model = models.stream().filter(m -> Objects.equals(m.id(), subCategoryId)).findAny().orElseThrow();
        models.remove(model);
        mapper.writeValue(SUBCATEGORY_TMP_PATH.toFile(), models);

        return model;
    }

    @Override
    public void update(SubCategory subCategory) throws IOException {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);
        List<SubCategoryModel> models = mapper.readValue(SUBCATEGORY_TMP_PATH.toFile(), new TypeReference<List<SubCategoryModel>>() {});

        List<SubCategoryModel> updated = models.stream().map(
                m -> {
                    if (Objects.equals(m.id(), subCategory.getId())) {
                        return m.update(subCategory);
                    }
                    return m;
                }
        ).toList();

        mapper.writeValue(SUBCATEGORY_TMP_PATH.toFile(), updated);
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
