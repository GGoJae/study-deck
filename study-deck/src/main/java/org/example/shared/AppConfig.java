package org.example.shared;

import org.example.cli.excutor.*;
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
import org.example.core.application.card.factory.CardFactory;
import org.example.core.application.card.factory.CardFactoryV1;
import org.example.core.application.card.service.CardCommandServiceV1;
import org.example.core.application.card.service.CardQueryServiceV1;
import org.example.core.application.card.usecase.CardCommandUseCase;
import org.example.core.application.card.usecase.CardQueryUseCase;
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
import org.example.core.application.subcategory.factory.SubCategoryFactory;
import org.example.core.application.subcategory.factory.SubCategoryFactoryV1;
import org.example.core.application.subcategory.factory.sortcalculator.SubCategorySortCalculator;
import org.example.core.application.subcategory.factory.sortcalculator.SubCategorySortCalculatorV1;
import org.example.core.application.subcategory.mapper.SubCategoryToModelMapper;
import org.example.core.application.subcategory.service.SubCategoryCommandServiceV1;
import org.example.core.application.subcategory.service.SubCategoryInternalQueryServiceV1;
import org.example.core.application.subcategory.service.SubCategoryQueryServiceV1;
import org.example.core.application.subcategory.usecase.SubCategoryCommandUseCase;
import org.example.core.application.subcategory.usecase.SubCategoryQueryUseCase;
import org.example.core.domain.card.CardStore;
import org.example.core.domain.category.Category;
import org.example.core.domain.category.CategoryPort;
import org.example.core.domain.category.CategoryStore;
import org.example.core.domain.subcategory.SubCategory;
import org.example.core.domain.subcategory.SubCategoryPort;
import org.example.filestore.api.FileStoreApi;
import org.example.filestore.card.adapter.CardStoreAdapter;
import org.example.filestore.card.manager.CardManager;
import org.example.filestore.card.manager.CardManagerV1;
import org.example.filestore.category.adapter.CategoryStoreAdapter;
import org.example.filestore.category.manager.CategoryManager;
import org.example.filestore.category.manager.CategoryManagerV1;
import org.example.filestore.category.mapper.ModelToDomainMapperV1;
import org.example.filestore.category.model.CategoryModel;
import org.example.filestore.data.meta.manager.JacksonMetaDataManagerV1;
import org.example.filestore.data.meta.manager.MetaDataManager;
import org.example.filestore.filesystem.manager.FileSystemManager;
import org.example.filestore.filesystem.manager.FileSystemManagerV1;
import org.example.filestore.filesystem.naming.FileNameGenerator;
import org.example.filestore.filesystem.naming.UuidFileNameGenerator;
import org.example.filestore.shared.ModelToDomainMapper;
import org.example.filestore.subcategory.adapter.SubCategoryStoreAdapter;
import org.example.filestore.subcategory.manager.SubCategoryManager;
import org.example.filestore.subcategory.manager.SubCategoryManagerV1;
import org.example.filestore.subcategory.mapper.ModelToSubCategoryMapperV1;
import org.example.filestore.subcategory.model.SubCategoryModel;

import java.util.List;
import java.util.Map;

public abstract class AppConfig {

    private static final CommandResolver commandResolver;
    private static final CategoryCommandUseCase categoryCommandPort;
    private static final CategoryQueryUseCase categoryQueryPort;
    private static final CategoryStore categoryStore;
    private static final SubCategoryCommandUseCase subCategoryCommandUseCase;
    private static final SubCategoryQueryUseCase subCategoryQueryUseCase;
    private static final CardCommandUseCase cardCommandUseCase;
    private static final CardQueryUseCase cardQueryUseCase;

