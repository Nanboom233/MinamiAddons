package top.nanboom233.Utils.Module;

import org.jetbrains.annotations.NotNull;
import top.nanboom233.Config.Keybind.MultiKeybind;
import top.nanboom233.Utils.Texts.ChatUtils;

import static top.nanboom233.Utils.Texts.ChatUtils.MessageCategory.INFO;

public abstract class ModuleTemplate {
    @NotNull
    public String moduleName;
    @NotNull
    public String description;
    @NotNull
    public MultiKeybind keybind;

    public ModuleTemplate(@NotNull String moduleName, @NotNull String description, @NotNull MultiKeybind keyBinding) {
        this.moduleName = moduleName;
        this.description = description;
        this.keybind = keyBinding;
    }

    protected void trigger() {
        ChatUtils.debug(moduleName + " §6Triggered§r!", INFO);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ModuleTemplate module)) {
            return false;
        }
        return module.moduleName.equals(this.moduleName) &&
                module.description.equals(this.description);
    }

    @Override
    public int hashCode() {
        return moduleName.hashCode() * 31 + description.hashCode();
    }
}
