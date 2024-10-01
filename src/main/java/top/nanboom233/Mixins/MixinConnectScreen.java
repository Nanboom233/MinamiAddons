package top.nanboom233.Mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.CookieStorage;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.ClientConnection;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static top.nanboom233.MinamiAddons.mc;

@Mixin(ConnectScreen.class)
public abstract class MixinConnectScreen {
    @Invoker("connect")
    abstract void invokeConnect(MinecraftClient client, ServerAddress address, ServerInfo info, @Nullable CookieStorage cookieStorage);

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/ClientConnection;handleDisconnection()V"
            )
    )
    public void tick(ClientConnection clientConnection) {
        invokeConnect(mc, new ServerAddress("localhost", 25565), new ServerInfo("test", "localhost", ServerInfo.ServerType.LAN), null);
    }
}
