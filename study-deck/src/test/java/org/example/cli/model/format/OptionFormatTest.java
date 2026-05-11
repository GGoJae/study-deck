package org.example.cli.model.format;

import org.example.cli.model.command.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OptionFormatTest {

    @Test
    @DisplayName("[생성자] value에 null을 넣으면 예외가 터진다.")
    void when_create_format_put_in_value_is_null_should_throw_exception() {
        // given
        String value = null;
        Essential essential = Essential.OPTIONAL;

        // when, then
        assertThatThrownBy(() -> new OptionFormat(value, Essential.OPTIONAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("value 형식이 맞지 않습니다.");
    }

    @DisplayName("[생성자] value에 wrongValueFormat을 넣으면 예외가 터진다.")
    @ParameterizedTest
    @ValueSource(strings = {"-", " ", "- ", " -", "option"})
    void when_create_format_put_in_value_is_wrong_format_should_throw_exception(String inputValue) {
        // given
        String value = inputValue;
        Essential essential = Essential.OPTIONAL;

        // when, then
        assertThatThrownBy(() -> new OptionFormat(value, Essential.OPTIONAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("value 형식이 맞지 않습니다.");
    }

    @Test
    @DisplayName("[생성자] Essential 에 null 을 넣으면 기본값인 OPTIONAL로 생성된다.")
    void when_essential_input_null_constructor_should_create_OPTIONAL() {
        // given
        String value = "-option";
        Essential essential = null;

        // when
        OptionFormat format = new OptionFormat(value, essential);

        // then
        assertThat(format.hasArgument())
                .isEqualTo(Essential.OPTIONAL);

    }

    @Test
    @DisplayName("isWrongValueFormat 과 isRightValueFormat 은 항상 반대 값을 리턴한다.")
    void isWrongValueFormat_always_return_opposite_of_isRightValueFormats() {
        // given
        String value = "-option";

        // when
        boolean isRight = OptionFormat.isRightValueFormat(value);
        boolean isWrong = OptionFormat.isWrongValueFormat(value);

        // then
        assertThat(isRight)
                .isNotEqualTo(isWrong);
    }


    @Test
    @DisplayName("isWrongOptionFormat 과 isRightOptionFormat 은 항상 반대 값을 리턴한다.")
    void isWrongOptionFormat_always_return_opposite_of_isRightOptionFormats() {
        // given
        Option option = new Option("-option", List.of());
        OptionFormat format = new OptionFormat("-option", Essential.OPTIONAL);

        // when
        boolean isRight = format.isRightOptionFormat(option);
        boolean isWrong = format.isWrongOptionFormat(option);

        // then
        assertThat(isRight)
                .isNotEqualTo(isWrong);
    }

    @DisplayName("[isRightOptionFormat] option이 잘못된 valueFormat을 가지고 있을 경우 false를 리턴한다.")
    @ParameterizedTest
    @ValueSource(strings = {"-", " ", "- ", " -", "option"})
    void when_option_has_wrong_valueFormat_should_return_false(String optionValue) {
        // given
        Option option = new Option(optionValue, List.of());
        OptionFormat format = new OptionFormat("-test", Essential.OPTIONAL);

        // when
        boolean result = format.isRightOptionFormat(option);

        // then
        assertThat(result).isFalse();
    }


    @Test
    @DisplayName("[isRightOptionFormat] option이 null 인 경우 false를 리턴한다.")
    void when_option_isNull_should_return_false() {
        // given
        Option option = null;
        OptionFormat format = new OptionFormat("-test", Essential.OPTIONAL);

        // when
        boolean result = format.isRightOptionFormat(option);

        // then
        assertThat(result).isFalse();
    }


    @Test
    @DisplayName("[isRightOptionFormat] option의 value가  null 인 경우 false를 리턴한다.")
    void when_options_value_isNull_should_return_false() {
        // given
        Option option = new Option(null, List.of());
        OptionFormat format = new OptionFormat("-test", Essential.OPTIONAL);

        // when
        boolean result = format.isRightOptionFormat(option);

        // then
        assertThat(result).isFalse();
    }



    @Test
    @DisplayName("[isRightOptionFormat] option의 value가 format 의 value와 다른 경우 false를 리턴한다.")
    void when_options_value_difference_formats_value_should_return_false() {
        // given
        Option option = new Option("-wrong", List.of());
        OptionFormat format = new OptionFormat("-test", Essential.OPTIONAL);

        // when
        boolean result = format.isRightOptionFormat(option);

        // then
        assertThat(result).isFalse();
    }


    @Test
    @DisplayName("[isRightOptionFormat] option의 value가 format 의 value와 같고 다른 조건도 맞다면 true를 리턴한다.")
    void when_options_value_equals_formats_value_and_other_cond_passed_should_return_true() {
        // given
        Option option = new Option("-test", List.of());
        OptionFormat format = new OptionFormat("-test", Essential.OPTIONAL);

        // when
        boolean result = format.isRightOptionFormat(option);

        // then
        assertThat(result).isTrue();
    }


    @Test
    @DisplayName("[isRightOptionFormat] hasArgument 가 REQUIRED 인데 argumnets 가 비어있으면 false를 리턴한다.")
    void when_hasArgument_is_REQUIRED_but_argument_isEmpty_should_return_false() {
        // given
        Option option = new Option("-test", List.of());
        OptionFormat format = new OptionFormat("-test", Essential.REQUIRED);

        // when
        boolean result = format.isRightOptionFormat(option);

        // then
        assertThat(result).isFalse();
    }



    @Test
    @DisplayName("[isRightOptionFormat] hasArgument 가 NONE 인데 argumnets 에 값이 있다면 false를 리턴한다.")
    void when_hasArgument_is_NONE_but_argument_isNotEmpty_should_return_false() {
        // given
        Option option = new Option("-test", List.of("-arg"));
        OptionFormat format = new OptionFormat("-test", Essential.NONE);

        // when
        boolean result = format.isRightOptionFormat(option);

        // then
        assertThat(result).isFalse();
    }



    @Test
    @DisplayName("[isRightOptionFormat] hasArgument 가 OPTIONAL 인데 argumnets 에 값이 있다면 true를 리턴한다.")
    void when_hasArgument_is_OPTION_and_argument_isNotEmpty_should_return_true() {
        // given
        Option option = new Option("-test", List.of("-arg"));
        OptionFormat format = new OptionFormat("-test", Essential.OPTIONAL);

        // when
        boolean result = format.isRightOptionFormat(option);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("[isRightOptionFormat] hasArgument 가 OPTIONAL 인데 argumnets 에 값이 없다면 true를 리턴한다.")
    void when_hasArgument_is_OPTION_and_argument_isEmpty_should_return_true() {
        // given
        Option option = new Option("-test", List.of());
        OptionFormat format = new OptionFormat("-test", Essential.OPTIONAL);

        // when
        boolean result = format.isRightOptionFormat(option);

        // then
        assertThat(result).isTrue();
    }






}