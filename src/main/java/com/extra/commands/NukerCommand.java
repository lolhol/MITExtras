package com.extra.commands;

import com.extra.utils.SendChat;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

import static com.extra.MITExtras.NukerEnabled;

public class NukerCommand extends Command {

    public NukerCommand() {
        super("nuker");
    }

    @DefaultHandler
    public void handle() {
        NukerEnabled = !NukerEnabled;
        String str = NukerEnabled ? "Nuker Enabled!" : "Nuker Disabled!";
        SendChat.chat("§l§4[MINING IN TWO]§r " + str);
    }

}
