package top.nanboom233.Module;

import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import top.nanboom233.Config.Keybind.MultiKeybind;

import java.util.Set;

import static top.nanboom233.Config.Keybind.KeyCodes.KEY_NONE;
import static top.nanboom233.Config.Keybind.MultiKeybind.KeyActionType.PRESS;
import static top.nanboom233.Config.Keybind.MultiKeybind.scopeType.INGAME;
import static top.nanboom233.MinamiAddons.mc;

public class ExampleInstantModule extends ModuleTemplate {
    public static final String moduleName = "ExampleInstantModule";
    public static final String description = "It's a example of instant modules.";
    private static final MultiKeybind keybind = new MultiKeybind(Set.of(KEY_NONE), INGAME, PRESS, true);

    public ExampleInstantModule() {
        super(moduleName, description, keybind);
    }

    @Override
    protected void trigger() {
        super.trigger();
        if (mc.interactionManager != null && mc.player != null) {
            BlockPos blockPos = new BlockPos(-60, 72, 424);
            Vec3d hitVecIn = new Vec3d(-60, 72, 424);
            BlockHitResult blockHitResult = new BlockHitResult(
                    hitVecIn, Direction.EAST, blockPos, false);
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHitResult);
        }
    }
}