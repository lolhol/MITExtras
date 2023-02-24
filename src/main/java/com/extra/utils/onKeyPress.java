package com.extra.utils;

import com.extra.commands.NukerCommand;
import com.extra.commands.farming.cropNukerCommand;
import com.extra.commands.mining.coalNukerCommand;
import com.extra.MITExtras;
import com.extra.data.MITconfig;
import com.extra.utils.chatUtils.SendChat;
import com.extra.utils.random.checkIfItemInInv;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import static com.extra.MITExtras.NukerEnabled;
import static com.extra.MITExtras.cropNuker;

public class onKeyPress {
    @SubscribeEvent
    public void keyPress(InputEvent.KeyInputEvent event) {
        if (MITExtras.keybinds.get(0).isPressed()) {
            coalNukerCommand.coalNukerIsOn = !coalNukerCommand.coalNukerIsOn;

            String str = coalNukerCommand.coalNukerIsOn ? "Coal Nuker Enabled!" : "Coal Nuker Disabled!";
            SendChat.chat("§l§4[MINING IN TWO]§r " + str);
        }

        if (MITExtras.keybinds.get(1).isPressed()) {
            boolean sendStopChat = true;

            NukerEnabled = !NukerEnabled;

            if (NukerEnabled) {
                boolean goldAxeInInv = checkIfItemInInv.checkIfItemInInv(Items.golden_axe);
                boolean diamondHoeInInv = checkIfItemInInv.checkIfItemInInv(Items.iron_hoe);
                boolean woodHoeInInv = checkIfItemInInv.checkIfItemInInv(Items.wooden_hoe);

                if (goldAxeInInv && (diamondHoeInInv || woodHoeInInv)) {
                    SendChat.chat("§l§4[MINING IN TWO]§r Found all items in inventory! Nuker is enabled!");
                    NukerEnabled = true;
                } else {
                    sendStopChat = false;
                    SendChat.chat("§l§4[MINING IN TWO]§r Some items are not in ur inventory! You need -> 1) golden axe, 2) diamond or wooden hoe! (run /continueNuker to start anyway)");
                    NukerEnabled = false;
                }
            }

            if (!NukerEnabled && sendStopChat) {
                SendChat.chat("§l§4[MINING IN TWO]§r Stopping nuker!");
            }
        }

        if (MITExtras.keybinds.get(2).isPressed()) {
            cropNukerCommand.cropType = Blocks.wheat;

            cropNuker = !cropNuker;

            cropNukerCommand.cropType = getCropType();

            String str = cropNuker ? "Crop Nuker Enabled!" : "Crop Nuker Disabled!";
            SendChat.chat("§l§4[MINING IN TWO]§r " + str);
        }
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
