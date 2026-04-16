package org.example.cli.resolver;

import org.example.cli.excutor.CommandExecutor;
import org.example.cli.excutor.DefaultCmdExecutor;
import org.example.cli.excutor.InitCmdExecutor;
import org.example.cli.model.Command;
import org.example.cli.parser.CommandParser;

import java.util.HashMap;
import java.util.Map;

public class BasicCommandResolverV1 implements CommandResolver{

    public BasicCommandResolverV1(CommandParser commandParser) {
        this.commandParser = commandParser;
        executorMapInit();
    }

    private void executorMapInit() {
        InitCmdExecutor initCmdExecutor = new InitCmdExecutor();

        executorMap.put(initCmdExecutor.canResolvedCommand(), initCmdExecutor);
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
