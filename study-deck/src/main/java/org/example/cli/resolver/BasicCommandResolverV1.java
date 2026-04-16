package org.example.cli.resolver;

import org.example.cli.excutor.CommandExecutor;
import org.example.cli.excutor.DefaultCmdExecutor;
import org.example.cli.excutor.InitCmdExecutor;

import java.util.HashMap;
import java.util.Map;

public class BasicCommandResolverV1 implements CommandResolver{
    public BasicCommandResolverV1() {
        executorMapInit();
    }

    private void executorMapInit() {
        InitCmdExecutor initCmdExecutor = new InitCmdExecutor();

        executorMap.put(initCmdExecutor.canResolvedCommand(), initCmdExecutor);
    }

    private final CommandExecutor defaultCmdExecutor = new DefaultCmdExecutor();
    private final Map<String, CommandExecutor> executorMap = new HashMap<>();
    @Override
    public void resolve(String rawCommand) {
        // parser 두기
        executorMap.getOrDefault(rawCommand, defaultCmdExecutor).execute();
    }
}
