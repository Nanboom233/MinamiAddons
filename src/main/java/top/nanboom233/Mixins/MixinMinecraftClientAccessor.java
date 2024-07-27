package top.nanboom233.Mixins;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftClient.class)
public interface MixinMinecraftClientAccessor {
    @Invoker("openChatScreen")
    void invokeOpenChatScreen(String text);
}
