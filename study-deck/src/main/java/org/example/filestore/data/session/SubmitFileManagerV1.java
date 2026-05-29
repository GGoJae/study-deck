package org.example.filestore.data.session;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.filestore.common.JsonMapper;
import org.example.filestore.data.meta.manager.MetaDataManager;
import org.example.filestore.shared.model.AnswerSubmission;
import org.example.filestore.shared.model.Focus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.example.filestore.shared.PathConfig.SUBMIT_LOCATION_PATH;
import static org.example.filestore.shared.PathConfig.SUBMIT_WORK_PATH;

public class SubmitFileManagerV1 implements SubmitManager {

    private final MetaDataManager metaDataManager;
    private final ObjectMapper mapper = JsonMapper.getInstance();

    public SubmitFileManagerV1(MetaDataManager metaDataManager) {
        this.metaDataManager = metaDataManager;
    }

    @Override
    public AnswerSubmission getContext() throws IOException {
        Focus focusCapture = mapper.readValue(SUBMIT_LOCATION_PATH.toFile(), new TypeReference<Focus>() {
        });
        String answer = Files.readString(SUBMIT_WORK_PATH, StandardCharsets.UTF_8);

        return new AnswerSubmission(focusCapture, answer);
    }

    @Override
    public void finish() throws IOException {
        Files.deleteIfExists(SUBMIT_WORK_PATH);
        Files.deleteIfExists(SUBMIT_LOCATION_PATH);
    }

    @Override
    public boolean isWorkingOn() {
        return Files.exists(SUBMIT_WORK_PATH) || Files.exists(SUBMIT_LOCATION_PATH);
    }

    @Override
    public void init() throws IOException {
        if (Files.exists(SUBMIT_WORK_PATH) || Files.exists(SUBMIT_LOCATION_PATH)) {
            throw new IllegalStateException("이미 작업중 입니다.");
        }
        Files.createFile(SUBMIT_WORK_PATH);
        Files.createFile(SUBMIT_LOCATION_PATH);
        Focus focus = metaDataManager.currentFocus();
        mapper.writeValue(SUBMIT_LOCATION_PATH.toFile(), focus);
    }
}
