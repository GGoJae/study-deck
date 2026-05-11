package org.example.cli.resolver.validator;

import org.example.cli.model.command.Command;
import org.example.cli.model.format.CommandFormat;
import org.example.cli.model.format.Essential;
import org.example.cli.repository.CmdFormatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandValidatorV1Test {

    @Mock
    private CmdFormatRepository cmdFormatRepository;
    @InjectMocks
    private CommandValidatorV1 validator;

    @Test
    @DisplayName("isRightFormat은 isWrongFormat과 반대의 결과를 리턴해야 한다.")
    void isRightFormat_should_return_opposite_of_isWrongFormat() {
        // given
        Command command = new Command("test", List.of(), List.of());
        when(cmdFormatRepository.findByCmd(any())).thenReturn(
                Optional.of(new CommandFormat("test", Essential.OPTIONAL, Essential.OPTIONAL, Map.of()))
        );

        // when
        boolean isRight = validator.isRightFormat(command);
        boolean isWrong = validator.isWrongFormat(command);

        // then
        assertThat(isRight).isTrue();
        assertThat(isWrong).isFalse();

    }

    @Test
    @DisplayName("cmd 가 null이 아닐때 formatRepository에서 cmdFormat을 호출한다.")
    void when_cmd_isNotNull_should_call_formatRepository_findByCmd() {
        // given
        Command command = new Command("test", List.of(), List.of());

        // when
        validator.isRightFormat(command);

        // then
        verify(cmdFormatRepository).findByCmd("test");
    }


    @Test
    @DisplayName("cmd 의 포맷을 찾을 수 없으면 isRightFormat은 false를 리턴한다.")
    void when_not_found_cmd_isRightFormat_method_should_return_false() {
        // given
        Command command = new Command("test", List.of(), List.of());
        when(cmdFormatRepository.findByCmd(any())).thenReturn(Optional.empty());

        // when
        boolean result = validator.isRightFormat(command);

        // then
        assertThat(result)
                .isFalse();
    }

    @Test
    @DisplayName("포맷이 존재하면 CommandFormat 의 isRightFormat 을 호출해야 한다.")
    void when_format_exists_should_call_isRightFormat() {
        // given
        Command command = new Command("test", List.of(), List.of());
        CommandFormat mock = mock(CommandFormat.class);
        when(cmdFormatRepository.findByCmd(any())).thenReturn(Optional.of(mock));

        // when
        validator.isRightFormat(command);

        // then
        verify(mock).isRightFormat(command);
    }






}