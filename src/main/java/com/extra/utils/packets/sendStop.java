package com.extra.utils.packets;

import com.extra.utils.random.ids;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class sendStop {
    public static void sendStopPacket(BlockPos block, EnumFacing enumFacing) {
        ids.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, block, enumFacing));
    }
}
