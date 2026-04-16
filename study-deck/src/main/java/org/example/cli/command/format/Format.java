package org.example.cli.command.format;

import org.example.cli.command.model.CommandFormat;
import org.example.cli.command.model.Essential;
import org.example.cli.command.model.OptionFormat;

import java.util.HashMap;
import java.util.Map;

public class Format {
    private static final Map<String, CommandFormat> commandMap = new HashMap<>();

    static {
        CommandFormat init = createInitCmd();
        CommandFormat add = createAddCmd();

        commandMap.put(init.cmd(), init);
        commandMap.put(add.cmd(), add);
    }

    public static Map<String, CommandFormat> commandFormatMap() {
        return Map.copyOf(commandMap);
    }

    private static CommandFormat createAddCmd() {
        OptionFormat cOptionFormat = new OptionFormat("-c", Essential.TRUE);
        return new CommandFormat("add", Essential.TRUE, Map.of(cOptionFormat.value(), cOptionFormat));
    }

    private static CommandFormat createInitCmd() {
        return new CommandFormat("init", Essential.FALSE, Map.of());
    }

}
