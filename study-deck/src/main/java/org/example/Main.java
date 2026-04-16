package org.example;

import org.example.cli.input.BasicCommandInput;
import org.example.cli.input.WhileCommandInputV1;
import org.example.cli.resolver.BasicCommandResolverV1;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        BasicCommandResolverV1 basicCommandResolverV1 = new BasicCommandResolverV1();
//        WhileCommandInputV1 input = new WhileCommandInputV1(basicCommandResolverV1);
        BasicCommandInput input = new BasicCommandInput(basicCommandResolverV1, args[0]);

        input.execute();
    }
}