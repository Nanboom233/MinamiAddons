package top.nanboom233;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import top.nanboom233.Module.ModuleManager;
import top.nanboom233.Utils.Keybind.MultiKeybind;

/**
 * @author Nanboom233
 */
public class MinamiAddons implements ClientModInitializer {
    public static final String MOD_NAME = "MinamiAddons";
    public static final String MOD_VERSION = "0.1.0-dev";
    public static final String MOD_ID = "minamiaddons";
    private static final Logger logger = LogManager.getLogger(MOD_ID);
    private final ModuleManager moduleManager = ModuleManager.getInstance();

    public static final MinecraftClient mc = MinecraftClient.getInstance();

    @Nullable
    public static Screen getCurrentScreen() {
        return MinecraftClient.getInstance().currentScreen;
    }

    @Override
    public void onInitializeClient() {
        MultiKeybind.setDebugMode(true);
    }

    public static Logger getLogger() {
        return logger;
    }
}