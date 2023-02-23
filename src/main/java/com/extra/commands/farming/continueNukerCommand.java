package com.extra.commands.farming;

import com.extra.utils.chatUtils.SendChat;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

import static com.extra.MITExtras.NukerEnabled;

public class continueNukerCommand extends Command {
    public continueNukerCommand() {
        super("continueNuker");
    }

    @DefaultHandler
    public void handle() {
        String str = "Ok, starting!";
        SendChat.chat("§l§4[MINING IN TWO]§r " + str);

        NukerEnabled = true;
    }

}
