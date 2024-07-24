package top.nanboom233.Module;

import org.jetbrains.annotations.NotNull;
import top.nanboom233.Utils.ChatUtils;
import top.nanboom233.Utils.Keybind.MultiKeybind;

import static top.nanboom233.Utils.ChatUtils.MessageCategory.INFO;

/**
 * @author Nanboom233
 */

public abstract class ToggleModuleTemplate extends ModuleTemplate {
    public boolean state = false;


    public ToggleModuleTemplate(String moduleName, String description, @NotNull MultiKeybind keyBinding) {
        super(moduleName, description, keyBinding);
    }

    @Override
    protected void trigger() {
        toggle();
    }

    public boolean setState(boolean state) {
        if (this.state != state) {
            this.state = state;
            if (state) {
                onEnable();
            } else {
                onDisable();
            }
            return true;
        }
        return false;
    }

    public void toggle() {
        setState(!state);
    }

    protected void onEnable() {
        ChatUtils.showInChat(moduleName + " is now §2Enabled§r.", INFO);
    }

    protected void onDisable() {
        ChatUtils.showInChat(moduleName + " is now §4Disabled§r.", INFO);
    }

    protected void tick() {
    }

}