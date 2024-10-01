package top.nanboom233.Tests;

import top.nanboom233.Config.Keybind.MultiKeybind;
import top.nanboom233.Utils.Module.ModuleTemplate;

import java.util.Set;

import static top.nanboom233.Config.Keybind.KeyCodes.KEY_NONE;
import static top.nanboom233.Config.Keybind.MultiKeybind.KeyActionType.PRESS;
import static top.nanboom233.Config.Keybind.MultiKeybind.scopeType.INGAME;

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
//        ChatUtils.debug("Post a TestEvent!", ChatUtils.MessageCategory.WARNING);
//        MinamiAddons.eventBus.post(new TestEvent("lolol233", 114514, false));
    }
}