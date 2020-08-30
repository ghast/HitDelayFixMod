package io.ghast.hitdelayfix.mixins;

import io.ghast.hitdelayfix.HitDelayFix;
import io.ghast.hitdelayfix.commands.HitDelayDebugCommand;
import io.ghast.hitdelayfix.commands.HitDelayFixCommand;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.ClientCommandHandler;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow public MovingObjectPosition objectMouseOver;
    @Shadow public WorldClient theWorld;

    @Shadow private int leftClickCounter;

    @Shadow public PlayerControllerMP playerController;

    @Shadow public EntityPlayerSP thePlayer;

    @Shadow public GuiIngame ingameGUI;


    @Inject(method = "startGame", at = @At("RETURN"))
    private void startGame(CallbackInfo ci) {
        ClientCommandHandler.instance.registerCommand(new HitDelayDebugCommand());
        ClientCommandHandler.instance.registerCommand(new HitDelayFixCommand());
    }

    /**
     * Overwriting the method to keep leftClickCounter intact, so we can track when a hit would have been blocked.
     * @author
     */
    @Overwrite
    private void clickMouse() {
        if (this.leftClickCounter <= 0 || HitDelayFix.INSTANCE.isEnabled()) {
            this.thePlayer.swingItem();

            if (this.objectMouseOver == null) {

                if (this.playerController.isNotCreative()) {
                    this.leftClickCounter = 10;
                }
            } else {
                switch (this.objectMouseOver.typeOfHit) {

                    case ENTITY:
                        if (leftClickCounter > 0) {
                            HitDelayFix.INSTANCE.affectedAttackCounter.incrementUnblockedAttacks();

                            if (HitDelayFix.INSTANCE.affectedAttackCounter.isEnabled()) {
                                this.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText("Unblocked attacks: " +
                                        HitDelayFix.INSTANCE.affectedAttackCounter.getUnblockedAttacks()), 999);
                            }
                        }

                        this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
                        break;

                    case BLOCK:
                        BlockPos blockpos = this.objectMouseOver.getBlockPos();

                        if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                            this.playerController.clickBlock(blockpos, this.objectMouseOver.sideHit);
                            break;
                        }

                    case MISS:
                    default:

                        if (this.playerController.isNotCreative()) {
                            this.leftClickCounter = 10;
                        }
                }
            }
        }else if (HitDelayFix.INSTANCE.affectedAttackCounter.isEnabled() &&
                this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {

            HitDelayFix.INSTANCE.affectedAttackCounter.incrementBlockedAttacks();

            this.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText("Blocked attacks: " +
                    HitDelayFix.INSTANCE.affectedAttackCounter.getBlockedAttacks()), 999);
        }
    }
}


