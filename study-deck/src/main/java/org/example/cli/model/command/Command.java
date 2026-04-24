package org.example.cli.model.command;

import java.util.List;

public record Command(
        String cmd,
        List<String> arguments,
        List<Option> options
) {
    public Command {
        arguments = (arguments == null) ? List.of() : List.copyOf(arguments);
        options = (options == null) ? List.of() : List.copyOf(options);
    }
    public static Command emptyCommand() {
        return new Command(null, List.of(), List.of());
    }

    public boolean hasArgument() {
        return !this.arguments.isEmpty();
    }

    public boolean hasOptions() {
        return !this.options.isEmpty();
    }
}
