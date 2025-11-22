package net.tastypommeslul.sizer.mixin;

import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.tastypommeslul.sizer.client.SizerClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer {

    // 1.21.2-1.21.10
    @Inject(method = "scale(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At("HEAD"))
    private void scale(PlayerEntityRenderState playerEntityRenderState, MatrixStack matrixStack, CallbackInfo ci) {
        if (SizerClient.config == null || !SizerClient.config.sizer.enabled) return;
        if (!SizerClient.config.sizer.everyone) {
            if (playerEntityRenderState.id != MinecraftClient.getInstance().player.getId()) return;
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
