package org.example.filestore.category.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.category.domain.Category;
import org.example.filestore.common.JsonMapper;
import org.example.filestore.category.model.CategoryModel;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.*;
import static org.example.filestore.shared.Constant.*;
import static org.example.filestore.shared.PathConfig.CATEGORY_TMP_PATH;
import static org.example.filestore.shared.PathConfig.CATEGORY_WORK_PATH;


public class CategoryManagerV1 implements CategoryManager{

    private final ObjectMapper mapper = JsonMapper.getInstance();

    @Override
    public void save(CategoryModel categoryModel) throws IOException {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        List<CategoryModel> models = mapper.readValue(CATEGORY_TMP_PATH.toFile(), new TypeReference<>() {});

        models.add(categoryModel);

        mapper.writeValue(CATEGORY_TMP_PATH.toFile(), models);
    }

    @Override
    public String getFilename(Long categoryId) throws IOException {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        List<CategoryModel> models = mapper.readValue(CATEGORY_TMP_PATH.toFile(), new TypeReference<>() {});

        return models.stream().filter(c -> c.id().equals(categoryId)).map(CategoryModel::fileName).findAny().orElse(null);
    }

    @Override
    public Optional<CategoryModel> findById(Long categoryId) throws IOException {
        return mapper.readValue(CATEGORY_WORK_PATH.toFile(), new TypeReference<List<CategoryModel>>() {})
                .stream()
                .filter(c -> categoryId.equals(c.id()))
                .findAny();
    }

    @Override
    public List<CategoryModel> findByOwnerId(Long ownerId, int offset, int limit) throws IOException {
        List<CategoryModel> models = mapper.readValue(CATEGORY_WORK_PATH.toFile(), new TypeReference<List<CategoryModel>>() {
                })
                .stream()
                .sorted(Comparator.comparingInt(CategoryModel::sortKey))
                .toList();

        if (offset >= models.size()) return List.of();
        limit = Math.min(limit, models.size());

        return models.subList(offset, offset + limit);
    }

    @Override
    public void transaction() {
        if (isTransactionOn()) throw new IllegalStateException(ALREADY_STARTED_TRANSACTION);

        try {
            Files.copy(CATEGORY_WORK_PATH, CATEGORY_TMP_PATH, COPY_ATTRIBUTES);
        } catch (IOException e) {
            System.out.println(TRANSACTION_FAILED);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commit() {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        try {
            Files.move(CATEGORY_TMP_PATH, CATEGORY_WORK_PATH, REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(COMMIT_FAILED);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollback() {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        try {
            Files.delete(CATEGORY_TMP_PATH);
        } catch (IOException e) {
            System.out.println(ROLLBACK_FAILED);
            throw new RuntimeException(e);
        }
    }

    private static boolean isTransactionOff() {
        return Files.notExists(CATEGORY_TMP_PATH);
    }

    private static boolean isTransactionOn() {
        return Files.exists(CATEGORY_TMP_PATH);
    }

    @Override
    public void init() throws IOException {
        Files.createFile(CATEGORY_WORK_PATH);
        mapper.writeValue(CATEGORY_WORK_PATH.toFile(), List.of());
    }
}
