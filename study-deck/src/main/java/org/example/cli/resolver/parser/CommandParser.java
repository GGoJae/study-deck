package org.example.cli.resolver.parser;

import org.example.cli.model.command.Command;

public interface CommandParser {
    Command parse(String[] args);
}
