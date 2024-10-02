package top.nanboom233.Mixins;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * @author Nanboom233
 * @since 2024/10/3
 */
@Mixin(MinecraftClient.class)
public interface MixinMinecraftClientAccessor {
    @Invoker("doAttack")
    boolean invokeDoAttack();

    @Invoker("openChatScreen")
    void invokeOpenChatScreen(String text);
}

