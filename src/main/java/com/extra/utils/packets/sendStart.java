package com.extra.utils.packets;

import com.extra.utils.random.ids;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class sendStart {
    public static void sendStartPacket(BlockPos block, EnumFacing enumFacing) {
        ids.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, block, enumFacing));
        ids.mc.thePlayer.swingItem();
    }
}
