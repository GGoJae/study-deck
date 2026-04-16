package org.example.cli.excutor;

import org.example.cli.model.Command;

public interface CommandExecutor {
    String canResolvedCommand();
    void execute(Command command);
}
