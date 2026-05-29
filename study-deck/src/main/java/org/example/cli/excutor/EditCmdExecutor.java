package org.example.cli.excutor;

import org.example.cli.model.command.Command;
import org.example.cli.output.Output;
import org.example.filestore.api.FileStoreApi;

import java.util.Objects;

public class EditCmdExecutor implements CommandExecutor{

    public EditCmdExecutor(FileStoreApi fileStoreApi, Output output) {
        this.fileStoreApi = fileStoreApi;
        this.output = output;
    }

    private final FileStoreApi fileStoreApi;
    private final Output output;

    @Override
    public String canResolvedCommand() {
        return "edit";
    }

    @Override
    public void execute(Command command) {
        Objects.requireNonNull(command);
        String cmd = command.cmd();
        Objects.requireNonNull(cmd);

        if (command.hasArgument() || command.hasOptions()) {
            // TODO  Arg, Option 필요없다는 안내
            return;
        }

        fileStoreApi.editAnswer();
    }
}
