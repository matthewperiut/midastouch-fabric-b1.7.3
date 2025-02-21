package com.slainlight.midastouch.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.slainlight.midastouch.entity.GoldenEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public class LivingRendererMixin
{
    @Shadow
    protected EntityModel model;

    @Unique
    private float last = 0.F;

    @WrapOperation(
            method = "render(Lnet/minecraft/entity/LivingEntity;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getHandSwingProgress(Lnet/minecraft/entity/LivingEntity;F)F"
            )
    )
    protected float method_820(LivingEntityRenderer instance, LivingEntity entity, float tickDelta, Operation<Float> original)
    {
        if (!((GoldenEntity) (Object) entity).midastouch$getGolden()) {
            last = original.call(instance, entity, tickDelta);
        }

        return last;
    }

    @WrapOperation(
            method = "render(Lnet/minecraft/entity/LivingEntity;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/model/EntityModel;animateModel(Lnet/minecraft/entity/LivingEntity;FFF)V"
            )
    )
    protected void animateCancel(EntityModel instance, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, Operation<Void> original)
    {
        if (!((GoldenEntity) (Object) entity).midastouch$getGolden())
        {
            original.call(instance, entity, limbAngle, limbDistance, tickDelta);
        }
    }
}
