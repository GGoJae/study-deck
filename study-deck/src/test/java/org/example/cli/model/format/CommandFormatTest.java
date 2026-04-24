package org.example.cli.model.format;

import org.example.cli.model.command.Command;
import org.example.cli.model.command.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandFormatTest {

    @Test
    @DisplayName("isRightFormat 과 isWrongFormat 은 항상 반대의 결과를 리턴한다.")
    void isRightFormat_should_return_opposite_of_isWrongFormat() {
        // given
        Command command = new Command("command", List.of(), List.of());
        CommandFormat commandFormat = new CommandFormat("test", Essential.OPTIONAL, Essential.OPTIONAL, Map.of());

        // when
        boolean isWrong = commandFormat.isWrongFormat(command);
        boolean isRight = commandFormat.isRightFormat(command);

        assertThat(isRight).isNotEqualTo(isWrong);
    }

    @Test
    @DisplayName("argument essential 에 null로 생성하면 기본값인 OPTIONAL이 들어간다.")
    void when_argument_essential_input_null_should_create_default_value_OPTIONAL() {
        // given
        String test = "test";
        Essential argumentRequirement = null;
        Essential optionRequirement = Essential.OPTIONAL;
        Map<String, OptionFormat> optionFormats = Map.of();

        // when
        CommandFormat commandFormat = new CommandFormat(test, argumentRequirement, optionRequirement, optionFormats);

        // then
        assertThat(commandFormat.argumentRequirement())
                .isNotNull()
                .isEqualTo(Essential.OPTIONAL);
    }

    @Test
    @DisplayName("option essential 에 null로 생성하면 기본값인 OPTIONAL이 들어간다.")
    void when_option_essential_input_null_should_create_default_value_OPTIONAL() {
        // given
        String test = "test";
        Essential argumentRequirement = Essential.OPTIONAL;
        Essential optionRequirement = null;
        Map<String, OptionFormat> optionFormats = Map.of();

        // when
        CommandFormat commandFormat = new CommandFormat(test, argumentRequirement, optionRequirement, optionFormats);

        // then
        assertThat(commandFormat.optionRequirement())
                .isNotNull()
                .isEqualTo(Essential.OPTIONAL);
    }


    @Test
    @DisplayName("option map 에 null로 생성하면 기본값인 Map.of();이 들어간다.")
    void when_option_map_input_null_should_create_empty_map() {
        // given
        String test = "test";
        Essential argumentRequirement = Essential.OPTIONAL;
        Essential optionRequirement = Essential.OPTIONAL;
        Map<String, OptionFormat> optionFormats = null;

        // when
        CommandFormat commandFormat = new CommandFormat(test, argumentRequirement, optionRequirement, optionFormats);

        // then
        assertThat(commandFormat.optionFormats())
                .isNotNull()
                .isEqualTo(Map.of());
    }


    @Test
    @DisplayName("Command의 cmd와 format의 cmd가 일치하지 않으면 isRightFormat 은 false 를 리턴한다.")
    void when_cmd_of_Command_notEquals_cmd_of_format_should_return_false() {
        // given
        Command wrong = new Command("wrong", List.of(), List.of());
        CommandFormat commandFormat = new CommandFormat("test", Essential.OPTIONAL, Essential.OPTIONAL, Map.of());

        // when
        boolean result = commandFormat.isRightFormat(wrong);

        // then
        assertThat(result).isFalse();

    }

    @Test
    @DisplayName("Command가 null이면 isRightFormat은 false를 리턴한다.")
    void when_Command_isNull_should_return_false() {
        // given
        Command nullCommand = null;
        CommandFormat commandFormat = new CommandFormat("test", Essential.OPTIONAL, Essential.OPTIONAL, Map.of());

        // when
        boolean result = commandFormat.isRightFormat(nullCommand);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Command의 cmd가 null이면 isRightFormat은 false를 리턴한다.")
    void when_Commands_cmd_isNull_should_return_false() {
        // given
        Command command = new Command(null, List.of(), List.of());
        CommandFormat commandFormat = new CommandFormat("test", Essential.OPTIONAL, Essential.OPTIONAL, Map.of());

        // when
        boolean result = commandFormat.isRightFormat(command);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("argument가 NONE인데, arguments가 비어있지않으면 isRightFormat은 false를 리턴한다.")
    void when_format_none_argument_but_arguments_isNotEmpty_isRightFormat_method_should_return_true() {
        // given
        Command command = new Command("test", List.of("arg"), List.of());
        CommandFormat commandFormat = new CommandFormat("test", Essential.NONE, Essential.OPTIONAL, Map.of());

        // when
        boolean result = commandFormat.isRightFormat(command);

        // then
        assertThat(result).isFalse();
    }


    @Test
    @DisplayName("argument가 필수인데, arguments가 비어있으면 isRightFormat 은 false를 리턴한다.")
    void when_format_required_argument_but_arguments_isEmpty_isRightFormat_method_should_return_false() {
        // given
        Command command = new Command("test", List.of(), List.of());
        CommandFormat commandFormat = new CommandFormat("test", Essential.REQUIRED, Essential.OPTIONAL, Map.of());

        // when
        boolean result = commandFormat.isRightFormat(command);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("option이 NONE인데, options가 비어있지않으면 isRightFormat 은 false를 리턴한다.")
    void when_format_none_option_but_options_isNotEmpty_isRightFormat_method_should_return_false() {
        // given
        Command command = new Command("test", List.of(), List.of(new Option("option", List.of("optArg"))));
        CommandFormat commandFormat = new CommandFormat("test", Essential.OPTIONAL, Essential.NONE, Map.of());

        // when
        boolean result = commandFormat.isRightFormat(command);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("option이 REQUIRED인데, options가 비어있으면 isRightFormat 은 false를 리턴한다.")
    void when_format_required_option_but_options_isEmpty_isRightFormat_method_should_return_false() {
        // given
        Command command = new Command("test", List.of(), List.of());
        CommandFormat commandFormat = new CommandFormat("test", Essential.OPTIONAL, Essential.REQUIRED, Map.of());

        // when
        boolean result = commandFormat.isRightFormat(command);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("agument와 option이 OPTIONAL이면 isRightFormat 은true 를 리턴한다.")
    void when_both_argument_and_option_is_OPTIONAL_isRightFormat_should_return_true() {
        // given
        Command command = new Command("test", List.of(), List.of());
        CommandFormat commandFormat = new CommandFormat("test", Essential.OPTIONAL, Essential.OPTIONAL, Map.of());

        // when
        boolean result = commandFormat.isRightFormat(command);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("augument가 REQUIRED 이고 값이 있다면 isRightFormat은 true를 리턴한다.")
    void when_argumentRequirement_is_REQUIRED_and_arguments_has_value_isRightFormat_should_return_true() {
        // given
        Command command = new Command("test", List.of("args"), List.of());
        CommandFormat commandFormat = new CommandFormat("test", Essential.REQUIRED, Essential.OPTIONAL, Map.of());

        // when
        boolean result = commandFormat.isRightFormat(command);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("augument가 NONE 이고 값이 없다면 isRightFormat 은 true을 리턴한다.")
    void when_argumentRequirement_is_NONE_and_arguments_hasNot_value_should_return_true() {
        // given
        Command command = new Command("test", List.of(), List.of());
        CommandFormat commandFormat = new CommandFormat("test", Essential.NONE, Essential.OPTIONAL, Map.of());

        // when
        boolean result = commandFormat.isRightFormat(command);

        // then
        assertThat(result).isTrue();
    }


    @Test
    @DisplayName("option 가 NONE 이고 값이 없다면 isRightFormat 은 true를 리턴한다.")
    void when_optionRequirement_is_NONE_and_options_hasNot_value_should_return_true() {
        // given
        Command command = new Command("test", List.of(), List.of());
        CommandFormat commandFormat = new CommandFormat("test", Essential.OPTIONAL, Essential.NONE, Map.of());

        // when
        boolean result = commandFormat.isRightFormat(command);

        // then
        assertThat(result).isTrue();
    }


    @Test
    @DisplayName("option 가 REQUIRED 이고 값이 있다면 isRightFormat 은 true를 리턴한다.")
    void when_optionRequirement_is_REQUIRED_and_options_has_value_should_return_true() {
        // given
        OptionFormat mock = mock(OptionFormat.class);
        when(mock.isRightOptionFormat(any())).thenReturn(true);
        Command command = new Command("test", List.of(), List.of(new Option("-option", List.of())));
        CommandFormat commandFormat = new CommandFormat("test", Essential.OPTIONAL, Essential.REQUIRED, Map.of("-option", mock));

        // when
        boolean result = commandFormat.isRightFormat(command);

        // then
        assertThat(result).isTrue();
    }


    @Test
    @DisplayName("argument,option 모두 REQUIRED이고 둘 다 값이 있을 경우 isRightFormat은 true를 리턴한다.")
    void when_both_argument_and_option_requirement_is_REQUIRED_and_both_have_value_should_return_true() {
        // given
        OptionFormat mock = mock(OptionFormat.class);
        when(mock.isRightOptionFormat(any())).thenReturn(true);
        Command command = new Command("test", List.of("arg"), List.of(new Option("-option", List.of())));
        CommandFormat commandFormat = new CommandFormat("test", Essential.REQUIRED, Essential.REQUIRED, Map.of("-option", mock));

        // when
        boolean result = commandFormat.isRightFormat(command);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("argument,option 모두 NONE이고 둘 다 값이 있을 경우 isRightFormat은 true를 리턴한다.")
    void when_both_argument_and_option_requirement_is_NONE_and_both_haveNot_value_should_return_true() {
        // given
        Command command = new Command("test", List.of(), List.of());
        CommandFormat commandFormat = new CommandFormat("test", Essential.NONE, Essential.NONE, Map.of());

        // when
        boolean result = commandFormat.isRightFormat(command);

        // then
        assertThat(result).isTrue();
    }


    @Test
    @DisplayName("requirement 조건을 다 만족하면 optionFormat의 isWrongFormat을 호출하여 옵션값을 검증한다.")
    void when_all_requirement_condition_passed_should_call_optionFormats_isRightFormat() {
        // given
        OptionFormat mock = mock(OptionFormat.class);
        Command command = new Command("test", List.of("arg"), List.of(new Option("-option", List.of())));
        CommandFormat commandFormat = new CommandFormat("test", Essential.REQUIRED, Essential.REQUIRED, Map.of("-option", mock));

        // when
        commandFormat.isRightFormat(command);

        // then
        verify(mock).isWrongOptionFormat(any());
    }


    @Test
    @DisplayName("optionFormat의 isWrongFormat이 true 를 리턴하면 commandFormat의 isRightFormat은 false를 리턴한다. ")
    void when_optionFormat_isWrongFormat_return_true_commandFormat_should_return_false() {
        // given
        OptionFormat mock = mock(OptionFormat.class);
        when(mock.isWrongOptionFormat(any())).thenReturn(true);
        Command command = new Command("test", List.of("arg"), List.of(new Option("-option", List.of())));
        CommandFormat commandFormat = new CommandFormat("test", Essential.REQUIRED, Essential.REQUIRED, Map.of("-option", mock));

        // when
        boolean result = commandFormat.isRightFormat(command);

        // then
        assertThat(result).isFalse();
    }




}