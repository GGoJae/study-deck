package org.example.cli.parser;

import org.example.cli.command.format.Format;
import org.example.cli.command.model.CommandFormat;
import org.example.cli.command.model.OptionFormat;
import org.example.cli.model.Command;
import org.example.cli.model.Option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BasicParserV1 implements CommandParser{

    @Override
    public Command parse(String[] args) {
        try {
            String cmd = args[0];

            List<Option> options = new ArrayList<>();
            if (args.length > 1) {
                String[] optionsAndArguments = Arrays.copyOfRange(args, 1, args.length);

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

            Command command = new Command(cmd, options);
            CommandFormat commandFormat = Format.commandFormatMap().get(cmd);
            if (commandFormat.isWrongOptionFormat(command)) throw new IllegalStateException();

            return command;
        } catch (Exception e) {
            return Command.emptyCommand();
        }
    }
}