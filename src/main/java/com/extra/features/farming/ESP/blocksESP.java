package com.extra.features.farming.ESP;

import com.extra.data.BlockArrays;
import com.extra.data.MITconfig;
import com.extra.utils.random.BoxRenderer;
import com.extra.utils.random.ids;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class blocksESP {

    private List<BlockPos> ESPBlocks = new ArrayList<BlockPos>();

    private int range;
    private Block[] arrayType;

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        boolean state = MITconfig.ESPState;

        if (state) {
            switch (MITconfig.ESPType) {
                case 0:
                    range = 0;
                    break;


                case 1:
                    range = MITconfig.ESPDist;
                    arrayType = BlockArrays.coalBlocks;
                    break;


                case 2:
                    range = MITconfig.ESPDist;
                    arrayType = BlockArrays.gardenBlocks;
                    break;
            }

            if (range > 0) {
                ESPBlocks.clear();

                getBlocksESP(range, Arrays.asList(arrayType));

                ESPBlocks.forEach(blockPos -> {
                    BoxRenderer.drawOutlinedBoundingBox(blockPos, Color.CYAN, 3, event.partialTicks);});
            }
        }
    }

    public void getBlocksESP(int range, List<Block> types) {
        BlockPos playerPos = ids.mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3i vec3i = new Vec3i(range, range, range);

        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = ids.mc.theWorld.getBlockState(blockPos);

                types.forEach(type -> {
                    if (blockState.getBlock() == type) {
                        ESPBlocks.add(new BlockPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    }
                });
            }
        }
    }
}
