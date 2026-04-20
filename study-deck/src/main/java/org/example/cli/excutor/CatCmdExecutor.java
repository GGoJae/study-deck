package org.example.cli.excutor;

import org.example.cli.model.command.Command;
import org.example.core.category.model.CreateCategory;
import org.example.core.category.port.in.CategoryCommandPort;

import java.util.List;

public class CatCmdExecutor implements CommandExecutor{

    private final CategoryCommandPort categoryCommandPort;

    public CatCmdExecutor(CategoryCommandPort categoryCommandPort) {
        this.categoryCommandPort = categoryCommandPort;
    }

    @Override
    public String canResolvedCommand() {
        return "cat";
    }

    @Override
    public void execute(Command command) {
        List<String> arguments = command.arguments();

        if (arguments == null) {
            // TODO 카테고리 query port 에서 카테고리 목록 가져오기 실행
            return;
        }

        String categoryName = arguments.stream().findAny().orElseThrow();
        categoryCommandPort.create(new CreateCategory(categoryName, 100));

    }
}
