package org.example.cli.model;

import java.util.List;

public record Command(
        String cmd,
        List<Option> options
) {
    public static Command emptyCommand() {
        return new Command(null, List.of());
    }
}
