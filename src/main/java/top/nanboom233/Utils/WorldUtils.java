package top.nanboom233.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class WorldUtils {
    public static final HashSet<Block> OPPOSITE_PLACING_BLOCKS = new HashSet<>(Set.of(
            //chests
            Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST,

            //pistons
            Blocks.PISTON, Blocks.STICKY_PISTON,

            //all trapdoors
            Blocks.COPPER_TRAPDOOR, Blocks.EXPOSED_COPPER_TRAPDOOR, Blocks.OXIDIZED_COPPER_TRAPDOOR, Blocks.WEATHERED_COPPER_TRAPDOOR, Blocks.WAXED_COPPER_TRAPDOOR, Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR, Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR, Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR, Blocks.WARPED_TRAPDOOR, Blocks.IRON_TRAPDOOR, Blocks.OAK_TRAPDOOR, Blocks.SPRUCE_TRAPDOOR, Blocks.BIRCH_TRAPDOOR, Blocks.JUNGLE_TRAPDOOR, Blocks.ACACIA_TRAPDOOR, Blocks.CHERRY_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR, Blocks.MANGROVE_TRAPDOOR, Blocks.BAMBOO_TRAPDOOR,

            //redstones
            Blocks.REPEATER, Blocks.COMPARATOR, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER, Blocks.CRAFTER,

            //furnace
            Blocks.FURNACE

    ));

    public static final HashSet<Block> ADJUST_PLACING_BLOCKS = new HashSet<>(Set.of(
            //redstones
            Blocks.REPEATER, Blocks.COMPARATOR,

            //all trapdoors except iron trapdoor
            Blocks.COPPER_TRAPDOOR, Blocks.EXPOSED_COPPER_TRAPDOOR, Blocks.OXIDIZED_COPPER_TRAPDOOR, Blocks.WEATHERED_COPPER_TRAPDOOR, Blocks.WAXED_COPPER_TRAPDOOR, Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR, Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR, Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR, Blocks.WARPED_TRAPDOOR, Blocks.OAK_TRAPDOOR, Blocks.SPRUCE_TRAPDOOR, Blocks.BIRCH_TRAPDOOR, Blocks.JUNGLE_TRAPDOOR, Blocks.ACACIA_TRAPDOOR, Blocks.CHERRY_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR, Blocks.MANGROVE_TRAPDOOR, Blocks.BAMBOO_TRAPDOOR,

            //all doors except iron door
            Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR, Blocks.BIRCH_DOOR, Blocks.JUNGLE_DOOR, Blocks.ACACIA_DOOR, Blocks.CHERRY_DOOR, Blocks.DARK_OAK_DOOR, Blocks.MANGROVE_DOOR, Blocks.BAMBOO_DOOR, Blocks.CRIMSON_DOOR, Blocks.WARPED_DOOR, Blocks.COPPER_DOOR, Blocks.EXPOSED_COPPER_DOOR, Blocks.OXIDIZED_COPPER_DOOR, Blocks.WEATHERED_COPPER_DOOR, Blocks.WAXED_COPPER_DOOR, Blocks.WAXED_EXPOSED_COPPER_DOOR, Blocks.WAXED_OXIDIZED_COPPER_DOOR, Blocks.WAXED_WEATHERED_COPPER_DOOR,

            //all fence gates
            Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.CHERRY_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.MANGROVE_FENCE_GATE, Blocks.BAMBOO_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE, Blocks.WARPED_FENCE_GATE
    ));

    @Nullable
    public static EnumFacing getEnumFacing(BlockState state) {
        for (Property<?> prop : state.getProperties()) {
            if (prop instanceof DirectionProperty && "facing".equals(prop.getName())) {
                return EnumFacing.getByName(state.get(prop).toString());
            }
            if (prop instanceof EnumProperty && "orientation".equals(prop.getName())) {
                return EnumFacing.getByName(state.get(prop).toString());
            }
        }
        return null;
    }

    public static Vec3d getBlockCorner(BlockPos pos) {
        return new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    public static Vec3d getUnderSurface(BlockPos pos) {
        return new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
    }

    public static BlockPos getLeftBlockPos(BlockPos pos, EnumFacing enumFacing) {
        Vec3i facingVec = vec3dTo3i(enumFacing.getLeft().vec);
        return new BlockPos(pos.getX(), pos.getY(), pos.getZ()).add(facingVec);
    }

    public static BlockPos getRightBlockPos(BlockPos pos, EnumFacing enumFacing) {
        Vec3i facingVec = vec3dTo3i(enumFacing.getRight().vec);
        return new BlockPos(pos.getX(), pos.getY(), pos.getZ()).add(facingVec);
    }

    public static Vec3d getRightSurface(BlockPos pos, EnumFacing enumFacing) {
        return getBlockCorner(pos).add(enumFacing.getRight().vec.multiply(0.5));
    }

    public static Vec3d getLeftSurface(BlockPos pos, EnumFacing enumFacing) {
        return getBlockCorner(pos).add(enumFacing.getLeft().vec.multiply(0.5));
    }

    public static Vec3i vec3dTo3i(Vec3d vec3d) {
        return new Vec3i((int) vec3d.x, (int) vec3d.y, (int) vec3d.z);
    }

    public enum EnumFacing {
        EAST(-90.0F, 0.0F, "east", new Vec3d(1, 0, 0), Direction.EAST),
        SOUTH(0.0F, 0.0F, "south", new Vec3d(0, 0, 1), Direction.SOUTH),
        WEST(90.0F, 0.0F, "west", new Vec3d(-1, 0, 0), Direction.WEST),
        NORTH(180.0F, 0.0F, "north", new Vec3d(0, 0, -1), Direction.NORTH),
        UP(0.0F, -90.0F, "up", new Vec3d(0, 1, 0), Direction.UP),
        DOWN(0.0F, 90.0F, "down", new Vec3d(0, -1, 0), Direction.DOWN),


        //orientations specially for crafter block
        DOWN_EAST(-90.0F, 90.0F, "down_east", new Vec3d(1, -1, 0), Direction.EAST),
        DOWN_NORTH(180.0F, 90.0F, "down_north", new Vec3d(0, -1, -1), Direction.NORTH),
        DOWN_SOUTH(0.0F, 90.0F, "down_south", new Vec3d(0, -1, 1), Direction.SOUTH),
        DOWN_WEST(90.0F, 90.0F, "down_west", new Vec3d(-1, -1, 0), Direction.WEST),
        UP_EAST(-90.0F, -90.0F, "up_east", new Vec3d(1, 1, 0), Direction.EAST),
        UP_NORTH(180.0F, -90.0F, "up_north", new Vec3d(0, 1, -1), Direction.NORTH),
        UP_SOUTH(0.0F, -90.0F, "up_south", new Vec3d(0, 1, 1), Direction.SOUTH),
        UP_WEST(90.0F, -90.0F, "up_west", new Vec3d(-1, 1, 0), Direction.WEST),
        WEST_UP(90.0F, 0.0F, "west_up", new Vec3d(-1, 0, 0), Direction.WEST),
        EAST_UP(-90.0F, 0.0F, "east_up", new Vec3d(1, 0, 0), Direction.EAST),
        NORTH_UP(180.0F, 0.0F, "north_up", new Vec3d(0, 0, -1), Direction.NORTH),
        SOUTH_UP(0.0F, 0.0F, "south_up", new Vec3d(1, 0, 1), Direction.SOUTH);

        public final float yaw;
        public final float pitch;
        public final String name;
        public final Vec3d vec;
        public final Direction direction;

        EnumFacing(float yaw, float pitch, String name, Vec3d vec, Direction direction) {
            this.yaw = yaw;
            this.pitch = pitch;
            this.name = name;
            this.vec = vec;
            this.direction = direction;
        }

        @Nullable
        public static EnumFacing getByName(String required) {
            for (EnumFacing facing : EnumFacing.values()) {
                if (facing.name.equals(required.toLowerCase())) {
                    return facing;
                }
            }
            return null;
        }

        public EnumFacing getLeft() {
            return switch (this) {
                case EAST -> NORTH;
                case SOUTH -> EAST;
                case WEST -> SOUTH;
                case NORTH -> WEST;
                case UP -> UP;
                case DOWN -> DOWN;

                //orientations specially for crafter block
                case DOWN_EAST -> DOWN_NORTH;
                case DOWN_NORTH -> DOWN_WEST;
                case DOWN_SOUTH -> DOWN_EAST;
                case DOWN_WEST -> DOWN_SOUTH;
                case UP_EAST -> UP_NORTH;
                case UP_NORTH -> UP_WEST;
                case UP_SOUTH -> UP_EAST;
                case UP_WEST -> UP_SOUTH;
                case WEST_UP -> SOUTH_UP;
                case EAST_UP -> NORTH_UP;
                case NORTH_UP -> WEST_UP;
                case SOUTH_UP -> EAST_UP;
            };
        }

        public EnumFacing getRight() {
            return switch (this) {
                case EAST -> SOUTH;
                case SOUTH -> WEST;
                case WEST -> NORTH;
                case NORTH -> EAST;
                case UP -> UP;
                case DOWN -> DOWN;


                //orientations specially for crafter block
                case DOWN_EAST -> DOWN_SOUTH;
                case DOWN_NORTH -> DOWN_EAST;
                case DOWN_SOUTH -> DOWN_WEST;
                case DOWN_WEST -> DOWN_NORTH;
                case UP_EAST -> UP_SOUTH;
                case UP_NORTH -> UP_EAST;
                case UP_SOUTH -> UP_WEST;
                case UP_WEST -> UP_NORTH;
                case WEST_UP -> NORTH_UP;
                case EAST_UP -> SOUTH_UP;
                case NORTH_UP -> EAST_UP;
                case SOUTH_UP -> WEST_UP;
            };
        }

        public EnumFacing getOpposite() {
            return switch (this) {
                case EAST -> WEST;
                case SOUTH -> NORTH;
                case WEST -> EAST;
                case NORTH -> SOUTH;
                case UP -> DOWN;
                case DOWN -> UP;

                //orientations specially for crafter block
                case DOWN_EAST -> UP_WEST;
                case DOWN_NORTH -> UP_SOUTH;
                case DOWN_SOUTH -> UP_NORTH;
                case DOWN_WEST -> UP_EAST;
                case WEST_UP -> EAST_UP;
                case EAST_UP -> WEST_UP;
                case NORTH_UP -> SOUTH_UP;
                case SOUTH_UP -> NORTH_UP;


                //why? IDK wtf mojang write in code
                case UP_EAST -> DOWN_EAST;
                case UP_NORTH -> DOWN_NORTH;
                case UP_SOUTH -> DOWN_SOUTH;
                case UP_WEST -> DOWN_WEST;
            };
        }
    }
}
