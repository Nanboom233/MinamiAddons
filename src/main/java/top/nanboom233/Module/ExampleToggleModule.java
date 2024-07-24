package top.nanboom233.Module;

import top.nanboom233.Utils.Keybind.AdvancedKeybind;
import top.nanboom233.Utils.Keybind.KeyCodes;
import top.nanboom233.Utils.Keybind.MultiKeybind;

import java.util.HashSet;
import java.util.Set;

import static top.nanboom233.Utils.Keybind.AdvancedKeybind.KeyTriggerType.INGAME;

public class ExampleToggleModule extends ToggleModuleTemplate {
    public static final String moduleName = "ExampleToggleModule";
    public static final String description = "It's a example of toggleable modules.";

    private static final MultiKeybind keyBinding = new MultiKeybind(
            new AdvancedKeybind(INGAME, true, new HashSet<>(Set.of(KeyCodes.KEY_NONE))));

    public ExampleToggleModule() {
        super(moduleName, description, keyBinding);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}