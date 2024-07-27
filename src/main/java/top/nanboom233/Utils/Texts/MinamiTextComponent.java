package top.nanboom233.Utils.Texts;

import net.minecraft.text.*;

import static net.minecraft.text.ClickEvent.Action.*;
import static net.minecraft.text.HoverEvent.Action.SHOW_TEXT;

public class MinamiTextComponent {
    public final MutableText content;
    public MinamiTextComponent lastComponent;

    public MinamiTextComponent(String string) {
        this.content = Text.literal(string);
        lastComponent = this;
    }

    public MinamiTextComponent(Text content) {
        this.content = content.copy();
        lastComponent = this;
    }

    public MinamiTextComponent() {
        this.content = Text.literal("");
        lastComponent = this;
    }

    public Text getText() {
        return content;
    }

    public MinamiTextComponent suggestCommand(String command) {
        content.setStyle(content.getStyle().withClickEvent(new ClickEvent(SUGGEST_COMMAND, command)));
        return this;
    }

    public MinamiTextComponent runCommand(String command) {
        content.setStyle(content.getStyle().withClickEvent(new ClickEvent(RUN_COMMAND, command)));
        return this;
    }

    public MinamiTextComponent showText(MinamiTextComponent text) {
        content.setStyle(content.getStyle().withHoverEvent(new HoverEvent(SHOW_TEXT, text.content)));
        return this;
    }

    public MinamiTextComponent openUrl(String url) {
        content.setStyle(content.getStyle().withClickEvent(new ClickEvent(OPEN_URL, url)));
        return this;
    }

    public MinamiTextComponent openFile(String fileAbsolutePath) {
        content.setStyle(content.getStyle().withClickEvent(new ClickEvent(OPEN_FILE, fileAbsolutePath)));
        return this;
    }

    public MinamiTextComponent withStyles(MinamiStyles... styles) {
        Style newStyle = content.getStyle();
        for (MinamiStyles style : styles) {
            newStyle = style.apply(newStyle);
        }
        content.setStyle(newStyle);
        return this;
    }

    public MinamiTextComponent append(MinamiTextComponent... texts) {
        for (MinamiTextComponent text : texts) {
            content.append(text.content);
            lastComponent = text;
        }
        return this;
    }

    public MinamiTextComponent append(String... strings) {
        for (String string : strings) {
            lastComponent = new MinamiTextComponent(string);
            content.append(lastComponent.content);
        }
        return this;
    }

    public MinamiTextComponent append(Text... texts) {
        for (Text text : texts) {
            lastComponent = new MinamiTextComponent(text);
            content.append(lastComponent.content);
        }
        return this;
    }

    @Override
    public String toString() {
        return content.toString();
    }
}
