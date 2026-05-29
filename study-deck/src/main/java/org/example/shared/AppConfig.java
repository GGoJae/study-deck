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
import org.example.core.application.card.dto.response.AnswerCapture;
import org.example.core.application.card.dto.response.CardCapture;
import org.example.core.application.card.factory.AnswerFactory;
import org.example.core.application.card.factory.AnswerFactoryV1;
import org.example.core.application.card.factory.CardFactory;
import org.example.core.application.card.factory.CardFactoryV1;
import org.example.core.application.card.mapper.AnswerToCaptureMapper;
import org.example.core.application.card.mapper.CardToCaptureMapper;
import org.example.core.application.card.service.CardCommandServiceV1;
import org.example.core.application.card.service.CardQueryServiceV1;
import org.example.core.application.card.usecase.CardCommandUseCase;
import org.example.core.application.card.usecase.CardQueryUseCase;
import org.example.core.application.category.dto.response.CategoryCapture;
import org.example.core.application.category.factory.CategoryFactory;
import org.example.core.application.category.factory.CategoryFactoryV1;
import org.example.core.application.category.factory.sortcalcultor.CategorySortCalculator;
import org.example.core.application.category.factory.sortcalcultor.CategorySortCalculatorV1;
import org.example.core.application.category.mapper.CategoryToModelMapperV1;
import org.example.core.application.category.service.CategoryCommandServiceV1;
import org.example.core.application.category.service.CategoryInternalQueryServiceV1;
import org.example.core.application.category.service.CategoryQueryServiceV1;
import org.example.core.application.category.usecase.CategoryCommandUseCase;
import org.example.core.application.category.usecase.CategoryQueryUseCase;
import org.example.core.application.mapper.ToResponseMapper;
import org.example.core.application.progress.dto.response.CardForDeck;
import org.example.core.application.progress.mapper.CardToForDeckResponse;
import org.example.core.application.progress.port.ProgressPort;
import org.example.core.application.progress.selector.CardSelector;
import org.example.core.application.progress.selector.OldestReviewedSelector;
import org.example.core.application.progress.service.PopCardServiceV1;
import org.example.core.application.progress.usecase.PopCardUseCase;
import org.example.core.application.subcategory.dto.response.SubCategoryCapture;
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
import org.example.core.domain.card.Answer;
import org.example.core.domain.card.Card;
import org.example.core.domain.card.CardStore;
import org.example.core.domain.category.Category;
import org.example.core.domain.category.CategoryPort;
import org.example.core.domain.category.CategoryStore;
import org.example.core.domain.subcategory.SubCategory;
import org.example.core.domain.subcategory.SubCategoryPort;
import org.example.filestore.api.FileStoreApi;
import org.example.filestore.card.adapter.CardStoreAdapter;
import org.example.filestore.card.manager.AnswerManager;
import org.example.filestore.card.manager.AnswerManagerV1;
import org.example.filestore.card.manager.CardManager;
import org.example.filestore.card.manager.CardManagerV1;
import org.example.filestore.card.mapper.ToAnswerMapper;
import org.example.filestore.card.mapper.ToCardMapper;
import org.example.filestore.card.model.AnswerModel;
import org.example.filestore.card.model.CardModel;
import org.example.filestore.category.adapter.CategoryStoreAdapter;
import org.example.filestore.category.manager.CategoryManager;
import org.example.filestore.category.manager.CategoryManagerV1;
import org.example.filestore.category.mapper.ModelToDomainMapperV1;
import org.example.filestore.category.model.CategoryModel;
import org.example.filestore.data.meta.manager.JacksonMetaDataManagerV1;
import org.example.filestore.data.meta.manager.MetaDataManager;
import org.example.filestore.data.session.SubmitFileManagerV1;
import org.example.filestore.data.session.SubmitManager;
import org.example.filestore.filesystem.manager.FileSystemManager;
import org.example.filestore.filesystem.manager.FileSystemManagerV1;
import org.example.filestore.filesystem.naming.FileNameGenerator;
import org.example.filestore.filesystem.naming.UuidFileNameGenerator;
import org.example.filestore.progress.adapter.ProgressJacksonAdapter;
import org.example.filestore.progress.manager.ProgressManager;
import org.example.filestore.progress.manager.ProgressManagerV1;
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
    private static final PopCardUseCase popCardUseCase;

    static {
        CategoryManager categoryManager = new CategoryManagerV1();
        MetaDataManager metaDataManager = new JacksonMetaDataManagerV1();
        FileNameGenerator fileNameGenerator = new UuidFileNameGenerator();
        SubCategoryManager subCategoryManager = new SubCategoryManagerV1();
        FileSystemManager fileSystemManager = new FileSystemManagerV1(metaDataManager, fileNameGenerator, categoryManager, subCategoryManager);
        CardManager cardManager = new CardManagerV1(fileSystemManager);
        SubmitManager submitManager = new SubmitFileManagerV1(metaDataManager);
        ProgressManager progressManager = new ProgressManagerV1(fileSystemManager);
        FileStoreApi fileStoreApi = new FileStoreApi(categoryManager, subCategoryManager, cardManager, fileSystemManager, metaDataManager, submitManager, progressManager);
        AnswerManager answerManager = new AnswerManagerV1(fileSystemManager);

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
        ToResponseMapper<Category, CategoryCapture> toCategoryCapture = new CategoryToModelMapperV1();
        categoryQueryPort = new CategoryQueryServiceV1(categoryPort, toCategoryCapture);


        ModelToDomainMapper<SubCategory, SubCategoryModel> modelToSubCategoryMapper = new ModelToSubCategoryMapperV1();

        SubCategoryStoreAdapter subCategoryStore = new SubCategoryStoreAdapter(fileSystemManager, categoryManager, metaDataManager, subCategoryManager, cardManager, answerManager, progressManager, modelToSubCategoryMapper);
        SubCategorySortCalculator subCategorySortCalculator = new SubCategorySortCalculatorV1(subCategoryStore);
        SubCategoryFactory subCategoryFactory = new SubCategoryFactoryV1(subCategorySortCalculator);
        SubCategoryPort subCategoryPort = new SubCategoryInternalQueryServiceV1(subCategoryStore);
        ToResponseMapper<SubCategory, SubCategoryCapture> subCategoryToModelMapper = new SubCategoryToModelMapper();
        ModelToDomainMapper<Card, CardModel> toCardMapper = new ToCardMapper();
        ModelToDomainMapper<Answer, AnswerModel> toAnswerMapper = new ToAnswerMapper();
        CardStore cardStore = new CardStoreAdapter(cardManager, answerManager, fileSystemManager, metaDataManager, toCardMapper, toAnswerMapper);
        CardFactory cardFactory = new CardFactoryV1();

        subCategoryCommandUseCase = new SubCategoryCommandServiceV1(categoryPort, subCategoryStore, subCategoryFactory);
        subCategoryQueryUseCase = new SubCategoryQueryServiceV1(subCategoryPort, subCategoryToModelMapper);
        SystemOutOutput output = new SystemOutOutput();
        RequesterInfo requesterInfo = new RequesterInfo();

        AnswerFactory answerFactory = new AnswerFactoryV1();
        cardCommandUseCase = new CardCommandServiceV1(cardStore, cardFactory, answerFactory);
        ToResponseMapper<Card, CardCapture> cardToCaptureMapper = new CardToCaptureMapper();
        ToResponseMapper<Answer, AnswerCapture> answerToCaptureMapper = new AnswerToCaptureMapper();
        cardQueryUseCase = new CardQueryServiceV1(cardStore, cardToCaptureMapper, answerToCaptureMapper);

        ProgressPort progressPort = new ProgressJacksonAdapter(progressManager);
        CardSelector cardSelector = new OldestReviewedSelector();
        ToResponseMapper<Card, CardForDeck> toCardForDeck = new CardToForDeckResponse();
        popCardUseCase = new PopCardServiceV1(cardStore, progressPort, cardSelector, toCardForDeck);

        List<CommandExecutor> commandExecutors = cmdExecutorList(fileStoreApi, requesterInfo, output);
        DefaultCmdExecutor defaultCmdExecutor = new DefaultCmdExecutor();
        commandResolver = new BasicCommandResolverV1(commandParser, commandExecutors, defaultCmdExecutor);
    }

    private static List<CommandFormat> commandFormatList() {
        CommandFormat addCmd = createAddCmd();
        CommandFormat initCmd = createInitCmd();
        CommandFormat catCmd = createCategoryCmd();
        CommandFormat subCmd = createSubCmd();
        CommandFormat cardCmd = createCardCmd();
        CommandFormat editCmd = createEditCmd();
        CommandFormat submitCmd = createSubmitCmd();
        CommandFormat discardCmd = createDiscardCmd();
        CommandFormat nextCmd = createNextCmd();
        CommandFormat answerCmd = createAnswerCmd();
        CommandFormat useCmd = createUseCmd();
        CommandFormat statusCmd = createStatusCmd();
        return List.of(addCmd, initCmd, catCmd, subCmd, cardCmd, editCmd, submitCmd, discardCmd, nextCmd, answerCmd, useCmd, statusCmd);
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
        CategoryCmdExecutor catCmdExecutor = new CategoryCmdExecutor(categoryCommandPort, categoryQueryPort, requesterInfo, output, fileStoreApi);
        SubCmdExecutor subCmdExecutor = new SubCmdExecutor(requesterInfo, subCategoryQueryUseCase, subCategoryCommandUseCase, output, fileStoreApi);
        CardCmdExecutor cardCmdExecutor = new CardCmdExecutor(output, cardCommandUseCase, cardQueryUseCase, fileStoreApi, requesterInfo);
        EditCmdExecutor editCmdExecutor = new EditCmdExecutor(fileStoreApi, output);
        SubmitCmdExecutor submitCmdExecutor = new SubmitCmdExecutor(fileStoreApi, cardCommandUseCase, cardQueryUseCase, requesterInfo, output);
        DiscardCmdExecutor discardCmdExecutor = new DiscardCmdExecutor(fileStoreApi);
        NextCmdExecutor nextCmdExecutor = new NextCmdExecutor(popCardUseCase, fileStoreApi, requesterInfo, output);
        AnswerCmdExecutor answerCmdExecutor = new AnswerCmdExecutor(fileStoreApi, cardQueryUseCase, output, requesterInfo);
        UseCmdExecutor useCmdExecutor = new UseCmdExecutor(fileStoreApi, output);
        StatusCmdExecutor statusCmdExecutor = new StatusCmdExecutor(fileStoreApi, output, requesterInfo, categoryQueryPort, subCategoryQueryUseCase, cardQueryUseCase);
        return List.of(
                initCmdExecutor, catCmdExecutor, subCmdExecutor, cardCmdExecutor,
                editCmdExecutor, submitCmdExecutor, discardCmdExecutor, nextCmdExecutor,
                answerCmdExecutor, useCmdExecutor, statusCmdExecutor
        );
    }

    private static CommandFormat createAddCmd() {
        OptionFormat cOption = new OptionFormat("-c", Essential.REQUIRED);
        return new CommandFormat("add", Essential.NONE, Essential.REQUIRED, Map.of(cOption.value(), cOption));
    }

    private static CommandFormat createInitCmd() {
        return new CommandFormat("init", Essential.NONE, Essential.NONE, Map.of());
    }

    private static CommandFormat createCategoryCmd() {
        OptionFormat nOption = new OptionFormat("-n", Essential.REQUIRED);
        OptionFormat dOption = new OptionFormat("-d", Essential.REQUIRED);

        return new CommandFormat("category", Essential.OPTIONAL, Essential.OPTIONAL,
                Map.of(
                        nOption.value(), nOption,
                        dOption.value(), dOption
                        )
        );
    }

    private static CommandFormat createSubCmd() {
        OptionFormat dOption = new OptionFormat("-d", Essential.REQUIRED);
        OptionFormat nOption = new OptionFormat("-n", Essential.REQUIRED);

        return new CommandFormat("sub", Essential.OPTIONAL, Essential.OPTIONAL,
                Map.of(
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

    private static CommandFormat createEditCmd() {
        return new CommandFormat("edit", Essential.NONE, Essential.NONE, Map.of());
    }

    private static CommandFormat createSubmitCmd() {
        return new CommandFormat("submit", Essential.NONE, Essential.NONE, Map.of());
    }

    private static CommandFormat createDiscardCmd() {
        return new CommandFormat("discard", Essential.NONE, Essential.NONE, Map.of());
    }

    private static CommandFormat createNextCmd() {
        return new CommandFormat("next", Essential.NONE, Essential.NONE, Map.of());
    }

    private static CommandFormat createAnswerCmd() {
        return new CommandFormat("answer", Essential.OPTIONAL, Essential.OPTIONAL, Map.of());
    }

    private static CommandFormat createUseCmd() {
        return new CommandFormat("use", Essential.REQUIRED, Essential.NONE, Map.of());
    }

    private static CommandFormat createStatusCmd() {
        return new CommandFormat("status", Essential.NONE, Essential.NONE, Map.of());
    }


    public static void appStart(String[] args) {
        new BasicCommandInput(commandResolver, args).execute();
    }

}
