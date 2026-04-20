package org.example.cli.resolver.validator;

import org.example.cli.model.command.Command;
import org.example.cli.model.format.CommandFormat;
import org.example.cli.model.format.Essential;
import org.example.cli.model.format.OptionFormat;

import java.util.HashMap;
import java.util.Map;

public class CommandValidator {
    private final Map<String, CommandFormat> formatRepository = new HashMap<>();

    public CommandValidator() {
        CommandFormat init = createInitCmd();
        CommandFormat add = createAddCmd();
        CommandFormat cat = createCatCmd();

        formatRepository.put(init.cmd(), init);
        formatRepository.put(add.cmd(), add);
        formatRepository.put(cat.cmd(), cat);
    }

    public boolean isRightFormat(Command command) {
        CommandFormat commandFormat = formatRepository.get(command.cmd());

        if (commandFormat == null) return false;

        return commandFormat.isRightFormat(command);
    }

    public boolean isWrongFormat(Command command) {
        return !isRightFormat(command);
    }


    private CommandFormat createAddCmd() {
        OptionFormat cOption = new OptionFormat("-c", Essential.REQUIRED);
        return new CommandFormat("add", Essential.NONE, Essential.REQUIRED, Map.of(cOption.value(), cOption));
    }

    private CommandFormat createInitCmd() {
        return new CommandFormat("init", Essential.NONE, Essential.NONE, Map.of());
    }

    private CommandFormat createCatCmd() {
        return new CommandFormat("cat", Essential.REQUIRED, Essential.OPTIONAL, Map.of());
    }


}
