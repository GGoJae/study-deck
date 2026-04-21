package org.example.cli.excutor;

import org.example.cli.info.RequesterInfo;
import org.example.cli.model.command.Command;
import org.example.cli.output.Output;
import org.example.core.category.dto.CategoryQuery;
import org.example.core.category.model.CategoryCapture;
import org.example.core.category.model.CreateCategory;
import org.example.core.category.port.in.CategoryCommandPort;
import org.example.core.category.port.in.CategoryQueryPort;
import org.example.filestore.api.FileStoreApi;

import java.util.List;

public class CatCmdExecutor implements CommandExecutor{

    private final CategoryCommandPort categoryCommandPort;
    private final CategoryQueryPort categoryQueryPort;
    private final RequesterInfo requesterInfo;
    private final Output output;
    private final FileStoreApi fileStoreApi;

    public CatCmdExecutor(CategoryCommandPort categoryCommandPort, CategoryQueryPort categoryQueryPort, RequesterInfo requesterInfo, Output output, FileStoreApi fileStoreApi) {
        this.categoryCommandPort = categoryCommandPort;
        this.categoryQueryPort = categoryQueryPort;
        this.requesterInfo = requesterInfo;
        this.output = output;
        this.fileStoreApi = fileStoreApi;
    }

    @Override
    public String canResolvedCommand() {
        return "cat";
    }

    @Override
    public void execute(Command command) {
        Long requesterId = requesterInfo.id();
        List<String> arguments = command.arguments();

        if (arguments.isEmpty()) {
            List<CategoryCapture> ownCategories = categoryQueryPort.getOwnCategories(new CategoryQuery(requesterId, 0, 100));
            Long currentCategoryId = fileStoreApi.currentCategory();
            output.categoriesAndCurrentCategory(ownCategories, currentCategoryId);
            return;
        }

        String categoryName = arguments.stream().findAny().orElseThrow();
        categoryCommandPort.create(new CreateCategory(requesterId, categoryName, 100));

    }
}
