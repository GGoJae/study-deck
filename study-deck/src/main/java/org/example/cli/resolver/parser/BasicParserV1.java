package org.example.cli.resolver.parser;

import org.example.cli.model.format.OptionFormat;
import org.example.cli.model.command.Command;
import org.example.cli.model.command.Option;
import org.example.cli.resolver.validator.CommandValidator;
import org.example.cli.resolver.validator.CommandValidatorV1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BasicParserV1 implements CommandParser{

    private final CommandValidator commandValidator;

    public BasicParserV1(CommandValidator commandValidator) {
        this.commandValidator = commandValidator;
    }

    @Override
    public Command parse(String[] args) {
        try {
            if (Objects.isNull(args) || Objects.equals(args.length, 0)) {
                throw new IllegalArgumentException("명령어가 입력되지 않았습니다.");
            }
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
                    cmdArgs = List.of(Arrays.copyOfRange(optionsAndArguments, 0, optionIdx));
                    optionsAndArguments = Arrays.copyOfRange(optionsAndArguments, optionIdx, optionsAndArguments.length);
                }

                Option.OptionBuilder optionBuilder = new Option.OptionBuilder();
                for (String oa : optionsAndArguments) {
                    if (OptionFormat.isOptionFormat(oa)) {
                        if (Objects.equals(oa.length(), 1)) throw new IllegalArgumentException("옵션 값이 올바르지 않습니다.");
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