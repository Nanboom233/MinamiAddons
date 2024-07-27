package top.nanboom233.Module;

import top.nanboom233.Config.Keybind.MultiKeybind;

import java.util.Set;

import static top.nanboom233.Config.Keybind.KeyCodes.KEY_NONE;
import static top.nanboom233.Config.Keybind.MultiKeybind.KeyActionType.PRESS;
import static top.nanboom233.Config.Keybind.MultiKeybind.scopeType.INGAME;

public class ExampleToggleModule extends ToggleModuleTemplate {
    public static final String moduleName = "ExampleToggleModule";
    public static final String description = "It's a example of toggleable modules.";
    private static final MultiKeybind keybind = new MultiKeybind(Set.of(KEY_NONE), INGAME, PRESS, true);

    public ExampleToggleModule() {
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
        super.tick();
    }
}