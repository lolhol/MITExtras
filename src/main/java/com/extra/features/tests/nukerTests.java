package com.extra.features.tests;

public class nukerTests {
    /*private final List<BlockPos> destroyedBlocks =  new ArrayList<BlockPos>();

    //PS: DO NOT MIND THIS, IT IS OLD VERSION ! (i just did some tests)

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (NukerTests && event.phase == TickEvent.Phase.END) {
            int cropType = MITconfig.cropNukerType;
            Block crop = Blocks.wheat;
            if (cropType == 1) {
                crop = Blocks.carrots;
            } else if (cropType == 2) {
                crop = Blocks.potatoes;
            } else if (cropType == 3) {
                crop = Blocks.nether_wart;
            } else if (cropType == 4) {
                crop = Blocks.pumpkin;
            } else if (cropType == 5) {
                crop = Blocks.melon_block;
            } else if (cropType == 6) {
                crop = Blocks.cocoa;
            }

            BlockPos cropCords = getBlocksMain(crop);

            MovingObjectPosition fake = ids.mc.objectMouseOver;
            fake.hitVec = new Vec3(cropCords);
            EnumFacing enumFacing = fake.sideHit;

            if (destroyedBlocks.size() >= 40) {
                destroyedBlocks.clear();
            }

            if (!destroyedBlocks.contains(cropCords) && canReachBlock(cropCords, nukerRange)) {
                ids.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, cropCords, enumFacing));
                ids.mc.thePlayer.swingItem();

                destroyedBlocks.add(cropCords);
            }
        }
    }

    public BlockPos getBlocksMain(Block type) {
        double r = 5;
        BlockPos playerPos = ids.mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3 playerVec = ids.mc.thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);

        ArrayList<Vec3> warts = new ArrayList<>();

        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = ids.mc.theWorld.getBlockState(blockPos);

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

    public canReachBlock(BlockPos pos, int range) {
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
    }

    //Block Nuked ESP

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (MITconfig.currentNukedBlockESPSwitch) {
            BoxRenderer.drawOutlinedBoundingBox(blockBeingNuked, Color.RED, 3, event.partialTicks);
        }
    }

    public static boolean canReachBlock(BlockPos pos, float range) {
        AxisAlignedBB aabb = getBlockAABB(pos);
        return isInterceptable(PlayerUtil.getPositionEyes(), getMiddleOfAABB(aabb), aabb, range);
    }

    public static AxisAlignedBB getBlockAABB(BlockPos pos) {
        Block block = ids.mc.theWorld.getBlockState(pos).getBlock();
        block.setBlockBoundsBasedOnState((IBlockAccess) ids.mc.theWorld, pos);
        return block.getSelectedBoundingBox((World) ids.mc.theWorld, pos);
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
    }*/
}
