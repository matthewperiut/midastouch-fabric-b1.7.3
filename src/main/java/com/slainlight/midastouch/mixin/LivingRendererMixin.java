package com.slainlight.midastouch.mixin;

import com.slainlight.midastouch.entity.GoldenEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelBase;
import net.minecraft.entity.Living;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntityRenderer.class)
public class LivingRendererMixin
{
    @Shadow
    protected EntityModelBase field_909;
    @Unique
    private float last = 0.F;

    @Redirect(method = "method_822", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;method_820(Lnet/minecraft/entity/Living;F)F"))
    protected float method_820(LivingEntityRenderer instance, Living f, float v)
    {
        if (((GoldenEntity) (Object) f).midastouch$getGolden())
            return last;
        else
        {
            last = f.method_930(v);
            return last;
        }
    }

    @Redirect(method = "method_822", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModelBase;animateModel(Lnet/minecraft/entity/Living;FFF)V"))
    protected void animateCancel(EntityModelBase instance, Living f, float g, float h, float v)
    {
        if (!((GoldenEntity) (Object) f).midastouch$getGolden())
        {
            instance.animateModel(f, g, h, v);
        }
    }
}
