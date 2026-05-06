package org.example.cli.excutor;

import org.example.cli.info.RequesterInfo;
import org.example.cli.model.command.Command;
import org.example.cli.model.command.Option;
import org.example.cli.output.Output;
import org.example.core.application.subcategory.dto.request.CreateSubCategoryRequest;
import org.example.core.application.subcategory.dto.response.SubCategoryCapture;
import org.example.core.application.subcategory.usecase.SubCategoryCommandUseCase;
import org.example.core.application.subcategory.usecase.SubCategoryQueryUseCase;
import org.example.filestore.api.FileStoreApi;

import java.util.List;
import java.util.Objects;

public class SubCmdExecutor implements CommandExecutor{

    private final RequesterInfo requesterInfo;
    private final SubCategoryQueryUseCase queryUseCase;
    private final SubCategoryCommandUseCase commandUseCase;
    private final Output output;
    private final FileStoreApi fileStoreApi;

    public SubCmdExecutor(
            RequesterInfo requesterInfo, SubCategoryQueryUseCase queryPort, SubCategoryCommandUseCase commandPort,
            Output output, FileStoreApi fileStoreApi
    ) {
        this.requesterInfo = requesterInfo;
        this.queryUseCase = queryPort;
        this.commandUseCase = commandPort;
        this.output = output;
        this.fileStoreApi = fileStoreApi;
    }

    @Override
    public String canResolvedCommand() {
        return "sub";
    }

    @Override
    public void execute(Command command) {
        Objects.requireNonNull(command);
        Objects.requireNonNull(command.cmd());
        Long requesterId = requesterInfo.id();
        Long currentCategory = fileStoreApi.currentCategory();
        if (Objects.isNull(currentCategory)) {
            output.errorMessage("카테고리가 선택되지 않았습니다.");
        }

        if (!command.hasArgument() && !command.hasOptions()) {
            showSubCategories(requesterId, currentCategory);
        }

        if (!command.hasOptions() && command.hasArgument()) {
            List<String> arguments = command.arguments();
            String name = arguments.get(0);
            commandUseCase.createSubCategory(new CreateSubCategoryRequest(requesterId, currentCategory, name, null));
        }

        List<Option> options = command.options();
        Option option = options.get(0);
        String value = option.value();
        if ("-s".equals(value)) {
            selectSubCategory(option);
        } else if ("-n".equals(value)) {
            renameSubCategory(option, requesterId);
        } else if ("-d".equals(value)) {
            deleteSubCategory(option, requesterId);
        } else {
            output.errorMessage("sub 커맨드에서 지원하지 않는 옵션입니다.");
        }

    }

    private void deleteSubCategory(Option option, Long requesterId) {
        try {
            long subCategoryId = Long.parseLong(option.arguments().get(0));
            commandUseCase.delete(requesterId, subCategoryId);
        } catch (NumberFormatException nfe) {
            output.errorMessage("subCategory id 는 숫자 형식이어야 합니다.");
        }
    }

    private void renameSubCategory(Option option, Long requesterId) {
        try {
            List<String> arguments = option.arguments();
            long subCategoryId = Long.parseLong(arguments.get(0));
            String newName = arguments.get(1);
            commandUseCase.rename(requesterId, subCategoryId, newName);
        } catch (NumberFormatException nfe) {
            output.errorMessage("subCategory id 는 숫자 형식이어야 합니다.");
        } catch (IndexOutOfBoundsException iobe) {
            output.errorMessage("변경할 이름을 입력해주세요");
        }
    }

    private void selectSubCategory(Option option) {
        try {
            long subCategoryId = Long.parseLong(option.arguments().get(0));
            fileStoreApi.changeCurrentSubCategory(subCategoryId);
        } catch (NumberFormatException nfe) {
            output.errorMessage("subCategory id 는 숫자 형식이어야 합니다.");
        }
    }

    private void showSubCategories(Long requesterId, Long currentCategory) {
        Long currentSubCat = fileStoreApi.currentSubCat();
        List<SubCategoryCapture> subCategories = queryUseCase.getSubCategories(requesterId, currentCategory);
        output.subAndCurrentSub(subCategories, currentSubCat);
    }
}
