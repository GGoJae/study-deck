package org.example.cli.resolver.parser;

import org.example.cli.model.command.Command;
import org.example.cli.model.command.Option;
import org.example.cli.resolver.validator.CommandValidatorV1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BasicParserV1Test {

    @Mock
    private CommandValidatorV1 commandValidator;
    @InjectMocks
    private BasicParserV1 commandParse;

    @Test
    @DisplayName("빈 배열이 들어오면 EmptyCommand 가 리턴된다.")
    void when_input_array_is_empty_return_empty_command() {
        // given
        String[] input = {};

        // when
        Command command = commandParse.parse(input);

        // then
        assertThat(command)
                .isEqualTo(Command.emptyCommand());
    }


    @Test
    @DisplayName("배열에 null이 들어오면 EmptyCommand 가 리턴된다.")
    void when_input_array_is_null_return_empty_command() {
        // given
        String[] input = null;

        // when
        Command command = commandParse.parse(input);

        // then
        assertThat(command)
                .isEqualTo(Command.emptyCommand());
    }


    @Test
    @DisplayName("validator 를 통과하지 못하면 EmptyCommand 가 리턴된다.")
    void return_EmptyCommand_if_not_passed_validatoe() {
        // given
        String[] input = {"wrong", "wrong"};
        when(commandValidator.isWrongFormat(any())).thenReturn(true);

        // when
        Command command = commandParse.parse(input);

        // then
        assertThat(command)
                .isEqualTo(Command.emptyCommand());
    }

    @Test
    @DisplayName("[\"test\"] 가 들어오면 cmd가 \"test\"고 args 와 options 가 빈 Command 가 생성돼야한다.")
    void create_cmd_emptyArgs_emptyOptions_command_should_input_just_cmd() {
        // given
        when(commandValidator.isWrongFormat(any())).thenReturn(false);
        String[] input = {"test"};

        // when
        Command command = commandParse.parse(input);

        // then
        org.junit.jupiter.api.Assertions.assertAll(
                () -> assertThat(command.cmd())
                        .isEqualTo("test"),
                () -> assertThat(command.arguments())
                        .isEmpty(),
                () -> assertThat(command.options())
                        .isEmpty()
        );
    }

    @Test
    @DisplayName("cmd 다음 문자열이 \"-\"로 시작하지 않으면 arguments 로 취급된다. ")
    void when_cmd_next_string_is_not_startsWith_dash_its_in_arguments() {
        // given
        String[] input = {"test", "argument"};
        when(commandValidator.isWrongFormat(any())).thenReturn(false);

        // when
        Command command = commandParse.parse(input);

        // then
        org.junit.jupiter.api.Assertions.assertAll(
                () -> assertThat(command.cmd())
                        .isEqualTo("test"),
                () -> assertThat(command.arguments())
                        .hasSize(1)
                        .contains("argument"),
                () -> assertThat(command.options())
                        .isEmpty()
        );
    }

    @Test
    @DisplayName("cmd 다음 문자열이 -로 시작하고 2글자 이상이면 options 로 취급된다.")
    void when_cmd_next_string_is_startsWith_dash_its_in_options() {
        // given
        String[] strings = {"test", "-option"};
        when(commandValidator.isWrongFormat(any())).thenReturn(false);

        // when
        Command command = commandParse.parse(strings);

        // then
        Assertions.assertAll(
                () -> assertThat(command.cmd())
                        .isEqualTo("test"),
                () -> assertThat(command.arguments())
                        .isEmpty(),
                () -> assertThat(command.options())
                        .hasSize(1)
                        .contains(new Option("-option", List.of()))
        );
    }

    @Test
    @DisplayName("cmd 이후에 \"-\" 한글자만 들어와있다면 option value 가 아닌 argument 로 취급된다.")
    void when_input_only_dash_string_that_should_be_argument() {
        // given
        String[] input = {"test", "-"};
        when(commandValidator.isWrongFormat(any())).thenReturn(false);

        // when
        Command command = commandParse.parse(input);

        // then
        Assertions.assertAll(
                () -> assertThat(command.cmd())
                        .isEqualTo("test"),
                () -> assertThat(command.arguments())
                        .hasSize(1)
                        .contains("-")
        );

    }

    @Test
    @DisplayName("option 이후에 \"-\" 한글자만 들어와있다면 option value 가 아닌 option argument 로 취급된다.")
    void when_option_next_string_is_only_dash_string_that_should_be_option_argument() {
        // given
        String[] input = {"test", "-option", "-"};
        when(commandValidator.isWrongFormat(any())).thenReturn(false);

        // when
        Command command = commandParse.parse(input);

        // then
        Assertions.assertAll(
                () -> assertThat(command.cmd())
                        .isEqualTo("test"),
                () -> assertThat(command.arguments())
                        .isEmpty(),
                () -> assertThat(command.options())
                        .hasSize(1)
                        .contains(new Option("-option", List.of("-")))
        );

    }


    @Test
    @DisplayName("argument가 하나고 option에 argument가 없어도 각각 argument, option으로 취급된다.")
    void when_cmd_have_one_argument_and_option_without_argument_create_each_argument_and_option() {
        // given
        String[] input = {"test", "argument", "-option"};
        when(commandValidator.isWrongFormat(any())).thenReturn(false);

        // when
        Command command = commandParse.parse(input);

        // then
        Assertions.assertAll(
                () -> assertThat(command.cmd())
                        .isEqualTo("test"),
                () -> assertThat(command.arguments())
                        .hasSize(1)
                        .contains("argument"),
                () -> assertThat(command.options())
                        .hasSize(1)
                        .contains(new Option("-option", List.of()))
        );
    }

    @Test
    @DisplayName("cmd 다음 문자열이 optionFormat 이 아니면 전부 arguments 로 취급된다. ")
    void when_cmd_next_strings_is_not_startsWith_dash_its_all_in_arguments() {
        // given
        String[] input = {"test", "argument1", "argument2", "argument3"};
        when(commandValidator.isWrongFormat(any())).thenReturn(false);

        // when
        Command command = commandParse.parse(input);

        // then
        org.junit.jupiter.api.Assertions.assertAll(
                () -> assertThat(command.cmd())
                        .isEqualTo("test"),
                () -> assertThat(command.arguments())
                        .hasSize(3)
                        .contains("argument1")
                        .contains("argument2")
                        .contains("argument3"),
                () -> assertThat(command.options())
                        .isEmpty()
        );
    }

    @Test
    @DisplayName("option뒤에 오는 string은 option의 argument 로 취급된다.")
    void when_option_next_string_is_in_options_argument() {
        // given
        String[] input = {"test", "-option", "optionArg"};
        when(commandValidator.isWrongFormat(any())).thenReturn(false);

        // when
        Command command = commandParse.parse(input);

        // then
        Assertions.assertAll(
                () -> assertThat(command.cmd())
                        .isEqualTo("test"),
                () -> assertThat(command.arguments())
                        .isEmpty(),
                () -> assertThat(command.options())
                        .hasSize(1)
                        .contains(new Option("-option", List.of("optionArg")))
        );
    }

    @Test
    @DisplayName("argument가 있는 option과 없는 option 이 섞여있어도 각각의 Option객체가 생성된다.")
    void when_mixed_option_with_argument_and_option_without_argument_create_each_option_instance() {
        // given
        String[] input = {"test", "-option1", "-option2", "optionArg", "-option3"};
        when(commandValidator.isWrongFormat(any())).thenReturn(false);

        // when
        Command command = commandParse.parse(input);

        // then
        Assertions.assertAll(
                () -> assertThat(command.cmd())
                        .isEqualTo("test"),
                () -> assertThat(command.arguments())
                        .isEmpty(),
                () -> assertThat(command.options())
                        .hasSize(3)
                        .contains(
                                new Option("-option1", null),
                                new Option("-option2", List.of("optionArg")),
                                new Option("-option3", null)
                        )
        );
    }

    @Test
    @DisplayName("argument가 2개 이상 있고 그 뒤에 옵션들이 있어도 argument 와 Option들이 각자 잘 생성돼야한다.")
    void when_mixed_more_than_two_arguments_and_options_create_each_argument_instance_and_option_instance() {
        // given
        String[] input = {"test", "argument1", "argument2", "-option1", "-option2", "optionArg", "-option3"};
        when(commandValidator.isWrongFormat(any())).thenReturn(false);

        // when
        Command command = commandParse.parse(input);

        // then
        Assertions.assertAll(
                () -> assertThat(command.cmd())
                        .isEqualTo("test"),
                () -> assertThat(command.arguments())
                        .hasSize(2)
                        .contains("argument1")
                        .contains("argument2"),
                () -> assertThat(command.options())
                        .hasSize(3)
                        .contains(
                                new Option("-option1", null),
                                new Option("-option2", List.of("optionArg")),
                                new Option("-option3", null)
                        )
        );
    }

    @Test
    @DisplayName("option의 argument 가 2개 이상이여도 각 옵션에 아귀먼트들이 포함돼야한다.")
    void when_options_arguments_more_than_two_should_create_option_with_all_arguments() {
        // given
        String[] input = {"test", "-option1", "option1Arg1", "option1Arg2", "option1Arg3", "-option2", "optionArg", "-option3"};
        when(commandValidator.isWrongFormat(any())).thenReturn(false);

        // when
        Command command = commandParse.parse(input);

        // then
        Assertions.assertAll(
                () -> assertThat(command.cmd())
                        .isEqualTo("test"),
                () -> assertThat(command.arguments())
                        .isEmpty(),
                () -> assertThat(command.options())
                        .hasSize(3)
                        .contains(
                                new Option("-option1", List.of("option1Arg1", "option1Arg2", "option1Arg3")),
                                new Option("-option2", List.of("optionArg")),
                                new Option("-option3", List.of())
                        )

        );

    }
}