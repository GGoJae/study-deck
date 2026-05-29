package org.example.cli.excutor;

import org.example.cli.model.command.Command;
import org.example.filestore.api.FileStoreApi;

import java.util.Objects;

public class DiscardCmdExecutor implements CommandExecutor{

    private final FileStoreApi fileStoreApi;

    public DiscardCmdExecutor(FileStoreApi fileStoreApi) {
        this.fileStoreApi = fileStoreApi;
    }

    @Override
    public String canResolvedCommand() {
        return "discard";
    }

    @Override
    public void execute(Command command) {
        Objects.requireNonNull(command);
        Objects.requireNonNull(command.cmd());

        fileStoreApi.deleteSession();
    }
}
