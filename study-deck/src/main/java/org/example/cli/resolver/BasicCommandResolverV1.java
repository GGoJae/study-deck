package org.example.cli.resolver;

import org.example.cli.excutor.CatCmdExecutor;
import org.example.cli.excutor.CommandExecutor;
import org.example.cli.excutor.DefaultCmdExecutor;
import org.example.cli.excutor.InitCmdExecutor;
import org.example.cli.info.RequesterInfo;
import org.example.cli.model.command.Command;
import org.example.cli.output.SystemOutOutput;
import org.example.cli.resolver.parser.CommandParser;
import org.example.core.category.mapper.CategoryToModelMapperV1;
import org.example.core.category.permission.CategoryPermissionChecker;
import org.example.core.category.permission.CategoryPermissionCheckerV1;
import org.example.core.category.port.in.CategoryCommandPort;
import org.example.core.category.port.in.CategoryQueryPort;
import org.example.core.category.port.out.CategoryStore;
import org.example.core.category.service.CategoryQueryServiceV1;
import org.example.filestore.api.FileStoreApi;
import org.example.filestore.category.adapter.CategoryStoreAdapter;
import org.example.filestore.category.manager.CategoryManagerV1;
import org.example.filestore.category.mapper.ModelToDomainMapperV1;
import org.example.filestore.data.DataManagerV1;
import org.example.filestore.data.meta.manager.JacksonMetaDataManagerV1;
import org.example.filestore.filesystem.manager.FileSystemManagerV1;
import org.example.filestore.filesystem.naming.UuidFileNameGenerator;
import org.example.filestore.filesystem.path.PathCalculatorV1;

import java.util.HashMap;
import java.util.Map;

public class BasicCommandResolverV1 implements CommandResolver{

    public BasicCommandResolverV1(CommandParser commandParser, CategoryCommandPort categoryCommandPort, FileStoreApi fileStoreApi) {
        this.commandParser = commandParser;
        executorMapInit(fileStoreApi, categoryCommandPort);
    }

    private void executorMapInit(FileStoreApi fileStoreApi, CategoryCommandPort categoryCommandPort) {
        UuidFileNameGenerator uuidFileNameGenerator = new UuidFileNameGenerator();
        CategoryManagerV1 categoryManagerV1 = new CategoryManagerV1();
        DataManagerV1 dataManagerV1 = new DataManagerV1(categoryManagerV1);
        PathCalculatorV1 pathCalculatorV1 = new PathCalculatorV1(categoryManagerV1, dataManagerV1);
        JacksonMetaDataManagerV1 jacksonMetaDataManagerV1 = new JacksonMetaDataManagerV1();
        FileSystemManagerV1 fileSystemManagerV1 = new FileSystemManagerV1(jacksonMetaDataManagerV1, pathCalculatorV1, dataManagerV1, uuidFileNameGenerator);
        ModelToDomainMapperV1 modelToDomainMapper = new ModelToDomainMapperV1();
        CategoryStoreAdapter categoryStoreAdapter = new CategoryStoreAdapter(fileSystemManagerV1, categoryManagerV1, jacksonMetaDataManagerV1, modelToDomainMapper);
        CategoryPermissionCheckerV1 categoryPermissionCheckerV1 = new CategoryPermissionCheckerV1(categoryStoreAdapter);
        CategoryToModelMapperV1 categoryToModelMapperV1 = new CategoryToModelMapperV1();
        CategoryQueryServiceV1 categoryQueryPort = new CategoryQueryServiceV1(categoryStoreAdapter, categoryPermissionCheckerV1, categoryToModelMapperV1);
        InitCmdExecutor initCmdExecutor = new InitCmdExecutor(fileStoreApi);
        SystemOutOutput output = new SystemOutOutput();
        RequesterInfo requesterInfo = new RequesterInfo();
        CatCmdExecutor catCmdExecutor = new CatCmdExecutor(categoryCommandPort, categoryQueryPort, requesterInfo, output, fileStoreApi);

        executorMap.put(initCmdExecutor.canResolvedCommand(), initCmdExecutor);
        executorMap.put(catCmdExecutor.canResolvedCommand(), catCmdExecutor);
    }

    private final CommandParser commandParser;

    private final CommandExecutor defaultCmdExecutor = new DefaultCmdExecutor();
    private final Map<String, CommandExecutor> executorMap = new HashMap<>();

    @Override
    public void resolve(String[] args) {
        Command command = commandParser.parse(args);

        executorMap.getOrDefault(command.cmd(), defaultCmdExecutor).execute(command);
    }
}
