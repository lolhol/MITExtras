package com.extra.features.mining;

import com.extra.data.currentNukedBlock;
import com.extra.utils.getBlocks.getBlocksInRange;
import com.extra.utils.packets.getBlockEnum;
import com.extra.utils.packets.sendStart;
import com.extra.utils.packets.sendStop;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.extra.commands.mining.coalNukerCommand.coalNukerIsOn;
import static com.extra.data.MITconfig.nukerRange;

public class coalNuker {
    private final Set<BlockPos> destroyedBlocks = ConcurrentHashMap.newKeySet();

    private BlockPos tickPos;
    private boolean nextTick = false;
    private int tickCounter = 0;
    private boolean sent = false;
    private final Block[] blockType = {Blocks.coal_ore} ;
    private BlockPos coalBlock;

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (coalNukerIsOn) {
            int range = nukerRange;

            if (tickCounter == 0) {
                coalBlock = getBlocksInRange.getBlocksMain(range, destroyedBlocks, blockType);
            }

            if (coalBlock != null) {
                if (nextTick && tickCounter >= 4) {
                    sendStop.sendStopPacket(tickPos, getBlockEnum.getEnum(tickPos));
                    nextTick = false;
                    sent = false;
                    tickCounter = 0;
                }
                if (destroyedBlocks.size() > 10){
                    destroyedBlocks.clear();
                }


                if (!sent) {
                    sendStart.sendStartPacket(coalBlock, getBlockEnum.getEnum(coalBlock));
                    currentNukedBlock.blockBeingNuked = coalBlock;

                    nextTick = true;
                    tickPos = coalBlock;

                    sent = true;
                    //destroyedBlocks.add(coalBlock);
                } else {
                    tickCounter++;
                }

            } else {
                currentNukedBlock.blockBeingNuked = null;
            }
        }
    }
}
