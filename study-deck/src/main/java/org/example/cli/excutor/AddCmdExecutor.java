package org.example.cli.excutor;

import org.example.cli.model.command.Command;

public class AddCmdExecutor implements CommandExecutor{

    @Override
    public String canResolvedCommand() {
        return "add";
    }

    @Override
    public void execute(Command command) {

    }
}
