package org.example.filestore.api;

import org.example.filestore.category.manager.CategoryManager;
import org.example.filestore.data.meta.manager.MetaDataManager;
import org.example.filestore.filesystem.manager.FileSystemManager;

import java.io.IOException;
import java.nio.file.Files;

import static org.example.filestore.shared.PathConfig.WORKING_DIR;

public class FileStoreApi {

    private final CategoryManager categoryManager;
    private final FileSystemManager fileSystemManager;
    private final MetaDataManager metaDataManager;

    public FileStoreApi(CategoryManager categoryManager, FileSystemManager fileSystemManager, MetaDataManager metaDataManager) {
        this.categoryManager = categoryManager;
        this.fileSystemManager = fileSystemManager;
        this.metaDataManager = metaDataManager;
    }

    public void fileStoreInit() {
        try {

        if (Files.exists(WORKING_DIR)) {
            System.out.println("이미 .deck 디렉토리가 존재합니다.");
            return;
        }


            Files.createDirectory(WORKING_DIR);
            metaDataManager.init();
            categoryManager.init();
            fileSystemManager.init();
            System.out.println("FileStore 생성이 완료됐습니다.");
        } catch (IOException e) {
            System.out.println("FileStore 생성에 실패했습니다.");
            throw new RuntimeException(e);
        }
    }

    public Long currentCategory() {
        try {
            return metaDataManager.currentCategory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
