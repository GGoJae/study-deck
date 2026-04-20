package org.example.cli.model.command;

import java.util.List;

public record Command(
        String cmd,
        List<String> arguments,
        List<Option> options
) {
    public static Command emptyCommand() {
        return new Command(null, List.of(), List.of());
    }
}
