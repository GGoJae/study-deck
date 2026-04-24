package org.example.cli.model.format;

import org.example.cli.model.command.Option;

import java.util.Objects;

public record OptionFormat(
        String value,
        Essential hasArgument
) {
    public OptionFormat {
        if (isWrongValueFormat(value)) {
            throw new IllegalArgumentException("value 형식이 맞지 않습니다.");
        }
        value = value.trim();
        hasArgument = Objects.isNull(hasArgument) ? Essential.OPTIONAL : hasArgument;
    }

    public static boolean isRightValueFormat(String str) {
        if (str == null) return false;
        str = str.trim();
        return str.startsWith("-") && str.trim().length() > 1;
    }

    public static boolean isWrongValueFormat(String string) {
        return !isRightValueFormat(string);
    }

    public boolean isWrongOptionFormat(Option option) {
        return !this.isRightOptionFormat(option);
    }

    public boolean isRightOptionFormat(Option option) {
        if (option == null || option.value() == null || option.value().isBlank() || isWrongValueFormat(option.value())) return false;
        if (!Objects.equals(this.value, option.value())) return false;
        if (this.hasArgument == Essential.REQUIRED && (!option.hasArgument())) return false;
        if (this.hasArgument == Essential.NONE && option.hasArgument()) return false;

        return true;
    }
}
