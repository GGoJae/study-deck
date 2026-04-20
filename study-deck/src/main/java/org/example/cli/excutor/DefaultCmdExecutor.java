package org.example.cli.excutor;

import org.example.cli.model.command.Command;

public class DefaultCmdExecutor implements CommandExecutor{
    @Override
    public String canResolvedCommand() {
        return null;
    }

    @Override
    public void execute(Command command) {
        System.out.println("명령어를 확인해주세요.");
    }
}
