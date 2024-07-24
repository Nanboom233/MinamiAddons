package top.nanboom233.Module;

import org.jetbrains.annotations.NotNull;
import top.nanboom233.Utils.ChatUtils;
import top.nanboom233.Utils.Keybind.MultiKeybind;

import static top.nanboom233.Utils.ChatUtils.MessageCategory.INFO;

public abstract class ModuleTemplate {
    public String moduleName;
    public String description;
    @NotNull
    public MultiKeybind keyBinding;

    public ModuleTemplate(String moduleName, String description, @NotNull MultiKeybind keyBinding) {
        this.moduleName = moduleName;
        this.description = description;
        this.keyBinding = keyBinding;
    }

    protected void trigger() {
        ChatUtils.showInChat(moduleName + " §6Triggered§r!", INFO);
    }
}
