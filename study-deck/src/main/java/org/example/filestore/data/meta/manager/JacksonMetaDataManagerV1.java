package org.example.filestore.data.meta.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.filestore.common.JsonMapper;
import org.example.filestore.shared.model.Counters;
import org.example.filestore.shared.model.Focus;
import org.example.filestore.shared.model.MetaDataModel;

import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.example.filestore.shared.Constant.*;
import static org.example.filestore.shared.PathConfig.META_DATA_TMP_PATH;
import static org.example.filestore.shared.PathConfig.META_DATA_WORK_PATH;

public class JacksonMetaDataManagerV1 implements MetaDataManager{

    private final ObjectMapper mapper = JsonMapper.getInstance();

    @Override
    public Focus getFocus() throws IOException {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);
        MetaDataModel metaData = mapper.readValue(META_DATA_TMP_PATH.toFile(), new TypeReference<MetaDataModel>() {});

        return metaData.focus();
    }

    @Override
    public Long nextCategoryId() throws IOException {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);
        MetaDataModel metaData = mapper.readValue(META_DATA_TMP_PATH.toFile(), new TypeReference<MetaDataModel>() {});

        long nextCategoryId = metaData.counters().nextCategoryId();
        MetaDataModel newMetaData = metaData.increaseNextCategoryId();
        mapper.writeValue(META_DATA_TMP_PATH.toFile(), newMetaData);

        return nextCategoryId;
    }

    @Override
    public void selectCategory(Long categoryId) throws IOException {
        MetaDataModel metaData = mapper.readValue(META_DATA_TMP_PATH.toFile(), new TypeReference<>(){});
        MetaDataModel newMetaData = metaData.changeCategoryFocus(categoryId);

        mapper.writeValue(META_DATA_TMP_PATH.toFile(), newMetaData);
    }

    @Override
    public Long currentCategory() throws IOException {
        MetaDataModel metaData = mapper.readValue(META_DATA_WORK_PATH.toFile(), new TypeReference<MetaDataModel>() {});
        return metaData.focus().categoryId();
    }

    @Override
    public void transaction() {
        if (isTransactionOn()) throw new IllegalStateException(ALREADY_STARTED_TRANSACTION);

        try {
            Files.copy(META_DATA_WORK_PATH, META_DATA_TMP_PATH, COPY_ATTRIBUTES);
        } catch (IOException e) {
            System.out.println(TRANSACTION_FAILED);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commit() {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        try {
            Files.move(META_DATA_TMP_PATH, META_DATA_WORK_PATH, REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(COMMIT_FAILED);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollback() {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        try {
            Files.delete(META_DATA_TMP_PATH);
        } catch (IOException e) {
            System.out.println(ROLLBACK_FAILED);
            throw new RuntimeException(e);
        }
    }

    private boolean isTransactionOn() {
        return Files.exists(META_DATA_TMP_PATH);
    }

    private boolean isTransactionOff() {
        return Files.notExists(META_DATA_TMP_PATH);
    }

    @Override
    public void init() throws IOException {
        Files.createFile(META_DATA_WORK_PATH);
        MetaDataModel metaData = new MetaDataModel(Focus.empty(), new Counters(0, 0, 0));
        mapper.writeValue(META_DATA_WORK_PATH.toFile(), metaData);
    }
}
