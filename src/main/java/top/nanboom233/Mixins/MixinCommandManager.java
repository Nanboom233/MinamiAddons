package top.nanboom233.Mixins;

import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CommandManager.class)
public class MixinCommandManager {

//    @Shadow
//    @Final
//    private CommandDispatcher<ServerCommandSource> dispatcher;
//
//    @Inject(
//            method = "<init>",
//            at = @At("RETURN")
//    )
//    private void onRegister(RegistrationEnvironment environment,
//                            CommandRegistryAccess commandRegistryAccess, CallbackInfo ci) {
//        MinamiAddons.registerCommands(this.dispatcher, environment, commandRegistryAccess);
//    }
}
