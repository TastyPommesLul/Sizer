package net.tastypommeslul.sizer.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.tastypommeslul.sizer.SizerClient;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class MixinAvatarRenderer {
    @Inject(method = "scale(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At("HEAD"))
    private void scale(AvatarRenderState avatarRenderState, PoseStack poseStack, CallbackInfo ci) {
        if (SizerClient.config == null || !SizerClient.config.enabled) return;
        if (!SizerClient.config.everyone) {
            if (avatarRenderState.id != Minecraft.getInstance().player.getId()) return;
        }
        if (SizerClient.config.aprilFools) {
            aprilFoolsUpdate(poseStack, SizerClient.config.useDifferentValues);
            return;
        }
        if (SizerClient.config.useDifferentValues) {
            poseStack.scale(
                    SizerClient.config.scaleX,
                    SizerClient.config.scaleY,
                    SizerClient.config.scaleZ
            );
        } else {
            float amount = SizerClient.config.scale;
            poseStack.scale(amount, amount, amount);
        }
    }


    /**
     * method for the april fools "update"
     * @param poseStack is the stack to change scale and rotation
     * @param differentValues if the user is using different values for the axes
     */
    @Unique
    public void aprilFoolsUpdate(PoseStack poseStack, boolean differentValues) {
        if (SizerClient.config == null || !SizerClient.config.aprilFools) return;
        if (differentValues) {
            poseStack.translate(0, -SizerClient.config.scaleY * 1.9, 0);
            poseStack.rotateAround(new Quaternionf(), 0, 0, 180);
            poseStack.scale(
                    -SizerClient.config.scaleX,
                    -SizerClient.config.scaleY,
                    -SizerClient.config.scaleZ
            );
        } else {
            float amount = SizerClient.config.scale;
            poseStack.translate(0, -amount * 1.9, 0);
            poseStack.scale(-amount, -amount, -amount);
        }
    }
}