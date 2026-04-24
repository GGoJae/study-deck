package org.example.cli.model.command;

import java.util.ArrayList;
import java.util.List;

public record Option(
        String value,
        List<String> arguments
) {

    public Option {
        arguments = (arguments == null) ? List.of() : List.copyOf(arguments);
    }

    public boolean hasArgument() {
        return !arguments.isEmpty();
    }

    public static class OptionBuilder {
        private String value;
        private final List<String> arguments = new ArrayList<>();

        public boolean hasValue() {
            return value != null;
        }

        public OptionBuilder value(String value) {
            if (this.value != null) throw new IllegalStateException();
            this.value = value;
            return this;
        }

        public OptionBuilder addArgument(String argument) {
            if (this.value == null) throw new IllegalStateException();
            this.arguments.add(argument);
            return this;
        }

        public Option build() {
            if (this.value == null) throw new IllegalStateException();
            return new Option(this.value, this.arguments);
        }
    }

}
