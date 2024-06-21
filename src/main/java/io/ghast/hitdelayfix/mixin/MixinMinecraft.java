package io.ghast.hitdelayfix.mixin;

import io.ghast.hitdelayfix.HitDelayFix;
import io.ghast.hitdelayfix.commands.HitDelayDebugCommand;
import io.ghast.hitdelayfix.commands.HitDelayFixCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.ClientCommandHandler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow
    public GuiIngame ingameGUI;
    @Shadow
    public MovingObjectPosition objectMouseOver;
    @Shadow
    private int leftClickCounter;

    @Inject(method = "startGame", at = @At("RETURN"))
    private void startGame(CallbackInfo ci) {
        ClientCommandHandler.instance.registerCommand(new HitDelayDebugCommand());
        ClientCommandHandler.instance.registerCommand(new HitDelayFixCommand());
    }

    @Redirect(method = "clickMouse", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;leftClickCounter:I", ordinal = 0, opcode = Opcodes.GETFIELD))
    private int clickMouseRedirect(Minecraft minecraft) {
        return HitDelayFix.INSTANCE.isEnabled() ? 0 : this.leftClickCounter;
    }

    @Inject(method = "clickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;attackEntity(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/Entity;)V"))
    private void clickMouseBefore(final CallbackInfo ci) {
        if (leftClickCounter > 0) {
            HitDelayFix.INSTANCE.affectedAttackCounter.incrementUnblockedAttacks();

            if (HitDelayFix.INSTANCE.affectedAttackCounter.isEnabled()) {
                this.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText("Unblocked attacks: " + HitDelayFix.INSTANCE.affectedAttackCounter.getUnblockedAttacks()), 999);
            }
        }
    }

    @Inject(method = "clickMouse", at = @At("TAIL"))
    private void clickMouseAfter(CallbackInfo ci) {
        if (!(this.leftClickCounter <= 0 || HitDelayFix.INSTANCE.isEnabled()) && HitDelayFix.INSTANCE.affectedAttackCounter.isEnabled() && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {

            HitDelayFix.INSTANCE.affectedAttackCounter.incrementBlockedAttacks();

            this.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText("Blocked attacks: " + HitDelayFix.INSTANCE.affectedAttackCounter.getBlockedAttacks()), 999);
        }
    }
}
