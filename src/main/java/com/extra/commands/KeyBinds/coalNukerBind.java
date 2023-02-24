package com.extra.commands.KeyBinds;

import com.extra.commands.mining.coalNukerCommand;
import com.extra.utils.chatUtils.SendChat;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import static com.extra.MITExtras.cropNuker;

public class coalNukerBind extends KeyBinding {
    public coalNukerBind() {
        super("Start coal nuker", Keyboard.KEY_NONE, "MITExtras");
    }

    public void handle() {
        coalNukerCommand.coalNukerIsOn = !coalNukerCommand.coalNukerIsOn;

        String str = coalNukerCommand.coalNukerIsOn ? "Coal Nuker Enabled!" : "Coal Nuker Disabled!";
        SendChat.chat("§l§4[MINING IN TWO]§r " + str);
    }
}
