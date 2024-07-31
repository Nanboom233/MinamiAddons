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
import top.nanboom233.Utils.WorldUtils;

import static top.nanboom233.MinamiAddons.mc;
import static top.nanboom233.Utils.WorldUtils.*;

//todo fix piston would failed
//todo fix trapdoor states
public class EasyPlaceFix {
    private static boolean povRecovery = false;
    private static boolean sneakRecovery = false;
    private static BlockState lastBlockState = null;
    private static BlockPos lastBlockPos = null;
    private static Vec3d lastHitVecIn = null;
    private static EnumFacing lastEnumFacing = null;

    public static void applyMovementPacket(BlockPos pos, BlockState state, Vec3d hitVecIn) {
        Block block = state.getBlock();
        lastBlockState = state;
        lastHitVecIn = hitVecIn;
        lastBlockPos = pos;
        Direction facing = WorldUtils.getFacingValue(state);
        if (facing != null) {
            EnumFacing enumFacing = EnumFacing.getByName(facing.asString());
            if (enumFacing == null || mc.getNetworkHandler() == null || mc.player == null) {
                return;
            }
//            ChatUtils.debug("before yaw: " + enumFacing.yaw + " ,before pitch: " + enumFacing.pitch, ChatUtils.MessageCategory.DEBUG);
//            ChatUtils.debug("after yaw: " + getPlacingYaw(block, enumFacing.yaw) + " ,after pitch: " + getPlacingPitch(block, enumFacing.pitch), ChatUtils.MessageCategory.DEBUG);
            if (OPPOSITE_PLACING_BLOCKS.contains(block.getDefaultState().getBlock())) {
                enumFacing = enumFacing.getOpposite();
            }
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                    enumFacing.yaw, enumFacing.pitch, mc.player.isOnGround()));
            povRecovery = true;
            lastEnumFacing = enumFacing;
        }
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
                                BlockPos leftBlockPos = getLeftBlockPos(blockPos, lastEnumFacing);
                                if (mc.world != null
                                        && mc.world.getBlockState(leftBlockPos).getBlock() == Blocks.CHEST) {
//                                    System.err.println("left");
                                    return new BlockHitResult(getBlockCorner(leftBlockPos),
                                            lastEnumFacing.getRight().direction, leftBlockPos, insideBlock);
                                }
                                break;
                            case "RIGHT":
                                BlockPos rightBlockPos = getRightBlockPos(blockPos, lastEnumFacing);
                                if (mc.world != null
                                        && mc.world.getBlockState(rightBlockPos).getBlock() == Blocks.CHEST) {
//                                    System.err.println("right");
                                    return new BlockHitResult(getBlockCorner(rightBlockPos),
                                            lastEnumFacing.getLeft().direction, rightBlockPos, insideBlock);
                                }
                                break;
                        }
//                        System.err.println("single");
                        return new BlockHitResult(getUnderSurface(blockPos),
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
                            return new BlockHitResult(getLeftSurface(blockPos, lastEnumFacing),
                                    direction, blockPos, insideBlock);
                        } else {
                            return new BlockHitResult(getRightSurface(blockPos, lastEnumFacing),
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
        Direction facing = top.nanboom233.Utils.WorldUtils.getFacingValue(schematicState);
//        System.err.println("required: " + facing);
        Block block = schematicState.getBlock();
        if (block == Blocks.CHAIN) {
            for (Property<?> prop : schematicState.getProperties()) {
                Comparable<?> val = schematicState.get(prop);
                if (prop instanceof EnumProperty) {
                    facing = switch (val.toString()) {
                        case "z" -> Direction.NORTH;
                        case "x" -> Direction.EAST;
                        case "y" -> Direction.DOWN;
                        default -> facing;
                    };
                }
            }
        }
        if (block instanceof TrapdoorBlock &&
                (originalDirection == Direction.UP || originalDirection == Direction.DOWN)) {
            return null;
        }
        if (facing != null) {
            Direction newFacing = facing;
            if (top.nanboom233.Utils.WorldUtils.OPPOSITE_PLACING_BLOCKS.contains(block)) {
                newFacing = switch (facing) {
                    case DOWN -> Direction.UP;
                    case UP -> Direction.DOWN;
                    case NORTH -> Direction.SOUTH;
                    case EAST -> Direction.WEST;
                    case SOUTH -> Direction.NORTH;
                    case WEST -> Direction.EAST;
                };
            }
//            System.err.println("new: " + newFacing);
            return newFacing;
        }
        return null;
    }

    public static void afterPlacement() {
        if (povRecovery && lastBlockState != null) {
            Block block = lastBlockState.getBlock();
            if (WorldUtils.ADJUST_PLACING_BLOCKS.contains(block)) {
                int adjustTimes = 0;
                if (block == Blocks.REPEATER) {
                    for (Property<?> prop : lastBlockState.getProperties()) {
                        Comparable<?> val = lastBlockState.get(prop);
                        if (prop instanceof IntProperty && "delay".equals(prop.getName())) {
                            adjustTimes = Integer.parseInt(val.toString()) - 1;
                            break;
                        }
                    }
                } else if (block == Blocks.COMPARATOR) {
                    for (Property<?> prop : lastBlockState.getProperties()) {
                        Comparable<?> val = lastBlockState.get(prop);
                        if (prop instanceof EnumProperty && "subtract".equals(val.toString())) {
                            adjustTimes = 1;
                            break;
                        }
                    }
                } else if (block instanceof TrapdoorBlock
                        || block instanceof FenceGateBlock
                        || block instanceof DoorBlock) {
                    for (Property<?> prop : lastBlockState.getProperties()) {
                        Comparable<?> val = lastBlockState.get(prop);
                        if (prop instanceof BooleanProperty && "open".equals(prop.getName()) && "true".equals(val.toString())) {
                            adjustTimes = 1;
                            break;
                        }
                    }
                }
                if (mc.interactionManager != null && mc.player != null) {
                    for (int i = 0; i < adjustTimes; i++) {
                        BlockHitResult blockHitResult = new BlockHitResult(
                                lastHitVecIn, Direction.NORTH, lastBlockPos, false);
//                        ChatUtils.debug("interact!", ChatUtils.MessageCategory.DEBUG);
                        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHitResult);
                    }
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
    }
}
