package org.example.cli.model.format;

import org.example.cli.model.command.Command;
import org.example.cli.model.command.Option;

import java.util.List;
import java.util.Map;

public record CommandFormat(
        String cmd,
        Essential argumentRequirement,
        Essential optionRequirement,
        Map<String, OptionFormat> optionFormats
) {
    public boolean isWrongOptionFormat(Command command) {
        return !this.isRightFormat(command);
    }
    public boolean isRightFormat(Command command) {
        List<Option> options = command.options();
        if (!this.cmd.equals(command.cmd())) return false;
        if (this.optionRequirement.equals(Essential.REQUIRED) && options.isEmpty()) return false;
        if (this.optionRequirement.equals(Essential.NONE) && !options.isEmpty()) return false;
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
