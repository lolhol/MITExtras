package com.extra.commands;

import com.extra.config.MITconfig;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.utils.GuiUtil;

import java.util.Objects;

public class ExtrasCommand extends Command {

    public ExtrasCommand() {
        super("mitextras");
    }

    @DefaultHandler
    public void handle() {
        GuiUtil.open(Objects.requireNonNull(MITconfig.INSTANCE.gui()));
    }

}
