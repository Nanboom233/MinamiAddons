package top.nanboom233.Features.World;

import net.minecraft.block.*;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import top.nanboom233.Handlers.TickHandler;
import top.nanboom233.Utils.WorldUtils;
import top.nanboom233.Utils.WorldUtils.EnumFacing;

import java.util.ArrayList;

import static top.nanboom233.MinamiAddons.config;
import static top.nanboom233.MinamiAddons.mc;

public class EasyPlaceFix {
    private static boolean povRecovery = false;
    private static boolean sneakRecovery = false;
    private static BlockState lastBlockState = null;
    private static BlockPos lastBlockPos = null;
    private static Vec3d lastHitVecIn = null;
    private static EnumFacing lastEnumFacing = null;
    private static final ArrayList<AdjustInfo> adjustList = new ArrayList<>();
    private static final int MAX_ADJUST_PER_TICK = 2;
    private static long lastAdjust = -1;
    private static final long ADJUST_AWAIT_TIME = 55L;

    public static void applyMovementPacket(BlockPos pos, BlockState state, Vec3d hitVecIn) {
        Block block = state.getBlock();
        setLastInfo(state, pos, hitVecIn);
        EnumFacing enumFacing = WorldUtils.getEnumFacing(state);
        if (enumFacing == null || mc.getNetworkHandler() == null || mc.player == null) {
            return;
        }
//        ChatUtils.debug("enumFacing:" + enumFacing.name, ChatUtils.MessageCategory.DEBUG);
//        ChatUtils.debug("before yaw: " + enumFacing.yaw + " ,before pitch: " + enumFacing.pitch, ChatUtils.MessageCategory.DEBUG);
//        ChatUtils.debug("after yaw: " + getPlacingYaw(block, enumFacing.yaw) + " ,after pitch: " + getPlacingPitch(block, enumFacing.pitch), ChatUtils.MessageCategory.DEBUG);
        if (WorldUtils.OPPOSITE_PLACING_BLOCKS.contains(block.getDefaultState().getBlock())) {
            enumFacing = enumFacing.getOpposite();
        }
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                enumFacing.yaw, enumFacing.pitch, mc.player.isOnGround()));
        povRecovery = true;
        lastEnumFacing = enumFacing;
    }

    public static BlockHitResult redirectPlacement(BlockHitResult hitResult) {
        BlockPos blockPos = hitResult.getBlockPos();
        Vec3d hitVecIn = hitResult.getPos();
        Direction direction = hitResult.getSide();
        boolean insideBlock = hitResult.isInsideBlock();

        if (povRecovery && mc.player != null && mc.getNetworkHandler() != null && lastEnumFacing != null) {
            Block block = lastBlockState.getBlock();

//          sneak when placing chest
            if (block instanceof ChestBlock) {
                mc.player.input.sneaking = true;
                mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(
                        mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
                sneakRecovery = true;

                for (Property<?> prop : lastBlockState.getProperties()) {
                    Comparable<?> val = lastBlockState.get(prop);
                    if (prop instanceof EnumProperty && "type".equals(prop.getName())) {
                        String chestType = val.toString();
                        switch (chestType) {
                            case "LEFT":
                                BlockPos leftBlockPos = WorldUtils.getLeftBlockPos(blockPos, lastEnumFacing);
                                if (mc.world != null
                                        && mc.world.getBlockState(leftBlockPos).getBlock() == Blocks.CHEST) {
//                                    System.err.println("left");
                                    return new BlockHitResult(WorldUtils.getBlockCorner(leftBlockPos),
                                            lastEnumFacing.getRight().direction, leftBlockPos, insideBlock);
                                }
                                break;
                            case "RIGHT":
                                BlockPos rightBlockPos = WorldUtils.getRightBlockPos(blockPos, lastEnumFacing);
                                if (mc.world != null
                                        && mc.world.getBlockState(rightBlockPos).getBlock() == Blocks.CHEST) {
//                                    System.err.println("right");
                                    return new BlockHitResult(WorldUtils.getBlockCorner(rightBlockPos),
                                            lastEnumFacing.getLeft().direction, rightBlockPos, insideBlock);
                                }
                                break;
                        }
//                        System.err.println("single");
                        return new BlockHitResult(WorldUtils.getUnderSurface(blockPos),
                                Direction.UP, blockPos, insideBlock);
                    }
                }
            }

            //fix door's hinge
            if (block instanceof DoorBlock) {
                for (Property<?> prop : lastBlockState.getProperties()) {
                    Comparable<?> val = lastBlockState.get(prop);
                    if (prop instanceof EnumProperty && "hinge".equals(prop.getName())) {
                        if ("left".equals(val.toString())) {
                            return new BlockHitResult(WorldUtils.getLeftSurface(blockPos, lastEnumFacing),
                                    direction, blockPos, insideBlock);
                        } else {
                            return new BlockHitResult(WorldUtils.getRightSurface(blockPos, lastEnumFacing),
                                    direction, blockPos, insideBlock);
                        }
                    }
                }
            }
        }
        return hitResult;
    }

    @Nullable
    public static Direction directionFix(BlockState schematicState, Direction originalDirection) {
//        System.err.println("old: " + originalDirection);
        Block block = schematicState.getBlock();
        EnumFacing enumFacing = WorldUtils.getEnumFacing(schematicState);
        Direction facing = null;
        if (enumFacing != null) {
            facing = enumFacing.direction;
        }

        if (block instanceof TrapdoorBlock &&
                (originalDirection == Direction.UP || originalDirection == Direction.DOWN)) {
            return null;
        }

//        System.err.println("required: " + facing);
        if (facing != null) {
            Direction newFacing = facing;
            if (top.nanboom233.Utils.WorldUtils.OPPOSITE_PLACING_BLOCKS.contains(block)) {
                newFacing = facing.getOpposite();
            }
//            System.err.println("new: " + newFacing);
            return newFacing;
        }
        return null;
    }

    public static void afterPlacement() {
        if (lastBlockState != null) {
            Block block = lastBlockState.getBlock();
            if (WorldUtils.ADJUST_PLACING_BLOCKS.contains(block)) {
                int adjustTimes = 0;
                if (block == Blocks.REPEATER) {
                    Property<?> prop = WorldUtils.getBlockPropertyByName(lastBlockState, "delay");
                    if (prop instanceof IntProperty) {
                        Comparable<?> val = lastBlockState.get(prop);
                        adjustTimes = Integer.parseInt(val.toString()) - 1;
                    }
                } else if (block == Blocks.COMPARATOR) {
                    Property<?> prop = WorldUtils.getBlockPropertyByName(lastBlockState, "mode");
                    if (prop instanceof EnumProperty) {
                        Comparable<?> val = lastBlockState.get(prop);
                        adjustTimes = "subtract".equals(val.toString()) ? 1 : 0;
                    }
                } else if (block instanceof TrapdoorBlock
                        || block instanceof FenceGateBlock
                        || block instanceof DoorBlock) {
                    Property<?> prop = WorldUtils.getBlockPropertyByName(lastBlockState, "open");
                    if (prop instanceof BooleanProperty) {
                        Comparable<?> val = lastBlockState.get(prop);
                        adjustTimes = "true".equals(val.toString()) ? 1 : 0;
                    }
                } else if (block == Blocks.NOTE_BLOCK) {
                    Property<?> prop = WorldUtils.getBlockPropertyByName(lastBlockState, "note");
                    System.out.println(prop);
                    if (prop instanceof IntProperty) {
                        Comparable<?> val = lastBlockState.get(prop);
                        adjustTimes = Integer.parseInt(val.toString());
                    }
                }
                if (adjustTimes != 0) {
                    adjustList.add(new AdjustInfo(lastBlockState, lastBlockPos, lastHitVecIn, lastEnumFacing, adjustTimes));
                }
            }
        }

        if (povRecovery && mc.player != null && mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                    mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround()));
            povRecovery = false;
        }

        if (sneakRecovery && mc.player != null && mc.getNetworkHandler() != null) {
            mc.player.input.sneaking = false;
            mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(
                    mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
        }

        resetLastInfo();
    }

    private static void resetLastInfo() {
        lastBlockState = null;
        lastBlockPos = null;
        lastHitVecIn = null;
        lastEnumFacing = null;
    }

    private static void setLastInfo(BlockState state, BlockPos pos, Vec3d hitVecIn) {
        lastBlockState = state;
        lastBlockPos = pos;
        lastHitVecIn = hitVecIn;
    }

    static {
        TickHandler.ITickTask adjustTask = (mc -> {
            if (!config.easyPlaceFix
                    || adjustList.isEmpty()
                    || System.currentTimeMillis() - lastAdjust < ADJUST_AWAIT_TIME
                    || mc.getNetworkHandler() == null) {
                return;
            }
            if (mc.interactionManager != null && mc.player != null) {
                int count = 0;
                for (AdjustInfo adjustInfo : adjustList) {
                    int current = Math.min(adjustInfo.adjustTimes, MAX_ADJUST_PER_TICK - count);
                    mc.player.input.sneaking = false;
                    mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(
                            mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
                    for (int i = 0; i < current; i++) {
                        BlockHitResult blockHitResult = new BlockHitResult(
                                adjustInfo.hitVecIn, Direction.NORTH, adjustInfo.blockPos, false);
                        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHitResult);
                    }
                    adjustInfo.adjustTimes -= current;
//                    ChatUtils.debug("interact " + current + " Times,left: " + adjustInfo.adjustTimes, ChatUtils.MessageCategory.DEBUG);
                    count += current;
                    if (count >= MAX_ADJUST_PER_TICK) {
                        break;
                    }
                }
            }
            adjustList.removeIf(adjustInfo -> adjustInfo.adjustTimes == 0);
            lastAdjust = System.currentTimeMillis();
        });
        TickHandler.getInstance().register(adjustTask);
    }

    public static class AdjustInfo {
        public BlockState blockState;
        public BlockPos blockPos;
        public Vec3d hitVecIn;
        public EnumFacing enumFacing;
        public int adjustTimes;

        public AdjustInfo(BlockState blockState, BlockPos blockPos, Vec3d hitVecIn, EnumFacing enumFacing, int adjustTimes) {
            this.blockState = blockState;
            this.blockPos = blockPos;
            this.hitVecIn = hitVecIn;
            this.enumFacing = enumFacing;
            this.adjustTimes = adjustTimes;
        }
    }
}
