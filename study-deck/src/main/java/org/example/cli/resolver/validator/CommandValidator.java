package org.example.cli.resolver.validator;

import org.example.cli.model.command.Command;

public interface CommandValidator {
    boolean isRightFormat(Command command);

    boolean isWrongFormat(Command command);
}
