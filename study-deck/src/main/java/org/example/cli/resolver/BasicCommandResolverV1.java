package org.example.cli.resolver;

import org.example.cli.excutor.CatCmdExecutor;
import org.example.cli.excutor.CommandExecutor;
import org.example.cli.excutor.DefaultCmdExecutor;
import org.example.cli.excutor.InitCmdExecutor;
import org.example.cli.model.command.Command;
import org.example.cli.resolver.parser.CommandParser;
import org.example.core.category.port.in.CategoryCommandPort;
import org.example.filestore.api.FileStoreApi;

import java.util.HashMap;
import java.util.Map;

public class BasicCommandResolverV1 implements CommandResolver{

    public BasicCommandResolverV1(CommandParser commandParser, CategoryCommandPort categoryCommandPort, FileStoreApi fileStoreApi) {
        this.commandParser = commandParser;
        executorMapInit(fileStoreApi, categoryCommandPort);
    }

    private void executorMapInit(FileStoreApi fileStoreApi, CategoryCommandPort categoryCommandPort) {
        InitCmdExecutor initCmdExecutor = new InitCmdExecutor(fileStoreApi);
        CatCmdExecutor catCmdExecutor = new CatCmdExecutor(categoryCommandPort);

        executorMap.put(initCmdExecutor.canResolvedCommand(), initCmdExecutor);
        executorMap.put(catCmdExecutor.canResolvedCommand(), catCmdExecutor);
    }

    private final CommandParser commandParser;

    private final CommandExecutor defaultCmdExecutor = new DefaultCmdExecutor();
    private final Map<String, CommandExecutor> executorMap = new HashMap<>();

    @Override
    public void resolve(String[] args) {
        Command command = commandParser.parse(args);

        executorMap.getOrDefault(command.cmd(), defaultCmdExecutor).execute(command);
    }
}
