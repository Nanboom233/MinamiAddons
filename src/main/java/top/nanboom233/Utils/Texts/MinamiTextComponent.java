package top.nanboom233.Utils.Texts;

import net.minecraft.text.*;

import java.net.URI;

import static top.nanboom233.Utils.Texts.MinamiStyles.EmptyStyle;

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
        content.setStyle(content.getStyle().withClickEvent(new ClickEvent.SuggestCommand(command)));
        return this;
    }

    public MinamiTextComponent runCommand(String command) {
        content.setStyle(content.getStyle().withClickEvent(new ClickEvent.RunCommand(command)));
        return this;
    }

    public MinamiTextComponent showText(MinamiTextComponent text) {
        content.setStyle(content.getStyle().withHoverEvent(new HoverEvent.ShowText(text.content)));
        return this;
    }

    public MinamiTextComponent openUrl(String url) {
        try {
            content.setStyle(content.getStyle().withClickEvent(new ClickEvent.OpenUrl(new URI(url))));
        } catch (java.net.URISyntaxException e) {
            e.printStackTrace();
            return this;
        }
        return this;
    }

    public MinamiTextComponent openFile(String fileAbsolutePath) {
        content.setStyle(content.getStyle().withClickEvent(new ClickEvent.OpenFile(fileAbsolutePath)));
        return this;
    }

    public MinamiTextComponent addStyles(MinamiStyles... styles) {
        Style newStyle = content.getStyle();
        for (MinamiStyles style : styles) {
            newStyle = style.apply(newStyle);
        }
        content.setStyle(newStyle);
        return this;
    }

    public MinamiTextComponent resetStyles() {
        content.setStyle(EmptyStyle);
        return this;
    }

    public MinamiTextComponent withStyles(MinamiStyles... styles) {
        Style newStyle = EmptyStyle;
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
