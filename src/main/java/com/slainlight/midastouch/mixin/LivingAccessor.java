package com.slainlight.midastouch.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingAccessor
{
    @Accessor("texture")
    void setTexture(String texture);
}
