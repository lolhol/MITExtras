package com.extra.commands.tests;

import com.extra.utils.chatUtils.SendChat;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

import static com.extra.MITExtras.NukerTests;

public class nukerTestsCommand extends Command {
    public nukerTestsCommand() {
        super("nukertest1269");
    }

    @DefaultHandler
    public void handle() {
        NukerTests = !NukerTests;
        String str = NukerTests ? "Tests Nuker Enabled!" : "Tests Nuker Disabled!";
        SendChat.chat("§l§4[MINING IN TWO]§r " + str);
    }
}
