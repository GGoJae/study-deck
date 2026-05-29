package org.example.cli.excutor;

import org.example.cli.model.command.Command;
import org.example.cli.output.Output;
import org.example.filestore.api.FileStoreApi;

import java.util.Objects;

public class UseCmdExecutor implements CommandExecutor {

    private final FileStoreApi fileStoreApi;
    private final Output output;

    public UseCmdExecutor(FileStoreApi fileStoreApi, Output output) {
        this.fileStoreApi = fileStoreApi;
        this.output = output;
    }

    @Override
    public String canResolvedCommand() {
        return "use";
    }

    @Override
    public void execute(Command command) {
        Objects.requireNonNull(command);
        Objects.requireNonNull(command.cmd());

        if (fileStoreApi.isWorkingOn()) {
            output.errorMessage("현재 작업중이라 이동할 수 없습니다. \n작업 취소 : deck discard");
            return;
        }

        if (command.arguments().size() != 2) {
            output.errorMessage("use 의 인자는 (1)target, (2)targetId 입니다.");
            return;
        }

        try {
            String target = command.arguments().get(0);
            long targetId = Long.parseLong(command.arguments().get(1));

            if ("category".equals(target)) {
                fileStoreApi.changeCurrentCategory(targetId);
            } else if ("sub".equals(target)) {
                fileStoreApi.changeCurrentSubCategory(targetId);
            } else if ("card".equals(target)) {
                fileStoreApi.selectCard(targetId);
            } else {
                output.errorMessage("커맨드를 확인해주세요.");
            }
        } catch (NumberFormatException nfe) {
            output.errorMessage("category id 는 숫자 형식이어야 합니다.");
        }

    }
}
