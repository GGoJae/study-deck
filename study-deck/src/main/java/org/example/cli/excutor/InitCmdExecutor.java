package org.example.cli.excutor;

import org.example.cli.model.command.Command;
import org.example.filestore.api.FileStoreApi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class InitCmdExecutor implements CommandExecutor{
    private static final String RESOLVED_COMMAND = "init";
    private final FileStoreApi fileStoreApi;

    public InitCmdExecutor(FileStoreApi fileStoreApi) {
        this.fileStoreApi = fileStoreApi;
    }

    @Override
    public String canResolvedCommand() {
        return RESOLVED_COMMAND;
    }

    @Override
    public void execute(Command command) {
        createDeckDir();
    }

    private void createDeckDir() {
        fileStoreApi.fileStoreInit();
    }
}
