package org.example.cli.input;

import org.example.cli.resolver.CommandResolver;

public class BasicCommandInput implements CommandInput{

    private final CommandResolver commandResolver;
    private final String command;

    public BasicCommandInput(CommandResolver commandResolver, String command) {
        this.commandResolver = commandResolver;
        this.command = command;
    }

    @Override
    public void execute() {
        commandResolver.resolve(command);
    }
}
