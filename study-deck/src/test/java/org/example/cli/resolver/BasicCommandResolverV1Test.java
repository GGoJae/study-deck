package org.example.cli.resolver;


import org.example.cli.excutor.CommandExecutor;
import org.example.cli.model.command.Command;
import org.example.cli.resolver.parser.CommandParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicCommandResolverV1Test {

    @Mock
    private CommandParser commandParser;
    @Mock
    private CommandExecutor defaultExecutor;

    @Test
    @DisplayName("해당 명령어를 가진 Executors가 있으면 해당 Executor만 실행되어야 한다.")
    void resolve_Should_Call_Matching_Executor() {
        // given
        String[] input = {"correct"};
        CommandExecutor correctExecutor = mock(CommandExecutor.class);
        when(correctExecutor.canResolvedCommand()).thenReturn("correct");

        CommandExecutor otherExecutor = mock(CommandExecutor.class);
        when(otherExecutor.canResolvedCommand()).thenReturn("other");

        var resolver = new BasicCommandResolverV1(commandParser, List.of(correctExecutor, otherExecutor), defaultExecutor);
        when(commandParser.parse(any())).thenReturn(new Command("correct", List.of(), List.of()));

        // when
        resolver.resolve(input);

        // then
        Assertions.assertAll(
                () -> verify(correctExecutor).execute(any()),
                () -> verify(otherExecutor, never()).execute(any()),
                () -> verify(defaultExecutor, never()).execute(any())
        );
    }

    @Test
    @DisplayName("해결할 수 없는 명령어가 들어오면 DefaultExecutor 가 실행돼야한다.")
    void cannot_resolve_command_should_executor_default() {
        // given
        String[] input = {"correct"};
        CommandExecutor otherExecutor = mock(CommandExecutor.class);
        when(otherExecutor.canResolvedCommand()).thenReturn("wrong");
        when(commandParser.parse(any())).thenReturn(new Command("correct", List.of(), List.of()));

        var resolver = new BasicCommandResolverV1(commandParser, List.of(otherExecutor), defaultExecutor);

        // when
        resolver.resolve(input);

        // then
        Assertions.assertAll(
                () -> verify(otherExecutor, never()).execute(any()),
                () -> verify(defaultExecutor).execute(any())
        );
    }

}