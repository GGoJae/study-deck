package org.example.cli.resolver.validator;

import org.example.cli.model.command.Command;
import org.example.cli.model.format.CommandFormat;
import org.example.cli.model.format.Essential;
import org.example.cli.model.format.OptionFormat;
import org.example.cli.repository.CmdFormatRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandValidatorV1 implements CommandValidator{

    private final CmdFormatRepository cmdFormatRepository;
    public CommandValidatorV1(CmdFormatRepository cmdFormatRepository) {
       this.cmdFormatRepository = cmdFormatRepository;
    }

    @Override
    public boolean isRightFormat(Command command) {
        Optional<CommandFormat> commandOpt = cmdFormatRepository.findByCmd(command.cmd());

        return commandOpt.map(cf -> cf.isRightFormat(command)).orElse(false);
    }

    @Override
    public boolean isWrongFormat(Command command) {
        return !isRightFormat(command);
    }

}
