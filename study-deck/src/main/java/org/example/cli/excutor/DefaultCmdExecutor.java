package org.example.cli.excutor;

public class DefaultCmdExecutor implements CommandExecutor{
    @Override
    public String canResolvedCommand() {
        return null;
    }

    @Override
    public void execute() {
        System.out.println("명령어를 확인해주세요.");
    }
}
