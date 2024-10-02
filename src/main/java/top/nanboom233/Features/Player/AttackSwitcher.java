package top.nanboom233.Features.Player;

import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import top.nanboom233.Config.Keybind.MultiKeybind;
import top.nanboom233.MinamiAddons;
import top.nanboom233.Utils.ControlUtils;
import top.nanboom233.Utils.Handlers.Events.PlayerAttackEvent;
import top.nanboom233.Utils.Handlers.SubscribeEvent;
import top.nanboom233.Utils.InventoryUtils;
import top.nanboom233.Utils.Module.ToggleModuleTemplate;

import java.util.Set;

import static top.nanboom233.Config.Keybind.KeyCodes.KEY_NONE;
import static top.nanboom233.Config.Keybind.MultiKeybind.KeyActionType.PRESS;
import static top.nanboom233.Config.Keybind.MultiKeybind.scopeType.INGAME;
import static top.nanboom233.MinamiAddons.mc;

/**
 * Auto switch to a specific item when attack,
 * which allows player to attack with both items' affects
 *
 * @author Nanboom233
 * @since 2024/10/2
 */
public class AttackSwitcher extends ToggleModuleTemplate {
    public static final String moduleName = "AttackSwitcher";
    public static final String description = "Auto switch to a specific item when attack, which allows player to attack with both items' affects.";
    private static final MultiKeybind keybind = new MultiKeybind(Set.of(KEY_NONE), INGAME, PRESS, true);

    public static AttackSwitcher INSTANCE;
    private int preSlot = -1;

    public AttackSwitcher() {
        super(moduleName, description, keybind);
        INSTANCE = this;
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
        super.tick();
    }

    @SubscribeEvent
    public static void runSwitcher(PlayerAttackEvent event) {
        if (!INSTANCE.state) {
            return;
        }
        if (mc.world == null || mc.player == null) {
            return;
        }
        INSTANCE.preSlot = ControlUtils.getCurrentSlot();
        ControlUtils.setSlot(InventoryUtils.findItem(mc.player.playerScreenHandler,
                Registries.ITEM.get(Identifier.of(MinamiAddons.config.attackSwitchItem)),
                UniformIntProvider.create(36, 44)) - 36);
    }

    public static void recoverSlot() {
        if (INSTANCE.preSlot != -1) {
            ControlUtils.setSlot(INSTANCE.preSlot);
            INSTANCE.preSlot = -1;
        }
    }
}
