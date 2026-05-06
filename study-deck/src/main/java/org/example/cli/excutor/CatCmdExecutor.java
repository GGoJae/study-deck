package org.example.cli.excutor;

import org.example.cli.info.RequesterInfo;
import org.example.cli.model.command.Command;
import org.example.cli.model.command.Option;
import org.example.cli.output.Output;
import org.example.core.application.category.dto.request.CategoryQuery;
import org.example.core.application.category.dto.response.CategoryCapture;
import org.example.core.application.category.dto.request.CreateCategoryRequest;
import org.example.core.application.category.usecase.CategoryCommandUseCase;
import org.example.core.application.category.usecase.CategoryQueryUseCase;
import org.example.filestore.api.FileStoreApi;

import java.util.List;
import java.util.Objects;

public class CatCmdExecutor implements CommandExecutor{

    private final CategoryCommandUseCase categoryCommandPort;
    private final CategoryQueryUseCase categoryQueryPort;
    private final RequesterInfo requesterInfo;
    private final Output output;
    private final FileStoreApi fileStoreApi;

    public CatCmdExecutor(CategoryCommandUseCase categoryCommandPort, CategoryQueryUseCase categoryQueryPort, RequesterInfo requesterInfo, Output output, FileStoreApi fileStoreApi) {
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
        Objects.requireNonNull(command);
        Objects.requireNonNull(command.cmd());
        Long requesterId = requesterInfo.id();

        if (!command.hasOptions() && !command.hasArgument()) {
            showCategoryList(requesterId);
            return;
        }

        if (!command.hasOptions()) {
            createCategory(command, requesterId);
            return;
        }

        List<Option> options = command.options();
        if (options.size() > 1) throw new IllegalStateException("cat 커맨드에 다중 옵션은 지원하지 않습니다.");

        Option option = options.get(0);
        String value = option.value();
        if ("-s".equals(value)) {
            selectCategory(option);
        } else if ("-n".equals(value)) {
            renameCategory(option, requesterId);
        } else if ("-d".equals(value)) {
            deleteCategory(option, requesterId);
        } else {
            throw new IllegalArgumentException("cat 커맨드에서 지원하지 않는 옵션입니다.");
        }

    }

    private void deleteCategory(Option option, Long requesterId) {
        try {
            long categoryId = Long.parseLong(option.arguments().get(0));
            categoryCommandPort.delete(requesterId, categoryId);
        } catch (NumberFormatException nfe) {
            output.errorMessage("category id 는 숫자 형식이어야 합니다.");
        }
    }

    private void renameCategory(Option option, Long requesterId) {
        try {
            long categoryId = Long.parseLong(option.arguments().get(0));
            String newName = option.arguments().get(1);
            categoryCommandPort.rename(requesterId, categoryId, newName);
        } catch (NumberFormatException nfe) {
            output.errorMessage("category id 는 숫자 형식이어야 합니다.");
        }
    }

    private void selectCategory(Option option) {
        try {
            long categoryId = Long.parseLong(option.arguments().get(0));
            fileStoreApi.changeCurrentCategory(categoryId);
        } catch (NumberFormatException nfe) {
            output.errorMessage("category id 는 숫자 형식이어야 합니다.");
        }
    }

    private void createCategory(Command command, Long requesterId) {
        List<String> arguments = command.arguments();
        if (arguments.size() > 1) {
            output.errorMessage("cat 커맨드에 다중 아귀먼트는 지원하지 않습니다.");
            return;
        }
        String categoryName = arguments.stream().findAny().orElseThrow();
        categoryCommandPort.create(new CreateCategoryRequest(requesterId, categoryName, null));
    }

    private void showCategoryList(Long requesterId) {
        List<CategoryCapture> ownCategories = categoryQueryPort.getOwnCategoriesForDisplay(new CategoryQuery(requesterId, 0, 100));
        Long currentCategoryId = fileStoreApi.currentCategory();
        output.categoriesAndCurrentCategory(ownCategories, currentCategoryId);
    }
}
