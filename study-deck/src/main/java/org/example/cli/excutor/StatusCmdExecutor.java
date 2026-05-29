package org.example.cli.excutor;

import org.example.cli.info.RequesterInfo;
import org.example.cli.model.command.Command;
import org.example.cli.model.display.Status;
import org.example.cli.output.Output;
import org.example.core.application.card.dto.response.CardCapture;
import org.example.core.application.card.usecase.CardQueryUseCase;
import org.example.core.application.category.dto.response.CategoryCapture;
import org.example.core.application.category.usecase.CategoryQueryUseCase;
import org.example.core.application.subcategory.dto.response.SubCategoryCapture;
import org.example.core.application.subcategory.usecase.SubCategoryQueryUseCase;
import org.example.filestore.api.FileStoreApi;
import org.example.filestore.shared.model.Type;

import java.util.Objects;

public class StatusCmdExecutor implements CommandExecutor {

    private final FileStoreApi fileStoreApi;
    private final Output output;
    private final RequesterInfo requesterInfo;
    private final CategoryQueryUseCase categoryQueryUseCase;
    private final SubCategoryQueryUseCase subCategoryQueryUseCase;
    private final CardQueryUseCase cardQueryUseCase;

    public StatusCmdExecutor(FileStoreApi fileStoreApi, Output output, RequesterInfo requesterInfo, CategoryQueryUseCase categoryQueryUseCase, SubCategoryQueryUseCase subCategoryQueryUseCase, CardQueryUseCase cardQueryUseCase) {
        this.fileStoreApi = fileStoreApi;
        this.output = output;
        this.requesterInfo = requesterInfo;
        this.categoryQueryUseCase = categoryQueryUseCase;
        this.subCategoryQueryUseCase = subCategoryQueryUseCase;
        this.cardQueryUseCase = cardQueryUseCase;
    }

    @Override
    public String canResolvedCommand() {
        return "status";
    }

    @Override
    public void execute(Command command) {
        Objects.requireNonNull(command);
        Objects.requireNonNull(command.cmd());

        Long requesterId = requesterInfo.id();
        Long categoryId = fileStoreApi.currentCategory().orElse(null);
        Long subCategoryId = fileStoreApi.currentSubCategory().orElse(null);
        Long cardId = fileStoreApi.currentCard().orElse(null);

        // TODO NOTE 생기면 분기

        Status status = Status.builder()
                .categoryName(
                        categoryId,
                        id -> categoryQueryUseCase.getCategoryForDisplay(requesterId, id).map(CategoryCapture::name).orElseThrow()
                )
                .subCategoryName(
                        subCategoryId,
                        id -> subCategoryQueryUseCase.getSubCategory(requesterId, id).map(SubCategoryCapture::name).orElseThrow()
                )
                .content(
                        Type.CARD,
                        cardId,
                        id -> cardQueryUseCase.getCard(requesterId, id).map(CardCapture::displayName).orElseThrow()
                ).build();

        output.showStatus(status);
    }
}
