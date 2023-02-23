package com.extra.commands.farming;

import com.extra.data.MITconfig;
import com.extra.utils.chatUtils.SendChat;
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

    @DefaultHandler
    public void handle() {
        cropNuker = !cropNuker;

        cropType = getCropType();

        String str = cropNuker ? "Crop Nuker Enabled!" : "Crop Nuker Disabled!";
        SendChat.chat("§l§4[MINING IN TWO]§r " + str);
    }

    private Block getCropType() {
        int cropType = MITconfig.cropNukerType;
        Block crop = Blocks.wheat;
        if (cropType == 1) {
            crop = Blocks.carrots;
        } else if (cropType == 2) {
            crop = Blocks.potatoes;
        } else if (cropType == 3) {
            crop = Blocks.nether_wart;
        } else if (cropType == 4) {
            crop = Blocks.pumpkin;
        } else if (cropType == 5) {
            crop = Blocks.melon_block;
        } else if (cropType == 6) {
            crop = Blocks.cocoa;
        }

        return crop;
    }

}
