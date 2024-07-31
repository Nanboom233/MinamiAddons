package top.nanboom233;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import top.nanboom233.Commands.MinamiCommandManager;
import top.nanboom233.Config.Config;
import top.nanboom233.Config.KeybindConfig;
import top.nanboom233.Module.ModuleManager;

/**
 * @author Nanboom233
 */
public class MinamiAddons implements ClientModInitializer {
    public static final String MOD_NAME = "MinamiAddons";
    public static final String MOD_VERSION = "0.2.1";
    public static final String MOD_ID = "minamiaddons";
    private static final Logger logger = LogManager.getLogger(MOD_ID);
    public static final ModuleManager moduleManager = ModuleManager.getInstance();
    public static final Config config = Config.getInstance();
    public static final KeybindConfig keybindConfig = KeybindConfig.getInstance();


    public static final MinecraftClient mc = MinecraftClient.getInstance();

    @Nullable
    public static Screen getCurrentScreen() {
        return MinecraftClient.getInstance().currentScreen;
    }

    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register(MinamiCommandManager::register);
    }

    @Override
    public void onInitializeClient() {
        registerCommands();
    }

    public static Logger getLogger() {
        return logger;
    }

}