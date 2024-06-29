package top.nanboom233.Utils.Keybind;

import java.util.Set;

public interface IKeybindCategory {

    /**
     * @return true if the keybind triggered last game tick.
     */
    boolean wasPressed();

    /**
     * @return true if the keybind is being held currently.
     */
    boolean isBeingHeld();

    AdvancedKeybind getKeybind();

    void setKeybind(AdvancedKeybind keybind);

    boolean matches(Set<Integer> keyCodes);

    boolean matches(int keyCode);

    boolean triggerKeyAction();

    void tick();

    void updateIsPressed();
}