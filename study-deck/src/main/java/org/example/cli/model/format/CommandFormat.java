package org.example.cli.model.format;

import org.example.cli.model.command.Command;
import org.example.cli.model.command.Option;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public record CommandFormat(
        String cmd,
        Essential argumentRequirement,
        Essential optionRequirement,
        Map<String, OptionFormat> optionFormats
) {
    public CommandFormat {
        if (Objects.isNull(cmd) || cmd.isBlank()) {
            throw new IllegalArgumentException("cmd는 필수입니다.");
        }
        argumentRequirement = (argumentRequirement == null) ? Essential.OPTIONAL : argumentRequirement;
        optionRequirement = (optionRequirement == null) ? Essential.OPTIONAL : optionRequirement;
        optionFormats = (optionFormats == null) ? Map.of() : Map.copyOf(optionFormats);
    }
    public boolean isWrongFormat(Command command) {
        return !this.isRightFormat(command);
    }
    public boolean isRightFormat(Command command) {
        if (command == null || command.cmd() == null || command.cmd().isBlank()) return false;

        if (!Objects.equals(command.cmd(), this.cmd)) return false;
        if (this.argumentRequirement == Essential.REQUIRED && !command.hasArgument()) return false;
        if (this.argumentRequirement == Essential.NONE && command.hasArgument()) return false;
        if (this.optionRequirement == Essential.REQUIRED && !command.hasOptions()) return false;
        if (this.optionRequirement == Essential.NONE && command.hasOptions()) return false;

        List<Option> options = command.options();
        for (Option o : options) {
            OptionFormat format = optionFormats.get(o.value());
            if (format == null || format.isWrongOptionFormat(o)) return false;
        }

        return true;

    }
}
