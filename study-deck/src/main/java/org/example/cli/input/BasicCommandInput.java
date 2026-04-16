package org.example.cli.input;

import org.example.cli.resolver.CommandResolver;

public class BasicCommandInput implements CommandInput{

    private final CommandResolver commandResolver;
    private final String[] args;

    public BasicCommandInput(CommandResolver commandResolver, String[] args) {
        this.commandResolver = commandResolver;
        this.args = args;
    }

    @Override
    public void execute() {
        commandResolver.resolve(args);
    }
}
