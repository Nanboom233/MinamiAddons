package top.nanboom233.Module;

import top.nanboom233.Utils.Keybind.AdvancedKeybind;
import top.nanboom233.Utils.Keybind.KeyCodes;
import top.nanboom233.Utils.Keybind.MultiKeybind;

import java.util.HashSet;
import java.util.Set;

import static top.nanboom233.MinamiAddons.mc;
import static top.nanboom233.Utils.Keybind.AdvancedKeybind.KeyTriggerType.INGAME;

public class ExampleInstantModule extends ModuleTemplate {
    public static final String moduleName = "ExampleInstantModule";
    public static final String description = "It's a example of instant modules.";

    private static final MultiKeybind keyBinding = new MultiKeybind(
            new AdvancedKeybind(INGAME, true, new HashSet<>(Set.of(KeyCodes.KEY_V))));

    public ExampleInstantModule() {
        super(moduleName, description, keyBinding);
    }

    @Override
    protected void trigger() {
        super.trigger();
        if (mc.player == null) {
            return;
        }
        mc.player.networkHandler.sendChatMessage("w");
    }
}
