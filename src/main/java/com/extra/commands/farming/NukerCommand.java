package com.extra.commands.farming;

import com.extra.utils.chatUtils.SendChat;
import com.extra.utils.getUtils.checkIfItemInInv;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import net.minecraft.init.Items;

import static com.extra.MITExtras.NukerEnabled;

public class NukerCommand extends Command {
    public NukerCommand() {
        super("nuker");
    }
    private boolean sendStopChat = true;

    @DefaultHandler
    public void handle() {
        NukerEnabled = !NukerEnabled;

        if (NukerEnabled) {
            boolean goldAxeInInv = checkIfItemInInv.checkIfItemInInv(Items.golden_axe);
            boolean diamondHoeInInv = checkIfItemInInv.checkIfItemInInv(Items.iron_hoe);
            boolean woodHoeInInv = checkIfItemInInv.checkIfItemInInv(Items.wooden_hoe);

            if (goldAxeInInv && (diamondHoeInInv || woodHoeInInv)) {
                SendChat.chat("§l§4[MINING IN TWO]§r Found all items in inventory! Nuker is enabled!");
                sendStopChat = true;
            } else {
                NukerEnabled = false;
                SendChat.chat("§l§4[MINING IN TWO]§r Some items are not in ur inventory! You need -> 1) golden axe, 2) diamond or wooden hoe! (run /continueNuker to start anyway)");
                sendStopChat = false;
            }
        }

        if (!NukerEnabled && sendStopChat) {
            SendChat.chat("§l§4[MINING IN TWO]§r Stopping nuker!");
        }
    }

}
