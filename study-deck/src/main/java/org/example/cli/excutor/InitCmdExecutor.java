package org.example.cli.excutor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class InitCmdExecutor implements CommandExecutor{
    private static final String RESOLVED_COMMAND = "init";

    @Override
    public String canResolvedCommand() {
        return RESOLVED_COMMAND;
    }

    @Override
    public void execute() {
        createDeckDir();

    }

    private void createDeckDir() {
        Path path = Path.of(".deck");

        if (Files.exists(path)) {
            System.out.println("이미 .deck 디렉토리가 존재합니다.");
            return;
        }

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(".deck 생성이 완료됐습니다.");
    }
}
