package top.nanboom233.Tests;

import top.nanboom233.Config.Keybind.MultiKeybind;
import top.nanboom233.Utils.Module.ModuleTemplate;

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
//        ChatUtils.debug(String.valueOf(InventoryUtils.findItem(mc.player.playerScreenHandler, Items.NETHERITE_SWORD, UniformIntProvider.create(36, 44))), WARNING);
//        mc.doAttack();
        if (mc.crosshairTarget != null) {
            System.err.println(mc.crosshairTarget.getType());
        }
    }
}