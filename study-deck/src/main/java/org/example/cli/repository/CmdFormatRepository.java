package org.example.cli.repository;

import org.example.cli.model.format.CommandFormat;

import java.util.Optional;

public interface CmdFormatRepository {
    Optional<CommandFormat> findByCmd(String cmd);
}
