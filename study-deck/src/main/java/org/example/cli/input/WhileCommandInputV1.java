package org.example.cli.input;

import org.example.cli.resolver.CommandResolver;

import java.util.Scanner;

public class WhileCommandInputV1 implements CommandInput {

    public WhileCommandInputV1(CommandResolver cmdResolver) {
        this.cmdResolver = cmdResolver;
    }

    private final CommandResolver cmdResolver;
    private final Scanner sc = new Scanner(System.in);
    @Override
    public void execute() {

        System.out.print("커맨드를 입력해주세요 (종료 q!) : ");
        String cmd = sc.nextLine();

        while (!"q!".equals(cmd)) {
            cmdResolver.resolve(cmd);
            cmd = sc.nextLine();
        }

        System.out.println("종료됩니다.");

    }
}
