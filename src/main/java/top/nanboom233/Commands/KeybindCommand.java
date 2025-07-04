package top.nanboom233.Commands;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import top.nanboom233.Config.Config;
import top.nanboom233.Config.Keybind.KeyCodes;
import top.nanboom233.Config.Keybind.MultiKeybind;
import top.nanboom233.Config.KeybindConfig;
import top.nanboom233.Handlers.TickHandler;
import top.nanboom233.Mixins.MixinMinecraftClientAccessor;
import top.nanboom233.Module.ModuleManager;
import top.nanboom233.Module.ModuleTemplate;
import top.nanboom233.Utils.Texts.ChatUtils;
import top.nanboom233.Utils.Texts.MinamiTextComponent;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.LongArgumentType.getLong;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.minecraft.command.CommandSource.suggestMatching;
import static top.nanboom233.Config.Keybind.KeyCodes.KEY_ESCAPE;
import static top.nanboom233.Config.Keybind.KeyCodes.KEY_NONE;
import static top.nanboom233.Config.KeybindConfig.commandSetKeybind;
import static top.nanboom233.Config.KeybindConfig.parseToLong;
import static top.nanboom233.MinamiAddons.config;
import static top.nanboom233.MinamiAddons.mc;
import static top.nanboom233.Utils.Texts.MinamiStyles.*;

