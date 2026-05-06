package org.example.filestore.filesystem.manager;

import org.apache.commons.io.FileUtils;
import org.example.filestore.data.DataManager;
import org.example.filestore.data.meta.manager.MetaDataManager;
import org.example.filestore.filesystem.naming.FileNameGenerator;
import org.example.filestore.filesystem.path.PathCalculator;
import org.example.filestore.shared.model.Focus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.example.filestore.shared.Constant.*;
import static org.example.filestore.shared.PathConfig.FILE_SYSTEM_TMP_PATH;
import static org.example.filestore.shared.PathConfig.FILE_SYSTEM_WORK_PATH;

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
        String filename = fileNameGenerator.getFileName();
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        Path categoryDirPath = FILE_SYSTEM_TMP_PATH.resolve(filename);
        Files.createDirectory(categoryDirPath);

        return filename;
    }

    @Override
    public void deleteCategory(String filename) throws IOException {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        Path target = FILE_SYSTEM_TMP_PATH.resolve(filename);
        FileUtils.deleteDirectory(target.toFile());
    }

    @Override
    public void transaction() {
        if (isTransactionOn()) throw new IllegalStateException(ALREADY_STARTED_TRANSACTION);
        try {
            FileUtils.copyDirectory(FILE_SYSTEM_WORK_PATH.toFile(), FILE_SYSTEM_TMP_PATH.toFile());
        } catch (IOException e) {
            System.out.println(TRANSACTION_FAILED);
            try {
                FileUtils.deleteDirectory(FILE_SYSTEM_TMP_PATH.toFile());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commit() {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);
        Path backupPath = FILE_SYSTEM_WORK_PATH.resolveSibling("work_bak");
        try {
            if (Files.exists(FILE_SYSTEM_WORK_PATH)) {
                Files.move(FILE_SYSTEM_WORK_PATH, backupPath, StandardCopyOption.REPLACE_EXISTING);
            }

            Files.move(FILE_SYSTEM_TMP_PATH, FILE_SYSTEM_WORK_PATH, StandardCopyOption.ATOMIC_MOVE);

            FileUtils.deleteQuietly(backupPath.toFile());
        } catch (IOException e) {
            System.out.println(COMMIT_FAILED);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollback() {
        if (isTransactionOff()) throw new IllegalStateException(NOT_STARTED_TRANSACTION);

        try {
            FileUtils.deleteDirectory(FILE_SYSTEM_TMP_PATH.toFile());
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

    @Override
    public void init() throws IOException {
        Files.createDirectory(FILE_SYSTEM_WORK_PATH);
    }
}
