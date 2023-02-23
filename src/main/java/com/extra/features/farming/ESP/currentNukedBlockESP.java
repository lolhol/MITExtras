package com.extra.features.farming.ESP;

import com.extra.data.MITconfig;
import com.extra.data.currentNukedBlock;
import com.extra.utils.random.BoxRenderer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class currentNukedBlockESP {
    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (MITconfig.currentNukedBlockESPSwitch && currentNukedBlock.blockBeingNuked != null) {
            BoxRenderer.drawOutlinedBoundingBox(currentNukedBlock.blockBeingNuked, Color.RED, 3, event.partialTicks);
        }
    }
}
