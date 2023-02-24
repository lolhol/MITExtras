package com.extra.features.mining.coalNuker;

import com.extra.commands.farming.cropNukerCommand;
import com.extra.data.currentNukedBlock;
import com.extra.utils.getBlocks.getBlocksInRange;
import com.extra.utils.packets.getBlockEnum;
import com.extra.utils.packets.sendStart;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.extra.commands.mining.coalNukerCommand.coalVroomVroom;
import static com.extra.data.MITconfig.nukerRange;

public class coalNukerVroomVroom {
    private final Set<BlockPos> destroyedBlocks = ConcurrentHashMap.newKeySet();
    public static int tickCount = cropNukerCommand.BPS;
    private final Block[] blockType = {Blocks.coal_ore} ;

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (coalVroomVroom && event.phase == TickEvent.Phase.END) {
            int range = nukerRange;

            if (getBlocksInRange.getBlocksMain(range, destroyedBlocks, blockType) != null) {

                if (tickCount >= 1) {
                    BlockPos coalBlockPos = getBlocksInRange.getBlocksMain(range, destroyedBlocks, blockType);

                    if (destroyedBlocks.size() >= 20) {
                        destroyedBlocks.clear();
                    }

                    sendStart.sendStartPacket(coalBlockPos, getBlockEnum.getEnum(coalBlockPos));

                    currentNukedBlock.blockBeingNuked = coalBlockPos;

                    destroyedBlocks.add(coalBlockPos);

                    tickCount = 0;
                }

                tickCount++;
            } else {
                currentNukedBlock.blockBeingNuked = null;
            }
        }
    }
}
