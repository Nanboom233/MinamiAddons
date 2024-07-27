package top.nanboom233.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class WorldUtils {
    public static final HashSet<Block> OPPOSITE_PLACING_BLOCKS = new HashSet<>(Set.of(
            //chests
            Blocks.CHEST, Blocks.TRAPPED_CHEST,

            //pistons
            Blocks.PISTON, Blocks.STICKY_PISTON,

            //all trapdoors
            Blocks.COPPER_TRAPDOOR, Blocks.EXPOSED_COPPER_TRAPDOOR, Blocks.OXIDIZED_COPPER_TRAPDOOR, Blocks.WEATHERED_COPPER_TRAPDOOR, Blocks.WAXED_COPPER_TRAPDOOR, Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR, Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR, Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR, Blocks.WARPED_TRAPDOOR, Blocks.IRON_TRAPDOOR, Blocks.OAK_TRAPDOOR, Blocks.SPRUCE_TRAPDOOR, Blocks.BIRCH_TRAPDOOR, Blocks.JUNGLE_TRAPDOOR, Blocks.ACACIA_TRAPDOOR, Blocks.CHERRY_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR, Blocks.MANGROVE_TRAPDOOR, Blocks.BAMBOO_TRAPDOOR,

            //redstones
            Blocks.REPEATER, Blocks.COMPARATOR, Blocks.HOPPER,

            //furnace
            Blocks.FURNACE

    ));

    @Nullable
    public static DirectionProperty getDirectionProperty(BlockState state) {
        for (Property<?> prop : state.getProperties()) {
            if (prop instanceof DirectionProperty && "facing".equals(prop.getName())) {
                return (DirectionProperty) prop;
            }
        }
        return null;
    }


    @Nullable
    public static Direction getFacingValue(BlockState state) {
        DirectionProperty prop = getDirectionProperty(state);
        if (prop != null) {
            return state.get(prop);
        }
        return null;
    }

    public enum EnumFacing {
        EAST(-90.0F, 0.0F, "east", new Vec3d(1, 0, 0)),
        SOUTH(0.0F, 0.0F, "south", new Vec3d(0, 0, 1)),
        WEST(90.0F, 0.0F, "west", new Vec3d(-1, 0, 0)),
        NORTH(180.0F, 0.0F, "north", new Vec3d(0, 0, -1)),
        UP(0.0F, 90.0F, "up", new Vec3d(0, 1, 0)),
        DOWN(0.0F, -90.0F, "down", new Vec3d(0, -1, 0));

        public final float yaw;
        public final float pitch;
        public final String name;
        public final Vec3d vec;

        EnumFacing(float yaw, float pitch, String name, Vec3d vec) {
            this.yaw = yaw;
            this.pitch = pitch;
            this.name = name;
            this.vec = vec;
        }

        @Nullable
        public static EnumFacing getByName(String required) {
            for (EnumFacing facing : EnumFacing.values()) {
                if (facing.name.equals(required)) {
                    return facing;
                }
            }
            return null;
        }

        public static float getPlacingYaw(Block block, float yaw) {
            if (OPPOSITE_PLACING_BLOCKS.contains(block.getDefaultState().getBlock())) {
                return (float) (((int) yaw + 360) % 360 - 180);
            }
            return yaw;
        }
    }
}
