package top.nanboom233.Module;

import org.jetbrains.annotations.NotNull;
import top.nanboom233.Utils.InfoUtils;
import top.nanboom233.Utils.Keybind.MultiKeybind;

import static top.nanboom233.Utils.InfoUtils.MessageCategory.INFO;

public abstract class ModuleTemplate {
    public String moduleName;
    public String description;
    public boolean state = false;
    @NotNull
    public MultiKeybind keyBinding;

    public ModuleTemplate(String moduleName, String description, @NotNull MultiKeybind keyBinding) {
        this.moduleName = moduleName;
        this.description = description;
        this.keyBinding = keyBinding;
    }

    public void trigger() {
        InfoUtils.showInChat(moduleName + " §6Triggered§r!", INFO);
    }
}
