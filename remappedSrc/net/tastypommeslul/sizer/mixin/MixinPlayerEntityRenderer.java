package net.tastypommeslul.sizer.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.tastypommeslul.sizer.SizerClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class MixinPlayerEntityRenderer {

    // 1.21.2-1.21.10
    @Inject(method = "scale(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At("HEAD"))
    private void scale(AvatarRenderState playerEntityRenderState, PoseStack matrixStack, CallbackInfo ci) {
        if (SizerClient.config == null || !SizerClient.config.sizer.enabled) return;
        if (!SizerClient.config.sizer.everyone) {
            if (playerEntityRenderState.id != Minecraft.getInstance().player.getId()) return;
        }
        if (SizerClient.config.sizer.useDifferentValues) {
            matrixStack.scale(
                SizerClient.config.sizer.shrinkAmountX,
                SizerClient.config.sizer.shrinkAmountY,
                SizerClient.config.sizer.shrinkAmountZ
            );
        } else {
            float amount = SizerClient.config.sizer.shrinkAmount;
            matrixStack.scale(amount, amount, amount);
        }
    }

    // 1.21-1.21.1
//    @Inject(method = "scale(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At("HEAD"))
//    private void scale(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, CallbackInfo ci) {
//        if (SizerClient.config == null || !SizerClient.config.sizer.enabled) return;
//        if (!SizerClient.config.sizer.everyone) {
//            if (abstractClientPlayerEntity.getId() != MinecraftClient.getInstance().player.getId()) return;
//        }
//        if (SizerClient.config.sizer.useDifferentValues) {
//            matrixStack.scale(
//                    SizerClient.config.sizer.shrinkAmountX,
//                    SizerClient.config.sizer.shrinkAmountY,
//                    SizerClient.config.sizer.shrinkAmountZ
//            );
//        } else {
//            float amount = SizerClient.config.sizer.shrinkAmount;
//            matrixStack.scale(amount, amount, amount);
//        }
//    }
}
