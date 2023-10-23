package com.slainlight.midastouch.mixin;

import com.slainlight.midastouch.entity.GoldenEntity;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public class LevelMixin
{
    @Inject(method = "playSound(Lnet/minecraft/entity/EntityBase;Ljava/lang/String;FF)V", at = @At("HEAD"), cancellable = true)
    public void playSound(EntityBase entity, String sound, float a, float b, CallbackInfo ci)
    {
        try
        {
            if (entity instanceof Living living && !sound.equals("step.stone"))
            {
                if (((GoldenEntity) living).midastouch$getGolden())
                {
                    ((Level) (Object) this).playSound(entity, "step.stone", a, b);
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
