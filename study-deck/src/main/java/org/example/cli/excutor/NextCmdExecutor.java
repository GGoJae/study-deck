package org.example.cli.excutor;

import org.example.cli.info.RequesterInfo;
import org.example.cli.model.command.Command;
import org.example.cli.model.display.Content;
import org.example.cli.output.Output;
import org.example.core.application.progress.dto.response.CardForDeck;
import org.example.core.application.progress.usecase.PopCardUseCase;
import org.example.core.application.progress.vo.PopStrategy;
import org.example.filestore.api.FileStoreApi;

import java.util.Objects;

public class NextCmdExecutor implements CommandExecutor{

    private final PopCardUseCase popCardUseCase;
    private final FileStoreApi fileStoreApi;
    private final RequesterInfo requesterInfo;
    private final Output output;

    public NextCmdExecutor(PopCardUseCase popCardUseCase, FileStoreApi fileStoreApi, RequesterInfo requesterInfo, Output output) {
        this.popCardUseCase = popCardUseCase;
        this.fileStoreApi = fileStoreApi;
        this.requesterInfo = requesterInfo;
        this.output = output;
    }

    @Override
    public String canResolvedCommand() {
        return "next";
    }

    @Override
    public void execute(Command command) {
        Objects.requireNonNull(command);
        Objects.requireNonNull(command.cmd());

        Long subCategoryId = fileStoreApi.currentSubCategory().orElseThrow(() -> new IllegalStateException("서브 카테고리가 선택되지 않았습니다."));
        if (!command.hasArgument() && !command.hasOptions()) {
            oldestStrategy(subCategoryId);
            return;
        }

    }

    private void oldestStrategy(Long subCategoryId) {
        CardForDeck cardForDeck = popCardUseCase.popNextCard(requesterInfo.id(), subCategoryId, PopStrategy.OLDEST);
        fileStoreApi.selectCard(cardForDeck.cardId());
        output.showContent(new Content(cardForDeck.cardId(), cardForDeck.displayName(), cardForDeck.question()));
    }
}
