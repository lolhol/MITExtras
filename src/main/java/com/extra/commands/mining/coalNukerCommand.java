package com.extra.commands.mining;

import com.extra.data.MITconfig;
import com.extra.utils.chatUtils.SendChat;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class coalNukerCommand extends Command {
    public static boolean coalNukerIsOn = false;
    public static boolean coalVroomVroom = false;

    public coalNukerCommand() {
        super("coalnuker");
    }

    @DefaultHandler
    public void handle() {
        String str = null;

        if (MITconfig.coalVroomVroomMode) {
            coalNukerIsOn = !coalNukerIsOn;
            str = coalNukerIsOn ? "Coal Nuker Enabled!" : "Coal Nuker Disabled!";
        } else {
            coalVroomVroom = !coalVroomVroom;
            str = coalVroomVroom ? "Coal Nuker Enabled!" : "Coal Nuker Disabled!";
        }

        SendChat.chat("§l§4[MINING IN TWO]§r " + str);
    }
}
