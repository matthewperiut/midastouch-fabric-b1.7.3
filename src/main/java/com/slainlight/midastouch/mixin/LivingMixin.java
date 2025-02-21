package com.slainlight.midastouch.mixin;

import com.slainlight.midastouch.entity.GoldenEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
abstract public class LivingMixin extends Entity implements GoldenEntity
{
    @Shadow
    public float swingAnimationProgress;
    @Shadow
    public float lastSwingAnimationProgress;

    @Shadow
    public float walkAnimationSpeed;
    @Shadow
    public float lastWalkAnimationSpeed;

    @Shadow
    public float bodyYaw;
    @Shadow
    public float lastBodyYaw;

    @Shadow
    public float walkAnimationProgress;
    @Shadow
    public int deathTime;

    @Shadow
    public abstract boolean isAlive();

    @Shadow
    protected abstract void applyDamage(int i);

    @Unique
    private boolean isGolden = false;

    public LivingMixin(World arg)
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
    float lastSwingAnimationProgressHeld;
    @Unique
    float swingAnimationProgressHeld;
    @Unique
    float walkAnimationSpeedHeld;
    @Unique
    float lastWalkAnimationSpeedHeld;

    @Unique
    float bodyYawHeld;
    @Unique
    float lastBodyYawHeld;

    @Unique
    float walkAnimationProgressHeld;
    @Unique
    int deathTimeHeld;

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;tickMovement()V"
            ),
            cancellable = true
    )
    public void canc(CallbackInfo ci)
    {
        if (((GoldenEntity) (Object) this).midastouch$getGolden() && ((LivingEntity) (Object) this).isAlive())
        {
            if (!frozen)
            {
                frozen = true;
                lastSwingAnimationProgressHeld = lastSwingAnimationProgress;
                swingAnimationProgressHeld = swingAnimationProgress;
                walkAnimationSpeedHeld = walkAnimationSpeed;
                lastWalkAnimationSpeedHeld = lastWalkAnimationSpeed;
                bodyYawHeld = bodyYaw;
                lastBodyYawHeld = lastBodyYaw;
                walkAnimationProgressHeld = walkAnimationProgress;
                deathTimeHeld = deathTime;
            }

            lastSwingAnimationProgress = lastSwingAnimationProgressHeld;
            swingAnimationProgress = swingAnimationProgressHeld;

            walkAnimationSpeed = walkAnimationSpeedHeld;
            lastWalkAnimationSpeed = lastWalkAnimationSpeedHeld;

            bodyYaw = bodyYawHeld;
            lastBodyYaw = lastBodyYawHeld;

            walkAnimationProgress = walkAnimationProgressHeld;
            deathTime = deathTimeHeld;

            Entity entityBase = (Entity) this;

            entityBase.move(0, -0.2F, 0);

            ci.cancel();
        }
    }

    @Inject(method = "damage", at = @At("HEAD"))
    public void damage(Entity i, int par2, CallbackInfoReturnable<Boolean> cir)
    {
        if (((GoldenEntity) (Object) this).midastouch$getGolden())
        {
            if (i instanceof PlayerEntity player)
            {
                if (player.inventory.getSelectedItem() != null)
                {
                    if (player.inventory.getSelectedItem().getItem() instanceof PickaxeItem)
                    {
                        ((GoldenEntity) (Object) this).midastouch$setGolden(false);

                        applyDamage(1000);
                        int var2 = this.random.nextInt(18);

                        for (int var3 = 0; var3 < var2; ++var3)
                        {
                            if (!player.world.isRemote)
                                this.dropItem(new ItemStack(Item.GOLD_INGOT, 1), 1);
                        }
                    }
                }
            }
        }
    }
}
