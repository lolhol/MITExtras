package com.extra.features.farming;

import com.extra.commands.farming.cropNukerCommand;
import com.extra.data.currentNukedBlock;
import com.extra.utils.getBlocks.getBlocksInRange;
import com.extra.utils.packets.sendStart;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.extra.MITExtras.cropNuker;
import static com.extra.commands.farming.cropNukerCommand.cropType;
import static com.extra.data.MITconfig.nukerRange;

public class cropNuker {
    private final Set<BlockPos> destroyedBlocks = ConcurrentHashMap.newKeySet();


    public static int tickCount = cropNukerCommand.BPS;
    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (cropNuker && event.phase == TickEvent.Phase.END) {
            int range = nukerRange;
            Block[] crop = {cropType};

            if (getBlocksInRange.getBlocksMain(range, destroyedBlocks, crop) != null) {

                if (tickCount >= cropNukerCommand.BPS) {
                    BlockPos cropCords = getBlocksInRange.getBlocksMain(range, destroyedBlocks, crop);

                    if (destroyedBlocks.size() >= 100) {
                        destroyedBlocks.clear();
                    }

                    sendStart.sendStartPacket(cropCords, EnumFacing.DOWN);

                    currentNukedBlock.blockBeingNuked = cropCords;

                    destroyedBlocks.add(cropCords);

                    tickCount = 0;
                }

                tickCount++;
            } else {
                currentNukedBlock.blockBeingNuked = null;
            }
        }
    }
}
