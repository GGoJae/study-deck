package org.example.cli.excutor;

import org.example.cli.info.RequesterInfo;
import org.example.cli.model.command.Command;
import org.example.core.application.card.dto.request.AddAnswer;
import org.example.core.application.card.usecase.CardCommandUseCase;
import org.example.filestore.api.FileStoreApi;
import org.example.filestore.shared.model.AnswerSubmission;
import org.example.filestore.shared.model.Type;

import java.util.Objects;

public class SubmitCmdExecutor implements CommandExecutor{
    private final FileStoreApi fileStoreApi;
    private final CardCommandUseCase cardCommandUseCase;
    private final RequesterInfo requesterInfo;

    public SubmitCmdExecutor(FileStoreApi fileStoreApi, CardCommandUseCase cardCommandUseCase, RequesterInfo requesterInfo) {
        this.fileStoreApi = fileStoreApi;
        this.cardCommandUseCase = cardCommandUseCase;
        this.requesterInfo = requesterInfo;
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
        cardCommandUseCase.addAnswer(new AddAnswer(requesterId, context.snapshot().targetId(), context.answer()));
        fileStoreApi.deleteSession();
    }
}
