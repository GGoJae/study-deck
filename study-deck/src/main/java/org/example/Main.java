package org.example;

import org.example.cli.input.BasicCommandInput;
import org.example.cli.resolver.parser.BasicParserV1;
import org.example.cli.resolver.BasicCommandResolverV1;
import org.example.cli.resolver.validator.CommandValidator;
import org.example.core.category.service.CategoryCommandServiceV1;
import org.example.filestore.api.FileStoreApi;
import org.example.filestore.category.adapter.CategoryStoreAdapter;
import org.example.filestore.data.DataManagerV1;
import org.example.filestore.data.category.manager.CategoryManagerV1;
import org.example.filestore.data.meta.manager.JacksonMetaDataManagerV1;
import org.example.filestore.filesystem.manager.FileSystemManagerV1;
import org.example.filestore.filesystem.naming.UuidFileNameGenerator;
import org.example.filestore.filesystem.path.PathCalculatorV1;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        BasicCommandInput input = dependencyInjection(args);

        input.execute();
    }

    private static BasicCommandInput dependencyInjection(String[] args) {
        UuidFileNameGenerator uuidFileNameGenerator = new UuidFileNameGenerator();
        CategoryManagerV1 categoryManagerV1 = new CategoryManagerV1();
        DataManagerV1 dataManagerV1 = new DataManagerV1(categoryManagerV1);
        PathCalculatorV1 pathCalculatorV1 = new PathCalculatorV1(categoryManagerV1, dataManagerV1);
        JacksonMetaDataManagerV1 jacksonMetaDataManagerV1 = new JacksonMetaDataManagerV1();
        FileSystemManagerV1 fileSystemManagerV1 = new FileSystemManagerV1(jacksonMetaDataManagerV1, pathCalculatorV1, dataManagerV1, uuidFileNameGenerator);
        FileStoreApi fileStoreApi = new FileStoreApi(categoryManagerV1, fileSystemManagerV1, jacksonMetaDataManagerV1);
        CategoryStoreAdapter categoryStoreAdapter = new CategoryStoreAdapter(fileSystemManagerV1, categoryManagerV1, jacksonMetaDataManagerV1);
        CategoryCommandServiceV1 categoryCommandServiceV1 = new CategoryCommandServiceV1(categoryStoreAdapter);
        CommandValidator commandValidator = new CommandValidator();
        BasicParserV1 basicParserV1 = new BasicParserV1(commandValidator);
        BasicCommandResolverV1 basicCommandResolverV1 = new BasicCommandResolverV1(basicParserV1, categoryCommandServiceV1, fileStoreApi);
        BasicCommandInput input = new BasicCommandInput(basicCommandResolverV1, args);
        return input;
    }
}