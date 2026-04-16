package org.example;

import org.example.cli.input.BasicCommandInput;
import org.example.cli.parser.BasicParserV1;
import org.example.cli.resolver.BasicCommandResolverV1;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        BasicParserV1 basicParserV1 = new BasicParserV1();
        BasicCommandResolverV1 basicCommandResolverV1 = new BasicCommandResolverV1(basicParserV1);
        BasicCommandInput input = new BasicCommandInput(basicCommandResolverV1, args);

        input.execute();
    }
}