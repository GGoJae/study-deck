package org.example.filestore.api;

import org.example.filestore.card.manager.CardManager;
import org.example.filestore.card.model.CardModel;
import org.example.filestore.category.manager.CategoryManager;
import org.example.filestore.data.meta.manager.MetaDataManager;
import org.example.filestore.data.session.SubmitManager;
import org.example.filestore.filesystem.manager.FileSystemManager;
import org.example.filestore.progress.manager.ProgressManager;
import org.example.filestore.shared.model.AnswerSubmission;
import org.example.filestore.subcategory.manager.SubCategoryManager;
import org.example.filestore.subcategory.model.SubCategoryModel;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Optional;

import static org.example.filestore.shared.PathConfig.WORKING_DIR;

public class FileStoreApi {

    private final CategoryManager categoryManager;
    private final SubCategoryManager subCategoryManager;
    private final CardManager cardManager;
    private final FileSystemManager fileSystemManager;
    private final MetaDataManager metaDataManager;
    private final SubmitManager submitManager;
    private final ProgressManager progressManager;

    public FileStoreApi(CategoryManager categoryManager, SubCategoryManager subCategoryManager, CardManager cardManager, FileSystemManager fileSystemManager, MetaDataManager metaDataManager, SubmitManager submitManager, ProgressManager progressManager) {
        this.categoryManager = categoryManager;
        this.subCategoryManager = subCategoryManager;
        this.cardManager = cardManager;
        this.fileSystemManager = fileSystemManager;
        this.metaDataManager = metaDataManager;
        this.submitManager = submitManager;
        this.progressManager = progressManager;
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
            subCategoryManager.init();
            fileSystemManager.init();

            System.out.println("FileStore 생성이 완료됐습니다.");
        } catch (IOException e) {
            System.out.println("FileStore 생성에 실패했습니다.");
            throw new RuntimeException(e);
        }
    }

    public Optional<Long> currentCategory() {
        try {
            return metaDataManager.currentCategory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeCurrentCategory(long categoryId) {
        try {
            metaDataManager.transaction();
            metaDataManager.selectCategory(categoryId);
            metaDataManager.commit();
        } catch (IOException e) {
            metaDataManager.rollback();
            throw new RuntimeException(e);
        }
    }

    public Optional<Long> currentSubCategory() {
        try {
            return metaDataManager.currentSubCategory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Long> currentCard() {
        try {
            if (!metaDataManager.isCurrentContentCard()) {
                return Optional.empty();
            }
            return metaDataManager.contentId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeCurrentSubCategory(long subCategoryId) {
        try {
            metaDataManager.transaction();

            SubCategoryModel subCategory = subCategoryManager.findById(subCategoryId).orElseThrow();
            Long currentCategory = metaDataManager.currentCategory().orElseThrow();
            if (!Objects.equals(subCategory.parentCategoryId(), currentCategory)) {
                throw new IllegalStateException();
            }

            metaDataManager.selectSubCategory(subCategoryId);

            metaDataManager.commit();
        } catch (IOException e) {
            metaDataManager.rollback();
            throw new RuntimeException(e);
        }
    }

    public void selectCard(long cardId) {
        try {
            metaDataManager.transaction();

            CardModel cardModel = cardManager.findById(cardId).orElseThrow();
            Long currentSubCategory = metaDataManager.currentSubCategory().orElseThrow();
            if (!Objects.equals(currentSubCategory, cardModel.subCategoryId())) {
                throw new IllegalStateException();
            }

            metaDataManager.selectCard(cardId);

            metaDataManager.commit();
        } catch (IOException e) {
            metaDataManager.rollback();
            throw new RuntimeException(e);
        }
    }

    public void editAnswer() {
        try {
            submitManager.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AnswerSubmission getContext() {
        try {
            return submitManager.getContext();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteSession() {
        try {
            submitManager.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
