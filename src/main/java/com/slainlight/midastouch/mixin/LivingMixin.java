package com.slainlight.midastouch.mixin;

import com.slainlight.midastouch.entity.GoldenEntity;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.Pickaxe;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Living.class)
abstract public class LivingMixin extends EntityBase implements GoldenEntity
{
    @Shadow
    public float handSwingProgress;
    @Shadow
    public float lastHandSwingProgress;
    @Shadow
    public float limbDistance;
    @Shadow
    public float field_1012;
    @Shadow
    public float field_1013;
    @Shadow
    public float field_1048;
    @Shadow
    public float field_1050;
    @Shadow
    public int deathTime;

    @Shadow
    public abstract boolean isAlive();

    @Shadow
    protected abstract void applyDamage(int i);

    @Unique
    private boolean isGolden = false;

    public LivingMixin(Level arg)
    {
        super(arg);
    }

    @Override
    public void midastouch$setGolden(boolean golden)
    {
        isGolden = golden;
    }

    @Override
    public boolean midastouch$getGolden()
    {
        if (isGolden)
        {
            ((LivingAccessor) (Object) this).setTexture("midastouch:/textures/entity/default.png");
            velocityX = 0.F;
            velocityZ = 0.F;
        }
        return isGolden;
    }

    @Unique
    boolean frozen = false;

    @Unique
    float lastHandSwingProgressHeld;
    @Unique
    float handSwingProgressHeld;
    @Unique
    float limbDistanceHeld;
    @Unique
    float field_1048Held;

    @Unique
    float field_1012Held;
    @Unique
    float field_1013Held;

    @Unique
    float field_1050Held;
    @Unique
    int deathTimeHeld;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Living;updateDespawnCounter()V"), cancellable = true)
    public void canc(CallbackInfo ci)
    {
        if (((GoldenEntity) (Object) this).midastouch$getGolden() && ((Living) (Object) this).isAlive())
        {
            if (!frozen)
            {
                frozen = true;
                lastHandSwingProgressHeld = lastHandSwingProgress;
                handSwingProgressHeld = handSwingProgress;
                limbDistanceHeld = limbDistance;
                field_1048Held = field_1048;
                field_1012Held = field_1012;
                field_1013Held = field_1013;
                field_1050Held = field_1050;
                deathTimeHeld = deathTime;
            }

            lastHandSwingProgress = lastHandSwingProgressHeld;
            handSwingProgress = handSwingProgressHeld;

            limbDistance = limbDistanceHeld;
            field_1048 = field_1048Held;

            field_1012 = field_1012Held;
            field_1013 = field_1013Held;

            field_1050 = field_1050Held;
            deathTime = deathTimeHeld;

            EntityBase entityBase = (EntityBase) this;

            entityBase.move(0, -0.2F, 0);

            ci.cancel();
        }
    }

    @Inject(method = "damage", at = @At("HEAD"))
    public void damage(EntityBase i, int par2, CallbackInfoReturnable<Boolean> cir)
    {
        if (((GoldenEntity) (Object) this).midastouch$getGolden())
        {
            if (i instanceof PlayerBase player)
            {
                if (player.inventory.getHeldItem() != null)
                {
                    if (player.inventory.getHeldItem().getType() instanceof Pickaxe)
                    {
                        ((GoldenEntity) (Object) this).midastouch$setGolden(false);

                        applyDamage(1000);
                        int var2 = this.rand.nextInt(18);

                        for (int var3 = 0; var3 < var2; ++var3)
                        {
                            if (!player.level.isServerSide)
                                this.dropItem(new ItemInstance(ItemBase.goldIngot, 1), 1);
                        }
                    }
                }
            }
        }
    }
}
