package io.ghast.hitdelayfix.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow private int leftClickCounter;

    @Inject(method = "clickMouse", at = @At("HEAD"))
    private void clickMouseAfter(final CallbackInfo ci) {
        leftClickCounter = 0;
    }
}
