package top.nanboom233.Module.Modules;

import net.minecraft.client.util.InputUtil;
import top.nanboom233.Module.ModuleTemplate;
import top.nanboom233.Utils.InfoUtils;
import top.nanboom233.Utils.Keybind.AdvancedKeybind;
import top.nanboom233.Utils.Keybind.MultiKeybind;

import java.util.HashSet;
import java.util.Set;

import static top.nanboom233.Utils.InfoUtils.MessageCategory.INFO;
import static top.nanboom233.Utils.Keybind.AdvancedKeybind.KeyTriggerType.INGAME;

public class ExampleInstantModule extends ModuleTemplate {
    public static final String moduleName = "ExampleInstantModule";
    public static final String description = "It's a example of instant modules.";

    private static final MultiKeybind keyBinding = new MultiKeybind(
            new AdvancedKeybind(INGAME, true, new HashSet<>(Set.of(InputUtil.UNKNOWN_KEY.getCode()))));

    public ExampleInstantModule() {
        super(moduleName, description, keyBinding);
    }

    @Override
    public void trigger() {
        super.trigger();
        InfoUtils.showInChat(moduleName + " §6Triggered§r!", INFO);
    }
}
