package com.extra.features;

import com.extra.config.MITconfig;
import com.extra.utils.BoxRenderer;
import com.extra.utils.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.extra.MITExtras.cropNuker;
import static com.extra.config.MITconfig.cropNukerType;
import static com.extra.config.MITconfig.*;

public class cropNuker {
    private final List<BlockPos> destroyedBlocks =  new ArrayList<BlockPos>();

    public static Minecraft mc = Minecraft.getMinecraft();

    private static BlockPos blockBeingNuked = null;

    public static int tickCount = MITconfig.cropNukerBPS * 2;
    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (cropNuker && event.phase == TickEvent.Phase.END) {

            int cropType = MITconfig.cropNukerType;
            Block crop = Blocks.wheat;
            switch (cropType) {
                case 1:
                    crop = Blocks.carrots;
                    break;

                case 2:
                    crop = Blocks.potatoes;
                    break;

                case 3:
                    crop = Blocks.nether_wart;
                    break;

                case 4:
                    crop = Blocks.pumpkin;
                    break;

                case 5:
                    crop = Blocks.melon_block;
                    break;

                case 6:
                    crop = Blocks.cocoa;
                    break;
            }

            if (getBlocksMain(crop) != null && tickCount >= MITconfig.cropNukerBPS * 2) {
                BlockPos cropCords = getBlocksMain(crop);

                MovingObjectPosition fake = mc.objectMouseOver;
                fake.hitVec = new Vec3(cropCords);
                EnumFacing enumFacing = fake.sideHit;

                if (destroyedBlocks.size() >= 30) {
                    destroyedBlocks.clear();
                }

                if (!destroyedBlocks.contains(cropCords) && canReachBlock(cropCords, nukerRange)) {
                    blockBeingNuked = cropCords;

                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, cropCords, enumFacing));
                    mc.thePlayer.swingItem();

                    destroyedBlocks.add(cropCords);
                }

                tickCount = 0;
            }

            tickCount++;
        } else {
            blockBeingNuked = null;
        }
    }

    //Find Block (ik this is like the worst way to get blocks but idc!)

    public BlockPos getBlocksMain(Block type) {
        double r = 6;
        BlockPos playerPos = mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3 playerVec = mc.thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);

        ArrayList<Vec3> warts = new ArrayList<>();

        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = mc.theWorld.getBlockState(blockPos);

                if (blockState.getBlock() == type) {
                    if (!destroyedBlocks.contains(blockPos)) {
                        warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    }
                }
            }
        }

        double smallest = 9999;
        Vec3 closest = null;
        for (Vec3 wart : warts) {
            double dist = wart.distanceTo(playerVec);
            if (dist < smallest) {
                smallest = dist;
                closest = wart;
            }
        }

        if (closest != null && smallest < 5) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }

        return null;
    }

    /*public canReachBlock(BlockPos pos, int range) {
        double r = 6;
        BlockPos playerPos = mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3 playerVec = mc.thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);

        ArrayList<Vec3> warts = new ArrayList<>();

        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = mc.theWorld.getBlockState(blockPos);

                if (blockState.getBlock() == Blocks.nether_wart || blockState.getBlock() == Blocks.potatoes || blockState.getBlock() == Blocks.wheat || blockState.getBlock() == Blocks.carrots || blockState.getBlock() == Blocks.pumpkin || blockState.getBlock() == Blocks.melon_block || blockState.getBlock() == Blocks.brown_mushroom || blockState.getBlock() == Blocks.red_mushroom || blockState.getBlock() == Blocks.cocoa) {
                    if (!destroyedBlocks.contains(blockPos)) {
                        warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    }
                }
            }
        }

        double smallest = 9999;
        Vec3 closest = null;
        for (Vec3 wart : warts) {
            double dist = wart.distanceTo(playerVec);
            if (dist < smallest) {
                smallest = dist;
                closest = wart;
            }
        }
        if (closest != null && smallest < 5) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }

        return null;
    }*/

    //Block Nuked ESP

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (MITconfig.currentNukedBlockESPSwitch && blockBeingNuked != null) {
            BoxRenderer.drawOutlinedBoundingBox(blockBeingNuked, Color.RED, 3, event.partialTicks);
        }
    }

    public static boolean canReachBlock(BlockPos pos, float range) {
        AxisAlignedBB aabb = getBlockAABB(pos);
        return isInterceptable(PlayerUtil.getPositionEyes(), getMiddleOfAABB(aabb), aabb, range);
    }

    public static AxisAlignedBB getBlockAABB(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        block.setBlockBoundsBasedOnState((IBlockAccess) mc.theWorld, pos);
        return block.getSelectedBoundingBox((World) mc.theWorld, pos);
    }

    public static boolean isInterceptable(Vec3 start, Vec3 goal, AxisAlignedBB aabb, float range) {
        if (start.squareDistanceTo(goal) > (range * range))
            return false;
        return isInterceptable(start, goal, aabb);
    }

    public static boolean isInterceptable(Vec3 start, Vec3 goal, AxisAlignedBB aabb) {
        return (isVecInYZ(start.getIntermediateWithXValue(goal, aabb.minX), aabb) || isVecInYZ(start.getIntermediateWithXValue(goal, aabb.maxX), aabb) || isVecInXZ(start.getIntermediateWithYValue(goal, aabb.minY), aabb) || isVecInXZ(start.getIntermediateWithYValue(goal, aabb.maxY), aabb) || isVecInXY(start.getIntermediateWithZValue(goal, aabb.minZ), aabb) || isVecInXY(start.getIntermediateWithZValue(goal, aabb.maxZ), aabb));
    }

    public static boolean isVecInYZ(Vec3 vec, AxisAlignedBB aabb) {
        return (vec != null && vec.yCoord >= aabb.minY && vec.yCoord <= aabb.maxY && vec.zCoord >= aabb.minZ && vec.zCoord <= aabb.maxZ);
    }

    public static boolean isVecInXY(Vec3 vec, AxisAlignedBB aabb) {
        return (vec != null && vec.xCoord >= aabb.minX && vec.xCoord <= aabb.maxX && vec.yCoord >= aabb.minY && vec.yCoord <= aabb.maxY);
    }

    public static boolean isVecInXZ(Vec3 vec, AxisAlignedBB aabb) {
        return (vec != null && vec.xCoord >= aabb.minX && vec.xCoord <= aabb.maxX && vec.zCoord >= aabb.minZ && vec.zCoord <= aabb.maxZ);
    }

    public static Vec3 getMiddleOfAABB(AxisAlignedBB aabb) {
        return new Vec3((aabb.maxX + aabb.minX) / 2.0D, (aabb.maxY + aabb.minY) / 2.0D, (aabb.maxZ + aabb.minZ) / 2.0D);
    }
}
