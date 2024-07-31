package top.nanboom233.Module;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
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
            BlockPos blockPos = new BlockPos(-68, 72, 402);
            Vec3d hittVecIn = new Vec3d(-68, 72, 402);
            BlockHitResult blockHitResult = new BlockHitResult(
                    hittVecIn, Direction.WEST, blockPos, false);
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(),
                    180, 0, mc.player.isOnGround()));
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHitResult);
        }
    }
}