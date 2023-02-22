package com.extra.commands;

import com.extra.config.MITconfig;
import com.extra.utils.SendChat;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import static com.extra.MITExtras.cropNuker;
import static com.extra.MITExtras.cropNukerType;

public class cropNukerCommand extends Command {

    public cropNukerCommand() {
        super("cropnuker");
    }

    @DefaultHandler
    public void handle() {
        cropNuker = !cropNuker;

        String str = cropNuker ? "Crop Nuker Enabled!" : "Crop Nuker Disabled!";
        SendChat.chat("§l§4[MINING IN TWO]§r " + str);
    }

}
