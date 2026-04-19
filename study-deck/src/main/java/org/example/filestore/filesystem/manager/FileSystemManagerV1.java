package org.example.filestore.filesystem.manager;

import org.example.filestore.data.DataManager;
import org.example.filestore.data.meta.manager.MetaDataManager;
import org.example.filestore.filesystem.naming.FileNameGenerator;
import org.example.filestore.filesystem.path.PathCalculator;
import org.example.filestore.shared.model.Focus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.example.filestore.shared.Constant.*;
import static org.example.filestore.shared.PathConfig.FILE_SYSTEM_TMP_PATH;

public class FileSystemManagerV1 implements FileSystemManager {

    private final MetaDataManager metaDataManager;
    private final PathCalculator pathCalculator;
    private final DataManager dataManager;
    private final FileNameGenerator fileNameGenerator;

    public FileSystemManagerV1(MetaDataManager metaDataManager, PathCalculator pathCalculator, DataManager dataManager, FileNameGenerator fileNameGenerator) {
        this.metaDataManager = metaDataManager;
        this.pathCalculator = pathCalculator;
        this.dataManager = dataManager;
        this.fileNameGenerator = fileNameGenerator;
    }


    @Override
    public String createCategoryFile() throws IOException {
        String fileName = fileNameGenerator.getFileName();
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        Path categoryDirPath = Path.of(FILE_SYSTEM_TMP_PATH.toString(), fileName);
        Files.createDirectory(categoryDirPath);

        return fileName;
    }

    @Override
    public void transaction() {
        if (isTransactionOn()) throw new IllegalStateException(ALREADY_STARTED_TRANSACTION);

        try {
            Files.createDirectory(FILE_SYSTEM_TMP_PATH);
        } catch (IOException e) {
            try {
                Files.deleteIfExists(FILE_SYSTEM_TMP_PATH);
            } catch (IOException ex) {
                System.out.println(TRANSACTION_FAILED);
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commit() {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        try {
            Focus focus = metaDataManager.getFocus();
            String filename = dataManager.getFileName(focus);
            Path target = pathCalculator.getPath(focus);

            Files.move(Path.of(FILE_SYSTEM_TMP_PATH.toString(), filename), target);
        } catch (IOException e) {
            System.out.println(COMMIT_FAILED);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollback() {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        try {
            // 내부에 파일이 있으면 DirectoryNotEmptyException 터지는데 이거 어떻게 해결할건지 생각해보기
            Files.delete(FILE_SYSTEM_TMP_PATH);
        } catch (IOException e) {
            System.out.println(ROLLBACK_FAILED);
            throw new RuntimeException(e);
        }
    }

    private boolean isTransactionOff() {
        return Files.notExists(FILE_SYSTEM_TMP_PATH);
    }

    private boolean isTransactionOn() {
        return Files.exists(FILE_SYSTEM_TMP_PATH);
    }
}
