package org.example.cli.resolver;

import org.example.cli.excutor.CommandExecutor;
import org.example.cli.model.command.Command;
import org.example.cli.resolver.parser.CommandParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicCommandResolverV1 implements CommandResolver{

    public BasicCommandResolverV1(
            CommandParser commandParser, List<CommandExecutor> executors, CommandExecutor defaultCmdExecutor
    ) {
        this.commandParser = commandParser;
        this.defaultCmdExecutor = defaultCmdExecutor;
        executors.forEach(e -> this.executorMap.put(e.canResolvedCommand(), e));
    }

    private final CommandParser commandParser;

    private final CommandExecutor defaultCmdExecutor;
    private final Map<String, CommandExecutor> executorMap = new HashMap<>();

    @Override
    public void resolve(String[] args) {
        Command command = commandParser.parse(args);

        executorMap.getOrDefault(command.cmd(), defaultCmdExecutor).execute(command);
    }
}
