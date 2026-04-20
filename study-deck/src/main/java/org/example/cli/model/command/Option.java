package org.example.cli.model.command;

public record Option(
        String value,
        String argument
) {
    public static class OptionBuilder {
        private String value;
        private String argument;

        public boolean hasValue() {
            return value != null;
        }

        public OptionBuilder value(String value) {
            if (this.value != null) throw new IllegalStateException();
            this.value = value;
            return this;
        }

        public OptionBuilder argument(String argument) {
            if (this.argument != null) throw new IllegalStateException();
            this.argument = argument;
            return this;
        }

        public Option build() {
            if (this.value == null) throw new IllegalStateException();
            return new Option(this.value, this.argument);
        }
    }

}
