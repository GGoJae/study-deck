package org.example.cli.command.model;

import org.example.cli.model.Option;

public record OptionFormat(
    String value,
    Essential hasArgument
) {
    public static boolean isOptionFormat(String str) {
        return str.startsWith("-");
    }

    public static boolean isNotOptionFormat(String string) {
        return !string.startsWith("-");
    }

    public boolean isWrongOptionFormat(Option option) {
        return !this.isRightOptionFormat(option);
    }

    public boolean isRightOptionFormat(Option option) {
        if (!this.value.equals(option.value())) return false;
        if (this.hasArgument.equals(Essential.TRUE) && option.argument() == null) return false;
        if (this.hasArgument.equals(Essential.FALSE) && option.argument() != null) return false;

        return true;
    }
}