public class KeybindCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> register(LiteralArgumentBuilder<FabricClientCommandSource> literalargumentbuilder,
                                                                             CommandRegistryAccess commandRegistryAccess) {
        return literalargumentbuilder
                .then(literal("keybind").executes((source) -> KeybindCommand.listKeybinds())
                        .then(argument("keybindable features", StringArgumentType.word())
                                .suggests((context, builder) -> suggestMatching(getKeybindableFeatures(), builder))
                                .executes((context) -> keybindGuide(getString(context, "keybindable features")))
                                .then(argument("keyCodes", LongArgumentType.longArg())
                                        .executes((context -> commandSetKeybind(getString(context, "keybindable features"), getLong(context, "keyCodes")))))));
    }

    private static final Text description = new MinamiTextComponent("MinamiAddons").withStyles(BOLD, GOLD)
            .append(new MinamiTextComponent("'s Keybinds Settings Interface")).getText();
    public static final MessageIndicator minamiKeybindSettingsIndicater = new MessageIndicator(
            727727, null,
            description, "MinamiAddons");
    private static Set<String> keybindableFeatures = null;
    public static TickHandler.ITickTask setKeybindProgress;
    public static Set<Integer> aleardyPressedKeyCodes;
    public static boolean hasInput;
    public static boolean firstShown;
    public static long settingStartTime = -1;

    public static int listKeybinds() {
        MinamiCommandManager.beforeRender();
        show(new MinamiTextComponent("___________________________________").withStyles(GRAY));
        emptyLine();
        show(new MinamiTextComponent("Minami Keybinds Settings:").withStyles(GRAY));

        //config keybind
        MinamiTextComponent configKeybindInfo = getKeybindInfoText("Config", Config.getInstance().keybind);
        show(configKeybindInfo);

        //modules keybinds
        for (ModuleTemplate module : ModuleManager.getInstance().getModuleList()) {
            MinamiTextComponent modulesKeybindInfo = getKeybindInfoText(module.moduleName, module.keybind);
            show(modulesKeybindInfo);
        }

        emptyLine();
        show(new MinamiTextComponent("-----------------------------------").withStyles(GRAY));
        return 1;
    }

    public static MinamiTextComponent getKeybindInfoText(String name, MultiKeybind keybind) {
        MinamiTextComponent keybindInfo = new MinamiTextComponent().append(
                new MinamiTextComponent(" - " + name + ": "));
        KeybindConfig.colorFeatureName(name, keybindInfo);
        if (keybind.isNone()) {
            keybindInfo.append(new MinamiTextComponent("[NONE]").withStyles(YELLOW)
                    .showText(new MinamiTextComponent("Click to set keybind for " + name + ".").withStyles(YELLOW))
                    .runCommand("/minami keybind " + name)
            );
        } else {
            keybindInfo.append(new MinamiTextComponent(keybind.toString())
                    .showText(new MinamiTextComponent("Click to change keybind for " + name + ".").withStyles(GRAY))
                    .runCommand("/minami keybind " + name)
            );
        }
        return keybindInfo;
    }

    public static void show(MinamiTextComponent text) {
        ChatUtils.show(text, minamiKeybindSettingsIndicater);
    }

    public static void emptyLine() {
        ChatUtils.emptyLine(minamiKeybindSettingsIndicater);
    }

    public static Set<String> getKeybindableFeatures() {
        if (keybindableFeatures != null) {
            return keybindableFeatures;
        }
        keybindableFeatures = new HashSet<>();
        keybindableFeatures.add("Config");
        for (ModuleTemplate module : ModuleManager.getInstance().getModuleList()) {
            keybindableFeatures.add(module.moduleName);
        }
        return keybindableFeatures;
    }

    public static int keybindGuide(String keybindableFeature) {
        if (mc.currentScreen == null) {
            mc.execute(() ->
                    ((MixinMinecraftClientAccessor) mc).invokeOpenChatScreen("")
            );
        }
        aleardyPressedKeyCodes = new HashSet<>();
        hasInput = false;
        firstShown = false;
        setKeybindProgress = mc -> {
            if (System.currentTimeMillis() - settingStartTime < 200) {
                return;
            }
            Set<Integer> pressedKeyCodes = new HashSet<>(MultiKeybind.getPressedKeys());
            Set<String> pressedKeys =
                    pressedKeyCodes.stream()
                            .map(KeyCodes::getNameForKey)
                            .collect(Collectors.toSet());
            if (!aleardyPressedKeyCodes.containsAll(pressedKeyCodes) || !firstShown) {
                updateKeybindRender(keybindableFeature, pressedKeys);
                firstShown = true;
            }
            aleardyPressedKeyCodes.addAll(pressedKeyCodes);
            if (!pressedKeyCodes.isEmpty()) {
                hasInput = true;
            } else if (hasInput) {
                if (aleardyPressedKeyCodes.equals(Set.of(KEY_ESCAPE))) {
                    aleardyPressedKeyCodes = Set.of(KEY_NONE);
                }
                Set<String> alreadyPressedKeys =
                        aleardyPressedKeyCodes.stream()
                                .map(KeyCodes::getNameForKey)
                                .collect(Collectors.toSet());
                MinamiCommandManager.beforeRender();
                MinamiTextComponent confirmInfo = new MinamiTextComponent("Keybind for ")
                        .append(new MinamiTextComponent(keybindableFeature));
                KeybindConfig.colorFeatureName(keybindableFeature, confirmInfo);
                confirmInfo.append(new MinamiTextComponent(" will be set to :"));
                show(new MinamiTextComponent("___________________________________").withStyles(GRAY));
                emptyLine();
                show(confirmInfo);
                emptyLine();
                show(getCenterAlignText(KeybindConfig.getKeyInfo(alreadyPressedKeys, BOLD, DARK_GREEN)));
                emptyLine();
                show(new MinamiTextComponent("[CONFIRM]").withStyles(DARK_GREEN, BOLD)
                        .showText(new MinamiTextComponent("Confirm and set the keybind.").withStyles(GRAY))
                        .runCommand("/minami keybind " + keybindableFeature + " " + parseToLong(aleardyPressedKeyCodes))
                        .append(new MinamiTextComponent("    "))
                        .append(new MinamiTextComponent("[RESET]").withStyles(RED, BOLD)
                                .showText(new MinamiTextComponent("Reset the keybind."))
                                .runCommand("/minami keybind " + keybindableFeature)));
                emptyLine();
                show(new MinamiTextComponent("-----------------------------------").withStyles(GRAY));
                TickHandler.getInstance().unregister(setKeybindProgress);
            }
            if (System.currentTimeMillis() - settingStartTime > config.settingsTimeout * 1000L) {
                MinamiCommandManager.beforeRender();
                show(new MinamiTextComponent("Keybind Setting Aborted."));
                TickHandler.getInstance().unregister(setKeybindProgress);
            }
        };
        settingStartTime = System.currentTimeMillis();
        TickHandler.getInstance().register(setKeybindProgress);
        return 1;
    }

    public static void updateKeybindRender(String keybindableFeature, Set<String> pressedKeys) {
        MinamiCommandManager.beforeRender();
        show(new MinamiTextComponent("___________________________________").withStyles(GRAY));
        emptyLine();
        MinamiTextComponent setKeybindText = new MinamiTextComponent("Set Keybind for ")
                .append(new MinamiTextComponent(keybindableFeature + ":"));
        KeybindConfig.colorFeatureName(keybindableFeature, setKeybindText);
        show(setKeybindText);
        emptyLine();
        if (pressedKeys.isEmpty()) {
            show(getCenterAlignText(new MinamiTextComponent("[NONE]").withStyles(BOLD)));
        } else {
            show(getCenterAlignText(KeybindConfig.getKeyInfo(pressedKeys, BOLD, DARK_GREEN)));
        }
        emptyLine();
        show(new MinamiTextComponent("ESC For KEY_NONE.").withStyles(BOLD, YELLOW));
        show(new MinamiTextComponent("-----------------------------------").withStyles(GRAY));
    }

    public static MinamiTextComponent getCenterAlignText(MinamiTextComponent originalText) {
        int maxLength = 35;
        int originalLength = originalText.content.getString().length();
        if (originalLength >= maxLength) {
            return originalText;
        }
        return new MinamiTextComponent(" ".repeat((maxLength - originalLength) / 2))
                .append(originalText);
    }

}
