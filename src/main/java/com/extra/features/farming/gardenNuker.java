package com.extra.features.farming;

import com.extra.data.BlockArrays;
import com.extra.data.currentNukedBlock;
import com.extra.utils.checks.canReach;
import com.extra.utils.getBlocks.getBlocksInRange;
import com.extra.utils.packets.getBlockEnum;
import com.extra.utils.packets.sendStart;
import com.extra.utils.packets.sendStop;
import com.extra.utils.random.ids;
import com.extra.utils.random.swapToSlot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.extra.MITExtras.NukerEnabled;
import static com.extra.data.MITconfig.nukerRange;

public class gardenNuker {
    private final Set<BlockPos> destroyedBlocks = ConcurrentHashMap.newKeySet();

    private boolean nextTick = false;
    private boolean sent = false;
    private BlockPos tickPos;
    private int tickCounter = 0;

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (NukerEnabled) {
            int range = nukerRange;
            BlockPos gardenBlock = getBlocksInRange.getBlocksMain(range, destroyedBlocks, BlockArrays.gardenBlocks);

            if (gardenBlock != null) {
                currentNukedBlock.blockBeingNuked = gardenBlock;

                if (nextTick && tickCounter == 5) {
                    sendStop.sendStopPacket(tickPos, getBlockEnum.getEnum(tickPos));
                    nextTick = false;
                    sent = false;
                    tickCounter = 0;
                }

                if (destroyedBlocks.size() > 10) {
                    destroyedBlocks.clear();
                }

                if (canReach.canReachBlock(gardenBlock, range) || event.phase == TickEvent.Phase.END) {
                    IBlockState getBlockState = ids.mc.theWorld.getBlockState(gardenBlock);

                    Arrays.asList(BlockArrays.gardenBlocks).forEach(block -> {
                        if (getBlockState.getBlock() == block) {
                            swapToSlot.swapToSlot(Items.wooden_hoe);
                            swapToSlot.swapToSlot(Items.iron_hoe);

                            if (block == Blocks.log) {
                                swapToSlot.swapToSlot(Items.golden_axe);
                            }
                        }
                    });

                    if (!sent) {
                        sendStart.sendStartPacket(gardenBlock, getBlockEnum.getEnum(gardenBlock));

                        nextTick = true;
                        tickPos = gardenBlock;

                        sent = true;
                        destroyedBlocks.add(gardenBlock);
                    }

                    tickCounter++;
                }
            } else {
                currentNukedBlock.blockBeingNuked = null;
            }
        }
    }

    }
