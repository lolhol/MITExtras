package com.extra.commands.farming;

import com.extra.utils.chatUtils.SendChat;
import com.extra.utils.getUtils.getBPS;
import com.extra.utils.getUtils.getCropType;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import static com.extra.MITExtras.cropNuker;

public class cropNukerCommand extends Command {

    public cropNukerCommand() {
        super("cropnuker");
    }

    public static Block cropType = Blocks.wheat;
    public static int BPS = 1;

    @DefaultHandler
    public void handle() {
        cropNuker = !cropNuker;

        cropType = getCropType.getCropType();
        BPS = getBPS.getBPS();

        String str = cropNuker ? "Crop Nuker Enabled!" : "Crop Nuker Disabled!";
        SendChat.chat("§l§4[MINING IN TWO]§r " + str);
    }
}
