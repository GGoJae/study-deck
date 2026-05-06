package org.example.shared;

import org.example.cli.excutor.CatCmdExecutor;
import org.example.cli.excutor.CommandExecutor;
import org.example.cli.excutor.DefaultCmdExecutor;
import org.example.cli.excutor.InitCmdExecutor;
import org.example.cli.info.RequesterInfo;
import org.example.cli.input.BasicCommandInput;
import org.example.cli.model.format.CommandFormat;
import org.example.cli.model.format.Essential;
import org.example.cli.model.format.OptionFormat;
import org.example.cli.output.SystemOutOutput;
import org.example.cli.repository.CmdFormatRepository;
import org.example.cli.repository.MemoryCmdFormatRepository;
import org.example.cli.resolver.BasicCommandResolverV1;
import org.example.cli.resolver.CommandResolver;
import org.example.cli.resolver.parser.BasicParserV1;
import org.example.cli.resolver.parser.CommandParser;
import org.example.cli.resolver.validator.CommandValidator;
import org.example.cli.resolver.validator.CommandValidatorV1;
import org.example.core.application.category.factory.CategoryFactory;
import org.example.core.application.category.factory.CategoryFactoryV1;
import org.example.core.application.category.factory.sortcalcultor.CategorySortCalculator;
import org.example.core.application.category.factory.sortcalcultor.CategorySortCalculatorV1;
import org.example.core.application.category.mapper.CategoryToModelMapperV1;
import org.example.core.application.category.mapper.DomainToModelMapper;
import org.example.core.application.category.service.CategoryCommandServiceV1;
import org.example.core.application.category.service.CategoryInternalQueryServiceV1;
import org.example.core.application.category.service.CategoryQueryServiceV1;
import org.example.core.application.category.usecase.CategoryCommandUseCase;
import org.example.core.application.category.usecase.CategoryQueryUseCase;
import org.example.core.domain.category.Category;
import org.example.core.domain.category.CategoryPort;
import org.example.core.domain.category.CategoryStore;
import org.example.filestore.api.FileStoreApi;
import org.example.filestore.category.adapter.CategoryStoreAdapter;
import org.example.filestore.category.manager.CategoryManager;
import org.example.filestore.category.manager.CategoryManagerV1;
import org.example.filestore.category.model.CategoryModel;
import org.example.filestore.shared.ModelToDomainMapper;
import org.example.filestore.category.mapper.ModelToDomainMapperV1;
import org.example.filestore.data.DataManager;
import org.example.filestore.data.DataManagerV1;
import org.example.filestore.data.meta.manager.JacksonMetaDataManagerV1;
import org.example.filestore.data.meta.manager.MetaDataManager;
import org.example.filestore.filesystem.manager.FileSystemManager;
import org.example.filestore.filesystem.manager.FileSystemManagerV1;
import org.example.filestore.filesystem.naming.FileNameGenerator;
import org.example.filestore.filesystem.naming.UuidFileNameGenerator;
import org.example.filestore.filesystem.path.PathCalculator;
import org.example.filestore.filesystem.path.PathCalculatorV1;

import java.util.List;
import java.util.Map;

public abstract class AppConfig {

    private static final CommandResolver commandResolver;
    private static final CategoryCommandUseCase categoryCommandPort;
    private static final CategoryQueryUseCase categoryQueryPort;
    private static final CategoryStore categoryStore;