    static {
        CategoryManager categoryManager = new CategoryManagerV1();
        MetaDataManager metaDataManager = new JacksonMetaDataManagerV1();
        FileNameGenerator fileNameGenerator = new UuidFileNameGenerator();
        SubCategoryManager subCategoryManager = new SubCategoryManagerV1();
        FileSystemManager fileSystemManager = new FileSystemManagerV1(metaDataManager, fileNameGenerator, categoryManager, subCategoryManager);
        FileStoreApi fileStoreApi = new FileStoreApi(categoryManager, subCategoryManager, fileSystemManager, metaDataManager);

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


        ModelToDomainMapper<SubCategory, SubCategoryModel> modelToSubCategoryMapper = new ModelToSubCategoryMapperV1();

        SubCategoryStoreAdapter subCategoryStore = new SubCategoryStoreAdapter(fileSystemManager, categoryManager, metaDataManager, subCategoryManager, modelToSubCategoryMapper);
        SubCategorySortCalculator subCategorySortCalculator = new SubCategorySortCalculatorV1(subCategoryStore);
        SubCategoryFactory subCategoryFactory = new SubCategoryFactoryV1(subCategorySortCalculator);
        SubCategoryPort subCategoryPort = new SubCategoryInternalQueryServiceV1(subCategoryStore);
        org.example.core.application.subcategory.mapper.DomainToModelMapper subCategoryToModelMapper = new SubCategoryToModelMapper();
        CardManager cardManager = new CardManagerV1(fileSystemManager);
        CardStore cardStore = new CardStoreAdapter(cardManager, fileSystemManager, metaDataManager);
        CardFactory cardFactory = new CardFactoryV1();

        subCategoryCommandUseCase = new SubCategoryCommandServiceV1(categoryPort, subCategoryStore, subCategoryFactory);
        subCategoryQueryUseCase = new SubCategoryQueryServiceV1(subCategoryPort, subCategoryToModelMapper);
        SystemOutOutput output = new SystemOutOutput();
        RequesterInfo requesterInfo = new RequesterInfo();

        cardCommandUseCase = new CardCommandServiceV1(cardStore, cardFactory);
        cardQueryUseCase = new CardQueryServiceV1();

        List<CommandExecutor> commandExecutors = cmdExecutorList(fileStoreApi, requesterInfo, output);
        DefaultCmdExecutor defaultCmdExecutor = new DefaultCmdExecutor();
        commandResolver = new BasicCommandResolverV1(commandParser, commandExecutors, defaultCmdExecutor);
    }

    private static List<CommandFormat> commandFormatList() {
        CommandFormat addCmd = createAddCmd();
        CommandFormat initCmd = createInitCmd();
        CommandFormat catCmd = createCatCmd();
        CommandFormat subCmd = createSubCmd();
        CommandFormat cardCmd = createCardCmd();
        return List.of(addCmd, initCmd, catCmd, subCmd, cardCmd);
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
        SubCmdExecutor subCmdExecutor = new SubCmdExecutor(requesterInfo, subCategoryQueryUseCase, subCategoryCommandUseCase, output, fileStoreApi);
        CardCmdExecutor cardCmdExecutor = new CardCmdExecutor(output, cardCommandUseCase, cardQueryUseCase, fileStoreApi, requesterInfo);
        return List.of(initCmdExecutor, catCmdExecutor, subCmdExecutor, cardCmdExecutor);
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
        OptionFormat nOption = new OptionFormat("-n", Essential.REQUIRED);

        return new CommandFormat("sub", Essential.OPTIONAL, Essential.OPTIONAL,
                Map.of(
                        sOption.value(), sOption,
                        dOption.value(), dOption,
                        nOption.value(), nOption
                ));
    }

    private static CommandFormat createCardCmd() {
        OptionFormat qOption = new OptionFormat("-q", Essential.REQUIRED);

        return new CommandFormat("card", Essential.OPTIONAL, Essential.OPTIONAL,
                Map.of(
                        qOption.value(), qOption
                ));
    }



    public static void appStart(String[] args) {
        new BasicCommandInput(commandResolver, args).execute();
    }

}
