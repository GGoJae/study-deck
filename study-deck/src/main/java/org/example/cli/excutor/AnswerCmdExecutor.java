package org.example.cli.excutor;

import org.example.cli.info.RequesterInfo;
import org.example.cli.model.command.Command;
import org.example.cli.model.display.Content;
import org.example.cli.output.Output;
import org.example.core.application.card.usecase.CardQueryUseCase;
import org.example.filestore.api.FileStoreApi;

import java.util.Objects;
import java.util.Optional;

public class AnswerCmdExecutor implements CommandExecutor {

    private final FileStoreApi fileStoreApi;
    private final CardQueryUseCase cardQueryUseCase;
    private final Output output;
    private final RequesterInfo requesterInfo;

    public AnswerCmdExecutor(FileStoreApi fileStoreApi, CardQueryUseCase cardQueryUseCase, Output output, RequesterInfo requesterInfo) {
        this.fileStoreApi = fileStoreApi;
        this.cardQueryUseCase = cardQueryUseCase;
        this.output = output;
        this.requesterInfo = requesterInfo;
    }

    @Override
    public String canResolvedCommand() {
        return "answer";
    }

    @Override
    public void execute(Command command) {
        Objects.requireNonNull(command);
        Objects.requireNonNull(command.cmd());

        Long requesterId = requesterInfo.id();
        Optional<Long> optCardId = fileStoreApi.currentCard();

        if (optCardId.isEmpty()) {
            output.errorMessage("카드가 선택되지 않았습니다.");
            return;
        }

        Long cardId = optCardId.orElseThrow();

        if (!command.hasArgument() && !command.hasOptions()) {
            cardQueryUseCase.getBestAnswer(requesterId, cardId).ifPresentOrElse(
                    a -> {
                        output.showContent(new Content(a.id(), "card [ " + a.cardId() + " ] 에 대한 best 답변", a.answer()));
                    },
                    () -> output.errorMessage("베스트 답변이 없습니다.")
            );
            return;
        }
    }
}
