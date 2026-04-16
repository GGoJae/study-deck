package org.example.cli.command.model;

import org.example.cli.model.Command;
import org.example.cli.model.Option;

import java.util.List;
import java.util.Map;

public record CommandFormat(
        String cmd,
        Essential hasOption,
        Map<String, OptionFormat> optionFormats
) {
    public boolean isWrongOptionFormat(Command command) {
        return !this.isRightFormat(command);
    }
    public boolean isRightFormat(Command command) {
        List<Option> options = command.options();
        if (!this.cmd.equals(command.cmd())) return false;
        if (this.hasOption.equals(Essential.TRUE) && options.isEmpty()) return false;
        if (this.hasOption.equals(Essential.FALSE) && !options.isEmpty()) return false;
        try {
            for (Option o : options) {
                if (optionFormats.get(o).isWrongOptionFormat(o)) return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;

    }
}
