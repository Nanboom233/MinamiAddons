package top.nanboom233.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import top.nanboom233.MinamiAddons;
import top.nanboom233.Utils.Handlers.Events.TickEndEvent;
import top.nanboom233.Utils.Texts.ChatUtils;
import top.nanboom233.Utils.Texts.MinamiTextComponent;

import java.util.function.Consumer;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static top.nanboom233.MinamiAddons.config;
import static top.nanboom233.Utils.Texts.MinamiStyles.BOLD;
import static top.nanboom233.Utils.Texts.MinamiStyles.GOLD;


public class MinamiCommandManager {
    private static final Text description = new MinamiTextComponent("MinamiAddons").addStyles(BOLD, GOLD)
            .append(new MinamiTextComponent("'s Main Settings Interface")).getText();
    public static final MessageIndicator minamiSettingsIndicater = new MessageIndicator(
            727727, null,
            description, "MinamiAddons");

    public static long lastResponse = -1;

    // todo add custom commands
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess) {
        LiteralArgumentBuilder<FabricClientCommandSource> literalargumentbuilder =
                literal("minami").executes((source) -> defaultEntry())
                        .then(literal("clear").executes((source) -> clearSettingMessage()));

        literalargumentbuilder = KeybindCommand.register(literalargumentbuilder, commandRegistryAccess);
        dispatcher.register(literalargumentbuilder);

        Consumer<TickEndEvent> autoClear = (TickEndEvent event) -> {
            if (lastResponse != -1 && System.currentTimeMillis() - lastResponse > config.settingsTimeout * 1000L) {
                lastResponse = -1;
                clearSettingMessage();
            }
        };
        MinamiAddons.eventBus.register(TickEndEvent.class, autoClear);
    }

    public static void show(MinamiTextComponent text) {
        ChatUtils.show(text, minamiSettingsIndicater);
    }

    public static int clearSettingMessage() {
        ChatUtils.removeMessage(minamiSettingsIndicater);
        ChatUtils.removeMessage(KeybindCommand.minamiKeybindSettingsIndicater);
        return 1;
    }

    private static int defaultEntry() {
        beforeRender();
        show(new MinamiTextComponent("Hello from MinamiAddons there! (*/ω＼*)").addStyles(GOLD));
        return 1;
    }

    public static void beforeRender() {
        MinamiCommandManager.lastResponse = System.currentTimeMillis();
        clearSettingMessage();
    }
}