    static {
        CategoryManager categoryManager = new CategoryManagerV1();
        DataManager dataManager = new DataManagerV1(categoryManager);
        MetaDataManager metaDataManager = new JacksonMetaDataManagerV1();
        FileNameGenerator fileNameGenerator = new UuidFileNameGenerator();
        PathCalculator pathCalculator = new PathCalculatorV1(categoryManager, dataManager);
        FileSystemManager fileSystemManager = new FileSystemManagerV1(metaDataManager, pathCalculator, dataManager, fileNameGenerator);
        FileStoreApi fileStoreApi = new FileStoreApi(categoryManager, fileSystemManager, metaDataManager);

        List<CommandFormat> commandFormats = commandFormatList();
        CmdFormatRepository cmdFormatRepository = new MemoryCmdFormatRepository(commandFormats);
        CommandValidator commandValidator = new CommandValidatorV1(cmdFormatRepository);
        CommandParser commandParser = new BasicParserV1(commandValidator);
        ModelToDomainMapper<Category, CategoryModel> modelToDomainMapper = new ModelToDomainMapperV1();
        categoryStore = new CategoryStoreAdapter(fileSystemManager, categoryManager, metaDataManager, modelToDomainMapper);
        CategoryPort categoryPort = new CategoryInternalQueryServiceV1(categoryStore);
        CategorySortCalculator sortCalculator = new CategorySortCalculatorV1(categoryStore);
        CategoryFactory categoryFactory = new CategoryFactoryV1(sortCalculator);
        categoryCommandPort = new CategoryCommandServiceV1(categoryStore, categoryFactory);
        DomainToModelMapper domainToModelMapper = new CategoryToModelMapperV1();
        categoryQueryPort = new CategoryQueryServiceV1(categoryPort, domainToModelMapper);
        SystemOutOutput output = new SystemOutOutput();
        RequesterInfo requesterInfo = new RequesterInfo();

        List<CommandExecutor> commandExecutors = cmdExecutorList(fileStoreApi, requesterInfo, output);
        DefaultCmdExecutor defaultCmdExecutor = new DefaultCmdExecutor();
        commandResolver = new BasicCommandResolverV1(commandParser, commandExecutors, defaultCmdExecutor);
    }

    private static List<CommandFormat> commandFormatList() {
        CommandFormat addCmd = createAddCmd();
        CommandFormat initCmd = createInitCmd();
        CommandFormat catCmd = createCatCmd();
        CommandFormat subCmd = createSubCmd();
        return List.of(addCmd, initCmd, catCmd, subCmd);
    }

    public static CommandResolver commandResolverInstance() {
        return commandResolver;
    }

    public static CategoryCommandUseCase categoryCommandPortInstance() {
        return categoryCommandPort;
    }

    public static CategoryQueryUseCase categoryQueryPortInstance() {
        return categoryQueryPort;
    }

    public static CategoryStore categoryStoreInstance() {
        return categoryStore;
    }

    private static List<CommandExecutor> cmdExecutorList(FileStoreApi fileStoreApi, RequesterInfo requesterInfo, SystemOutOutput output) {
        InitCmdExecutor initCmdExecutor = new InitCmdExecutor(fileStoreApi);
        CatCmdExecutor catCmdExecutor = new CatCmdExecutor(categoryCommandPort, categoryQueryPort, requesterInfo, output, fileStoreApi);
        return List.of(initCmdExecutor, catCmdExecutor);
    }

    private static CommandFormat createAddCmd() {
        OptionFormat cOption = new OptionFormat("-c", Essential.REQUIRED);
        return new CommandFormat("add", Essential.NONE, Essential.REQUIRED, Map.of(cOption.value(), cOption));
    }

    private static CommandFormat createInitCmd() {
        return new CommandFormat("init", Essential.NONE, Essential.NONE, Map.of());
    }

    private static CommandFormat createCatCmd() {
        OptionFormat sOption = new OptionFormat("-s", Essential.REQUIRED);
        OptionFormat nOption = new OptionFormat("-n", Essential.REQUIRED);
        OptionFormat dOption = new OptionFormat("-d", Essential.REQUIRED);

        return new CommandFormat("cat", Essential.OPTIONAL, Essential.OPTIONAL,
                Map.of(
                        sOption.value(), sOption,
                        nOption.value(), nOption,
                        dOption.value(), dOption
                        )
        );
    }

    private static CommandFormat createSubCmd() {
        OptionFormat sOption = new OptionFormat("-s", Essential.REQUIRED);
        OptionFormat dOption = new OptionFormat("-d", Essential.REQUIRED);
        return new CommandFormat("sub", Essential.OPTIONAL, Essential.OPTIONAL,
                Map.of(
                        sOption.value(), sOption,
                        dOption.value(), dOption
                ));
    }

    public static void appStart(String[] args) {
        new BasicCommandInput(commandResolver, args).execute();
    }

}
