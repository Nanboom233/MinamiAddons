package top.nanboom233.Features.World;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import top.nanboom233.Utils.Texts.ChatUtils;
import top.nanboom233.Utils.WorldUtils;

import static top.nanboom233.MinamiAddons.mc;

public class EasyPlaceFix {
    private static boolean required = false;
    private static BlockState lastBlockState = null;
    private static BlockPos lastBlockPos = null;
    private static Vec3d lastHitVecIn = null;

    public static void beforePlacement(BlockPos pos, BlockState state, Vec3d hitVecIn) {
        Block block = state.getBlock();
        lastBlockState = state;
        lastHitVecIn = hitVecIn;
        lastBlockPos = pos;
        Direction facing = WorldUtils.getFacingValue(state);
        WorldUtils.EnumFacing axis = null;
        if (facing != null || axis != null) {
            WorldUtils.EnumFacing enumFacing;
            if (axis != null) {
                enumFacing = axis;
            } else {
                enumFacing = WorldUtils.EnumFacing.getByName(facing.asString());
            }
            if (enumFacing == null || mc.getNetworkHandler() == null || mc.player == null) {
                return;
            }
//            ChatUtils.showInChat(String.valueOf(WorldUtils.EnumFacing.getPlacingYaw(block, enumFacing.yaw)), ChatUtils.MessageCategory.DEBUG);
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                    WorldUtils.EnumFacing.getPlacingYaw(block, enumFacing.yaw),
                    enumFacing.pitch, mc.player.isOnGround()));
            required = true;
        }
    }

    @Nullable
    public static Direction directionFix(BlockState state, Direction originalDirection) {
//        System.out.println("old : " + originalDirection);
        Direction facing = top.nanboom233.Utils.WorldUtils.getFacingValue(state);
        Block block = state.getBlock();
        if (block == Blocks.CHAIN) {
            for (Property<?> prop : state.getProperties()) {
                Comparable<?> val = state.get(prop);
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
                    case DOWN -> Direction.DOWN;
                    case UP -> Direction.UP;
                    case NORTH -> Direction.SOUTH;
                    case EAST -> Direction.WEST;
                    case SOUTH -> Direction.NORTH;
                    case WEST -> Direction.EAST;
                };
            }
//            System.err.println("new :" + newFacing);
            return newFacing;
        }
        return null;
    }

    public static void afterPlacement() {
        if (required && lastBlockState != null) {
            Block block = lastBlockState.getBlock();
            if (block == Blocks.REPEATER || block == Blocks.COMPARATOR) {
                int adjustTimes = 0;
                if (block == Blocks.REPEATER) {
                    for (Property<?> prop : lastBlockState.getProperties()) {
                        Comparable<?> val = lastBlockState.get(prop);
                        if (prop instanceof IntProperty && "delay".equals(prop.getName())) {
                            adjustTimes = Integer.parseInt(val.toString()) - 1;
                            break;
                        }
                    }
                }
                if (block == Blocks.COMPARATOR) {
                    for (Property<?> prop : lastBlockState.getProperties()) {
                        Comparable<?> val = lastBlockState.get(prop);
                        if (prop instanceof EnumProperty && "subtract".equals(val.toString())) {
                            adjustTimes = 1;
                            break;
                        }
                    }
                }
                if (mc.interactionManager != null && mc.player != null) {
                    for (int i = 0; i < adjustTimes; i++) {
                        BlockHitResult blockHitResult = new BlockHitResult(
                                lastHitVecIn, Direction.NORTH, lastBlockPos, false);
                        ChatUtils.debug("interact!", ChatUtils.MessageCategory.DEBUG);
                        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHitResult);
                    }
                }
            }
        }
        if (required && mc.player != null && mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                    mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround()));
            required = false;
        }
    }
}
