package top.nanboom233.Config.Keybind;

import java.util.Set;

public interface IKeybindCategory {

    /**
     * @return true if the keybind triggered last game tick.
     */
    boolean isTriggered();

    /**
     * @return true if the keybind is being held currently.
     */
    boolean isBeingHeld();

    Set<Integer> getKeyCodes();

    void setKeybind(Set<Integer> keyCodes);

    boolean matches(Set<Integer> keyCodes);

    boolean matches(int keyCode);

    void tick();

    void updateIsPressed();

    boolean isNone();
}