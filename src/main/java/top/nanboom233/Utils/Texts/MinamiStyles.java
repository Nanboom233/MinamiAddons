package top.nanboom233.Utils.Texts;

import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum MinamiStyles {
    BLACK("§0", "black", (style) -> style.withColor(Formatting.BLACK)),
    DARK_BLUE("§1", "dark_blue", (style) -> style.withColor(Formatting.DARK_BLUE)),
    DARK_GREEN("§2", "dark_green", (style) -> style.withColor(Formatting.DARK_GREEN)),
    DARK_AQUA("§3", "dark_aqua", (style) -> style.withColor(Formatting.DARK_AQUA)),
    DARK_RED("§4", "dark_red", (style) -> style.withColor(Formatting.DARK_RED)),
    DARK_PURPLE("§5", "dark_purple", (style) -> style.withColor(Formatting.DARK_PURPLE)),
    GOLD("§6", "gold", (style) -> style.withColor(Formatting.GOLD)),
    GRAY("§7", "gray", (style) -> style.withColor(Formatting.GRAY)),
    DARK_GRAY("§8", "dark_gray", (style) -> style.withColor(Formatting.DARK_GRAY)),
    BLUE("§9", "blue", (style) -> style.withColor(Formatting.BLUE)),
    GREEN("§a", "green", (style) -> style.withColor(Formatting.GREEN)),
    AQUA("§b", "aqua", (style) -> style.withColor(Formatting.AQUA)),
    RED("§c", "red", (style) -> style.withColor(Formatting.RED)),
    LIGHT_PURPLE("§d", "light_purple", (style) -> style.withColor(Formatting.LIGHT_PURPLE)),
    YELLOW("§e", "yellow", (style) -> style.withColor(Formatting.YELLOW)),
    WHITE("§f", "white", (style) -> style.withColor(Formatting.WHITE)),
    BOLD("§l", "bold", (style) -> style.withBold(true)),
    ITALIC("§o", "italic", (style) -> style.withItalic(true)),
    UNDERLINED("§n", "underlined", (style) -> style.withUnderline(true)),
    STRIKE_THROUGH("§m", "strike_through", (style) -> style.withStrikethrough(true)),
    OBFUSCATED("§k", "obfuscated", (style) -> style.withObfuscated(true)),
    RESET("§r", "reset", (style) -> Style.EMPTY);


    public final String FORMATTING;
    public final String NAME;
    public final Function<Style, Style> APPLIER;

    MinamiStyles(String formatting, String name, Function<Style, Style> applier) {
        this.FORMATTING = formatting;
        this.NAME = name;
        this.APPLIER = applier;
    }

    @Nullable
    public static MinamiStyles getStyle(String identifier) {
        for (MinamiStyles style : MinamiStyles.values()) {
            if (style.FORMATTING.equals(identifier) || style.NAME.equals(identifier)) {
                return style;
            }
        }
        return null;
    }

    public String applyOld(String text) {
        return this.FORMATTING + text;
    }

    public Style apply(Style style) {
        return this.APPLIER.apply(style);
    }
}
