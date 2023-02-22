package com.extra.features;

import com.extra.config.MITconfig;
import com.extra.utils.BoxRenderer;
import com.extra.utils.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.network.play.client.C07PacketPlayerDigging;

import static com.extra.MITExtras.NukerEnabled;
import static com.extra.config.MITconfig.turnOnESPRange;
import static com.extra.config.MITconfig.grassESPRange;
import static com.extra.config.MITconfig.nukerRange;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class gardenNuker {
    private final List<BlockPos> ESPBlocks = new ArrayList<BlockPos>();

    private final Set<BlockPos> destroyedBlocks = ConcurrentHashMap.newKeySet();
    public static Minecraft mc = Minecraft.getMinecraft();

    boolean readyToScan = true;

    private BlockPos tickPos;

    private boolean nextTick = false;
    private int tickCounter = 0;
    private boolean sent = false;

    private BlockPos blockBeingNuked;

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (NukerEnabled) {
            if (getBlocksMain() != null) {
                BlockPos gardenBlock = getBlocksMain();
                blockBeingNuked = gardenBlock;

                if (nextTick && tickCounter == 5) {
                    sendStopPacket(tickPos);
                    nextTick = false;
                    sent = false;
                    tickCounter = 0;
                }

                if (destroyedBlocks.size() > 10) {
                    destroyedBlocks.clear();
                }

                int range = nukerRange;

                if (!destroyedBlocks.contains(gardenBlock) || canReachBlock(gardenBlock, range) || event.phase == TickEvent.Phase.END) {
                    IBlockState getBlockState = mc.theWorld.getBlockState(gardenBlock);

                    if (getBlockState.getBlock() == Blocks.tallgrass || getBlockState.getBlock() == Blocks.double_plant || getBlockState.getBlock() == Blocks.red_flower || getBlockState.getBlock() == Blocks.yellow_flower || getBlockState.getBlock() == Blocks.leaves) {
                        swapToSlot(Items.wooden_hoe);
                    } else if (getBlockState.getBlock() == Blocks.log) {
                        swapToSlot(Items.golden_axe);
                    } else {
                        swapToSlot(Items.golden_pickaxe);
                    }

                    if (!sent) {
                        sendStartPacket(gardenBlock);

                        nextTick = true;
                        tickPos = gardenBlock;

                        sent = true;
                        destroyedBlocks.add(gardenBlock);
                    }

                    tickCounter++;
                }
            }
        }
    }

    public EnumFacing getEnum(BlockPos block) {
        MovingObjectPosition fake = mc.objectMouseOver;
        fake.hitVec = new Vec3(block);
        EnumFacing enumFacing = fake.sideHit;

        return enumFacing;
    }

    public void sendStopPacket(BlockPos block) {
        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, block, getEnum(block)));
    }

    public void sendStartPacket(BlockPos block) {
        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, block, getEnum(block)));
        mc.thePlayer.swingItem();
    }

    public BlockPos getBlocksMain() {
        double r = 6;
        BlockPos playerPos = mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3 playerVec = mc.thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);

        ArrayList<Vec3> warts = new ArrayList<>();

        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = mc.theWorld.getBlockState(blockPos);

                if (blockState.getBlock() == Blocks.tallgrass || blockState.getBlock() == Blocks.double_plant|| blockState.getBlock() == Blocks.red_flower || blockState.getBlock() == Blocks.yellow_flower || blockState.getBlock() == Blocks.log || blockState.getBlock() == Blocks.leaves) {
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

        blockBeingNuked = null;
        return null;
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

    public static void swapToSlot(Item item) {
        for (int i = 0; i < 8; i++) {
            ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
            if (stack != null && stack.getItem() == item) {
                mc.thePlayer.inventory.currentItem = i;
                break;
            }
        }
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

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        readyToScan = true;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (turnOnESPRange) {
            ESPBlocks.clear();
            getBlocksESP(grassESPRange);

            ESPBlocks.forEach(blockPos -> {BoxRenderer.drawOutlinedBoundingBox(blockPos, Color.CYAN, 3, event.partialTicks);});
        }

        if (MITconfig.currentNukedBlockESPSwitch && blockBeingNuked != null) {
            BoxRenderer.drawOutlinedBoundingBox(blockBeingNuked, Color.RED, 3, event.partialTicks);
        }
    }

    public void getBlocksESP(int range) {
        int x = (int) mc.thePlayer.posX;
        int y = (int) mc.thePlayer.posY;
        int z = (int) mc.thePlayer.posZ;
        for (int i = x - range; i <= x + range; i++) {
            for (int j = y - range; j <= y + range; j++) {
                for (int k = z - range; k <= z + range; k++) {
                    IBlockState blockState = mc.theWorld.getBlockState(new BlockPos(i, j, k));
                    if (blockState.getBlock() == Blocks.tallgrass || blockState.getBlock() == Blocks.double_plant|| blockState.getBlock() == Blocks.red_flower || blockState.getBlock() == Blocks.yellow_flower || blockState.getBlock() == Blocks.log || blockState.getBlock() == Blocks.leaves) {
                        ESPBlocks.add(new BlockPos(i, j, k));
                    }
                }
            }
        }
    }

    }

