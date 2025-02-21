package com.slainlight.midastouch.mixin;

import com.slainlight.midastouch.entity.GoldenEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class LevelMixin
{
    @Inject(
            method = "playSound(Lnet/minecraft/entity/Entity;Ljava/lang/String;FF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void playSound(Entity entity, String sound, float a, float b, CallbackInfo ci)
    {
        try
        {
            if (entity instanceof LivingEntity living && !sound.equals("step.stone"))
            {
                if (((GoldenEntity) living).midastouch$getGolden())
                {
                    ((World) (Object) this).playSound(entity, "step.stone", a, b);
                    ci.cancel();
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.fillInStackTrace());
        }

    }
}
