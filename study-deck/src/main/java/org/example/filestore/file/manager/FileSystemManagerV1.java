package org.example.filestore.file.manager;

import org.example.core.category.domain.Category;
import org.example.filestore.data.DataManager;
import org.example.filestore.data.meta.manager.MetaDataManager;
import org.example.filestore.file.path.PathCalculator;
import org.example.filestore.shared.model.CategoryModel;
import org.example.filestore.shared.model.Focus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.example.filestore.shared.Constant.WORKING_DIR;

public class FileSystemManagerV1 implements FileSystemManager {

    private final MetaDataManager metaDataManager;
    private final PathCalculator pathCalculator;
    private final DataManager dataManager;
    private Path tmpPath = Path.of(WORKING_DIR.toString(), "tmp");

    public FileSystemManagerV1(MetaDataManager metaDataManager, PathCalculator pathCalculator, DataManager dataManager) {
        this.metaDataManager = metaDataManager;
        this.pathCalculator = pathCalculator;
        this.dataManager = dataManager;
    }


    @Override
    public CategoryModel createCategoryFile(Category category) throws IOException {
        return null;
    }

    @Override
    public void transaction() {
        if (Files.exists(tmpPath)) {
            throw new IllegalStateException("이미 작업 중입니다.");
        }

        try {
            Files.createDirectory(tmpPath);
        } catch (IOException e) {
            try {
                Files.deleteIfExists(tmpPath);
            } catch (IOException ex) {
                System.out.println("transaction 실패");
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commit() {
        if (Files.notExists(tmpPath)) {
            throw new IllegalStateException("transaction 이 시작되지 않았습니다.");
        }

        Focus focus = metaDataManager.getFocus();
        String filename = dataManager.getFileName(focus);
        Path target = pathCalculator.getPath(focus);

        try {
            Files.move(Path.of(tmpPath.toString(), filename), target);
        } catch (IOException e) {
            System.out.println("commit 실패");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollback() {
        if (Files.notExists(tmpPath)){
            throw new IllegalStateException("transaction 이 시작되지 않았습니다.");
        }

        try {
            // 내부에 파일이 있으면 DirectoryNotEmptyException 터지는데 이거 어떻게 해결할건지 생각해보기
            Files.delete(tmpPath);
        } catch (IOException e) {
            System.out.println("rollback 실패");
            throw new RuntimeException(e);
        }
    }
}
