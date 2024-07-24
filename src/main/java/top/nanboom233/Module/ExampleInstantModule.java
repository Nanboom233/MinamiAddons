package top.nanboom233.Module;

import top.nanboom233.Utils.Keybind.AdvancedKeybind;
import top.nanboom233.Utils.Keybind.KeyCodes;
import top.nanboom233.Utils.Keybind.MultiKeybind;

import java.util.HashSet;
import java.util.Set;

import static top.nanboom233.Utils.Keybind.AdvancedKeybind.KeyTriggerType.INGAME;

public class ExampleInstantModule extends ModuleTemplate {
    public static final String moduleName = "ExampleInstantModule";
    public static final String description = "It's a example of instant modules.";

    private static final MultiKeybind keyBinding = new MultiKeybind(
            new AdvancedKeybind(INGAME, true, new HashSet<>(Set.of(KeyCodes.KEY_V))));

    public ExampleInstantModule() {
        super(moduleName, description, keyBinding);
    }

    @Override
    public void trigger() {
        super.trigger();
//        if (mc.currentScreen == null) {
//            mc.setScreen(Config.getInstance().gui());
////            UScreen.displayScreen(Config.getInstance().gui());
//        }
//        ChatHud chatHud = mc.inGameHud.getChatHud();
//        List<ChatHudLine> messages = List.copyOf(((MixinChatHud) ()chatHud).getMessages());
//        for (ChatHudLine line : messages) {
//            InfoUtils.showInChat(line.toString());
//            System.out.println(line.content());
//        }
//        if (mc.player == null) {
//            return;
//        }
//        for (int i = 0; i < 10; i++) {
//            mc.player.sendMessage(Text.of("TestChat" + i));
//        }
//
//        MinecraftClient mc = MinamiAddons.mc;
//        MixinChatHudAccessor chatHud = (MixinChatHudAccessor) mc.inGameHud.getChatHud();
//        int i = 0;
//        System.err.println("messages:" + chatHud.getMessages().size());
//        System.err.println("Visiblemessages:" + chatHud.getVisibleMessages().size());
//        System.out.println(((MixinTextColor) (Object) TextColor.fromRgb(1231)).getFORMATTING_TO_COLOR());
//        System.out.println(((MixinTextColor) (Object) TextColor.fromRgb(1231)).getBY_NAME());
    }
}
