package org.example.cli.repository;

import org.example.cli.model.format.CommandFormat;

import java.util.*;
import java.util.stream.Collectors;

public class MemoryCmdFormatRepository implements CmdFormatRepository{

    private final Map<String, CommandFormat> formatRepository;

    public MemoryCmdFormatRepository(List<CommandFormat> commandFormats) {
        this.formatRepository = commandFormats.stream().collect(Collectors.toMap(
                CommandFormat::cmd,
                cf -> cf,
                (a, b) -> b,
                HashMap::new
        ));
    }

    @Override
    public Optional<CommandFormat> findByCmd(String cmd) {
        if (Objects.isNull(cmd)) return Optional.empty();

        return Optional.ofNullable(formatRepository.get(cmd));
    }
}
