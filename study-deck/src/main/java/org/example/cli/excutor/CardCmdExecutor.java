package org.example.cli.excutor;

import org.example.cli.info.RequesterInfo;
import org.example.cli.model.command.Command;
import org.example.cli.model.command.Option;
import org.example.cli.output.Output;
import org.example.core.application.card.dto.request.CreateCard;
import org.example.core.application.card.dto.response.CardCapture;
import org.example.core.application.card.usecase.CardCommandUseCase;
import org.example.core.application.card.usecase.CardQueryUseCase;
import org.example.filestore.api.FileStoreApi;
import org.example.filestore.shared.model.Type;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CardCmdExecutor implements CommandExecutor{

    private final Output output;
    private final CardCommandUseCase commandUseCase;
    private final CardQueryUseCase queryUseCase;
    private final FileStoreApi fileStoreApi;
    private final RequesterInfo requesterInfo;

    public CardCmdExecutor(Output output, CardCommandUseCase commandUseCase, CardQueryUseCase queryUseCase, FileStoreApi fileStoreApi, RequesterInfo requesterInfo) {
        this.output = output;
        this.commandUseCase = commandUseCase;
        this.queryUseCase = queryUseCase;
        this.fileStoreApi = fileStoreApi;
        this.requesterInfo = requesterInfo;
    }

    @Override
    public String canResolvedCommand() {
        return "card";
    }

    @Override
    public void execute(Command command) {
        Objects.requireNonNull(command);
        Objects.requireNonNull(command.cmd());
        if (!Objects.equals(command.cmd(), canResolvedCommand())) {
            throw new IllegalStateException();
        }

        Long requesterId = requesterInfo.id();
        Optional<Long> optSubCategoryId = fileStoreApi.currentSubCategory();
        if (optSubCategoryId.isEmpty()) {
            output.errorMessage("서브카테고리가 선택되지 않았습니다.");
            return;
        }

        Long currentSubCategory = optSubCategoryId.orElseThrow();

        if (!command.hasArgument() && !command.hasOptions()) {
            showCards(requesterId, currentSubCategory);
            return;
        }

        Option option = command.options().get(0);
        if ("-s".equals(option.value())) {
            selectCard(option);
            return;
        }

        if (!command.hasArgument()) {
            output.errorMessage("card 생성시 display name 은 필수입니다.");
            return;
        } else if (!command.hasOptions()) {
            output.errorMessage("card 생성시 question 은 필수입니다.");
            return;
        }

        String displayName = command.arguments().get(0);

        if ("-q".equals(option.value())) {
            createCard(option, requesterId, currentSubCategory, displayName);
            return;
        }


        output.errorMessage("명령어를 확인해주세요.");

    }

    private void selectCard(Option option) {
        long cardId = Long.parseLong(option.arguments().get(0));
        fileStoreApi.selectCard(cardId);
    }

    private void createCard(Option option, Long requesterId, Long currentSubCategory, String displayName) {
        if (!option.hasArgument()) {
            output.errorMessage("card 생성시 question 은 필수입니다.");
        }
        Long cardId = commandUseCase.create(new CreateCard(requesterId, currentSubCategory, displayName, option.arguments().get(0)));
        fileStoreApi.selectCard(cardId);
    }

    private void showCards(Long requesterId, Long currentSubCategory) {
        List<CardCapture> cards = queryUseCase.getOwnCardsForDisplay(requesterId, currentSubCategory);
        Long currentCard = fileStoreApi.currentCard().orElse(null);

        output.showCards(cards, currentCard);
    }

}
