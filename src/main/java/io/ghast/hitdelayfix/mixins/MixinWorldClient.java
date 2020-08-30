package io.ghast.hitdelayfix.mixins;

import io.ghast.hitdelayfix.HitDelayFix;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldClient.class)
public class MixinWorldClient {


    @Inject(method = "sendQuittingDisconnectingPacket", at = @At("HEAD"))
    public void sendQuittingDisconnectingPacket(CallbackInfo ci) {
        HitDelayFix.INSTANCE.affectedAttackCounter.reset();
    }
}
