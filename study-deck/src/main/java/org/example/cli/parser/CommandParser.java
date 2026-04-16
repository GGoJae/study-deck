package org.example.cli.parser;

import org.example.cli.model.Command;

public interface CommandParser {
    Command parse(String[] args);
}
