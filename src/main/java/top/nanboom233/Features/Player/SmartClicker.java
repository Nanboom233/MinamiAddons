package top.nanboom233.Features.Player;

import top.nanboom233.Config.Keybind.MultiKeybind;
import top.nanboom233.MinamiAddons;
import top.nanboom233.Mixins.MixinMinecraftClientAccessor;
import top.nanboom233.Utils.Module.ToggleModuleTemplate;

import java.util.Set;

import static net.minecraft.util.hit.HitResult.Type.ENTITY;
import static top.nanboom233.Config.Keybind.KeyCodes.KEY_NONE;
import static top.nanboom233.Config.Keybind.MultiKeybind.KeyActionType.PRESS;
import static top.nanboom233.Config.Keybind.MultiKeybind.scopeType.INGAME;
import static top.nanboom233.MinamiAddons.mc;

/**
 * Click automatically and smartly to attack emenies
 *
 * @author Nanboom233
 * @since 2024/10/2
 */
public class SmartClicker extends ToggleModuleTemplate {
    public static final String moduleName = "SmartClicker";
    public static final String description = "Click automatically and smartly to attack emenies.";
    private static final MultiKeybind keybind = new MultiKeybind(Set.of(KEY_NONE), INGAME, PRESS, false);


    public SmartClicker() {
        super(moduleName, description, keybind);
    }

    @Override
    protected void onEnable() {
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
    }

    @Override
    protected void tick() {
        if (mc.world == null || mc.player == null || mc.crosshairTarget == null || mc.targetedEntity == null) {
            return;
        }
        if (mc.options.attackKey.isPressed() &&
                mc.crosshairTarget.getType() == ENTITY &&
                mc.targetedEntity.isAlive() &&
                mc.player.getAttackCooldownProgress(6.0F) >= 1.0F &&
                mc.player.getAttackCooldownProgressPerTick() > 5.0F) {
            if (MinamiAddons.config.smartClickerCritical) {
                if (mc.player.isOnGround()) {
                    mc.player.jump();
                } else if (mc.player.fallDistance > 0.0F) {
                    ((MixinMinecraftClientAccessor) mc).invokeDoAttack();
                }
            } else {
                ((MixinMinecraftClientAccessor) mc).invokeDoAttack();
            }
        }
    }
}
