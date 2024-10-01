package top.nanboom233.Utils.Handlers.Events;

/**
 * An Event generated when a key
 * (including keyboard and mouse) is pressed or released
 *
 * @author Nanboom233
 * @since 2024/10/2
 */
public class KeyInputEvent extends EventTemplate {
    public final long windowPointer;
    public final int keyCode;
    public final int scanCode;
    public final boolean state;

    public KeyInputEvent(long windowPointer, int key, int scanCode, boolean state) {
        this.windowPointer = windowPointer;
        this.keyCode = key;
        this.scanCode = scanCode;
        this.state = state;
    }
}
