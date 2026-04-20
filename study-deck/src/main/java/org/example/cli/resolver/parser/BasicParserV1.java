package org.example.cli.resolver.parser;

import org.example.cli.model.format.OptionFormat;
import org.example.cli.model.command.Command;
import org.example.cli.model.command.Option;
import org.example.cli.resolver.validator.CommandValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BasicParserV1 implements CommandParser{

    private final CommandValidator commandValidator;

    public BasicParserV1(CommandValidator commandValidator) {
        this.commandValidator = commandValidator;
    }

    @Override
    public Command parse(String[] args) {
        try {
            String cmd = args[0];

            List<String> cmdArgs = new ArrayList<>();
            List<Option> options = new ArrayList<>();

            if (args.length > 1) {
                String[] optionsAndArguments = Arrays.copyOfRange(args, 1, args.length);

                int optionIdx = -1;
                for (int i = 0; i < optionsAndArguments.length; i++) {
                    if (optionsAndArguments[i].startsWith("-")) {
                        optionIdx = i;
                        break;
                    }
                }

                if (optionIdx == -1) {
                    cmdArgs = List.of(optionsAndArguments);
                    optionsAndArguments = new String[0];
                } else if (optionIdx > 0){
                    cmdArgs = List.of(Arrays.copyOfRange(optionsAndArguments, 0, optionIdx - 1));
                    optionsAndArguments = Arrays.copyOfRange(optionsAndArguments, optionIdx, optionsAndArguments.length);
                }

                Option.OptionBuilder optionBuilder = new Option.OptionBuilder();
                for (String oa : optionsAndArguments) {
                    if (OptionFormat.isOptionFormat(oa)) {
                        if (optionBuilder.hasValue()) {
                            Option option = optionBuilder.build();
                            options.add(option);
                            optionBuilder = new Option.OptionBuilder();
                        }
                        optionBuilder.value(oa);
                    } else {
                        Option option = optionBuilder.argument(oa).build();
                        options.add(option);
                        optionBuilder = new Option.OptionBuilder();
                    }
                }
                if (optionBuilder.hasValue()) {
                    Option option = optionBuilder.build();
                    options.add(option);
                }
            }

            Command command = new Command(cmd, cmdArgs, options);
            if (commandValidator.isWrongFormat(command)) throw new IllegalStateException();

            return command;
        } catch (Exception e) {
            return Command.emptyCommand();
        }
    }
}