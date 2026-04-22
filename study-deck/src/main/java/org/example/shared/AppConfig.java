package org.example.shared;

import org.example.cli.excutor.CatCmdExecutor;
import org.example.cli.excutor.CommandExecutor;
import org.example.cli.excutor.DefaultCmdExecutor;
import org.example.cli.excutor.InitCmdExecutor;
import org.example.cli.info.RequesterInfo;
import org.example.cli.input.BasicCommandInput;
import org.example.cli.output.SystemOutOutput;
import org.example.cli.resolver.BasicCommandResolverV1;
import org.example.cli.resolver.CommandResolver;
import org.example.cli.resolver.parser.BasicParserV1;
import org.example.cli.resolver.parser.CommandParser;
import org.example.cli.resolver.validator.CommandValidator;
import org.example.cli.resolver.validator.CommandValidatorV1;
import org.example.core.category.mapper.CategoryToModelMapperV1;
import org.example.core.category.mapper.DomainToModelMapper;
import org.example.core.category.permission.CategoryPermissionChecker;
import org.example.core.category.permission.CategoryPermissionCheckerV1;
import org.example.core.category.port.in.CategoryCommandPort;
import org.example.core.category.port.in.CategoryQueryPort;
import org.example.core.category.port.out.CategoryStore;
import org.example.core.category.service.CategoryCommandServiceV1;
import org.example.core.category.service.CategoryQueryServiceV1;
import org.example.filestore.api.FileStoreApi;
import org.example.filestore.category.adapter.CategoryStoreAdapter;
import org.example.filestore.category.manager.CategoryManager;
import org.example.filestore.category.manager.CategoryManagerV1;
import org.example.filestore.category.mapper.ModelToDomainMapper;
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

public abstract class AppConfig {

    private static final CommandResolver commandResolver;
    private static final CategoryCommandPort categoryCommandPort;
    private static final CategoryQueryPort categoryQueryPort;
    private static final CategoryStore categoryStore;

    static {
        CategoryManager categoryManager = new CategoryManagerV1();
        DataManager dataManager = new DataManagerV1(categoryManager);
        MetaDataManager metaDataManager = new JacksonMetaDataManagerV1();
        FileNameGenerator fileNameGenerator = new UuidFileNameGenerator();
        PathCalculator pathCalculator = new PathCalculatorV1(categoryManager, dataManager);
        FileSystemManager fileSystemManager = new FileSystemManagerV1(metaDataManager, pathCalculator, dataManager, fileNameGenerator);
        FileStoreApi fileStoreApi = new FileStoreApi(categoryManager, fileSystemManager, metaDataManager);
        CommandValidator commandValidator = new CommandValidatorV1();
        CommandParser commandParser = new BasicParserV1(commandValidator);
        ModelToDomainMapper modelToDomainMapper = new ModelToDomainMapperV1();
        categoryStore = new CategoryStoreAdapter(fileSystemManager, categoryManager, metaDataManager, modelToDomainMapper);
        categoryCommandPort = new CategoryCommandServiceV1(categoryStore);
        DomainToModelMapper domainToModelMapper = new CategoryToModelMapperV1();
        CategoryPermissionChecker categoryPermissionChecker = new CategoryPermissionCheckerV1(categoryStore);
        categoryQueryPort = new CategoryQueryServiceV1(categoryStore, categoryPermissionChecker, domainToModelMapper);
        SystemOutOutput output = new SystemOutOutput();
        RequesterInfo requesterInfo = new RequesterInfo();

        List<CommandExecutor> commandExecutors = cmdExecutorList(fileStoreApi, requesterInfo, output);
        DefaultCmdExecutor defaultCmdExecutor = new DefaultCmdExecutor();
        commandResolver = new BasicCommandResolverV1(commandParser, commandExecutors, defaultCmdExecutor);
    }

    private static List<CommandExecutor> cmdExecutorList(FileStoreApi fileStoreApi, RequesterInfo requesterInfo, SystemOutOutput output) {
        InitCmdExecutor initCmdExecutor = new InitCmdExecutor(fileStoreApi);
        CatCmdExecutor catCmdExecutor = new CatCmdExecutor(categoryCommandPort, categoryQueryPort, requesterInfo, output, fileStoreApi);
        return List.of(initCmdExecutor, catCmdExecutor);
    }

    public static CommandResolver commandResolverInstance() {
        return commandResolver;
    }

    public static CategoryCommandPort categoryCommandPortInstance() {
        return categoryCommandPort;
    }

    public static CategoryQueryPort categoryQueryPortInstance() {
        return categoryQueryPort;
    }

    public static CategoryStore categoryStoreInstance() {
        return categoryStore;
    }

    public static void appStart(String[] args) {
        new BasicCommandInput(commandResolver, args).execute();
    }

}
