package org.example.cli.excutor;

import org.example.cli.info.RequesterInfo;
import org.example.cli.model.command.Command;
import org.example.cli.output.Output;
import org.example.core.application.card.dto.request.AddAnswer;
import org.example.core.application.card.dto.request.UpdateBestAnswer;
import org.example.core.application.card.dto.response.CardCapture;
import org.example.core.application.card.usecase.CardCommandUseCase;
import org.example.core.application.card.usecase.CardQueryUseCase;
import org.example.filestore.api.FileStoreApi;
import org.example.filestore.shared.model.AnswerSubmission;
import org.example.filestore.shared.model.Type;

import java.util.Objects;

public class SubmitCmdExecutor implements CommandExecutor{
    private final FileStoreApi fileStoreApi;
    private final CardCommandUseCase cardCommandUseCase;
    private final CardQueryUseCase cardQueryUseCase;
    private final RequesterInfo requesterInfo;
    private final Output output;

    public SubmitCmdExecutor(FileStoreApi fileStoreApi, CardCommandUseCase cardCommandUseCase, CardQueryUseCase cardQueryUseCase, RequesterInfo requesterInfo, Output output) {
        this.fileStoreApi = fileStoreApi;
        this.cardCommandUseCase = cardCommandUseCase;
        this.cardQueryUseCase = cardQueryUseCase;
        this.requesterInfo = requesterInfo;
        this.output = output;
    }

    @Override
    public String canResolvedCommand() {
        return "submit";
    }

    @Override
    public void execute(Command command) {
        Objects.requireNonNull(command);
        Objects.requireNonNull(command.cmd());

        Long requesterId = requesterInfo.id();
        AnswerSubmission context = fileStoreApi.getContext();
        if (!Objects.equals(context.snapshot().targetType(), Type.CARD)) {
            throw new IllegalStateException();
        }

        Long answerId = cardCommandUseCase.addAnswer(new AddAnswer(requesterId, context.snapshot().targetId(), context.answer()));
        CardCapture card = cardQueryUseCase.getCard(requesterId, context.snapshot().targetId()).orElseThrow();
        if (Objects.isNull(card.bestAnswer())) {
            cardCommandUseCase.updateBestAnswer(new UpdateBestAnswer(requesterId, card.id(), answerId));
        }

        fileStoreApi.deleteSession();
    }
}
