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
    public float walkAnimationProgress;
    @Shadow
    public float walkAnimationSpeed;
    @Shadow
    public float lastWalkAnimationSpeed;
    @Shadow
    public float bodyYaw;
    @Shadow
    public float lastBodyYaw;
    @Shadow
    public int deathTime;

    @Shadow public float damagedSwingDir;

    @Shadow public abstract boolean isAlive();

    @Shadow protected abstract void applyDamage(int i);

    @Unique
    private float swingAnimationProgressHeld;
    @Unique
    private float walkAnimationProgressHeld;
    @Unique
    private float walkAnimationSpeedHeld;
    @Unique
    private float bodyYawHeld;
    @Unique
    private float yawHeld;
    @Unique
    private float pitchHeld;
    @Unique
    private int deathTimeHeld;

    @Unique
    private boolean isGolden = false;
    @Unique
    private boolean frozen = false;

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
            if (!this.onGround) {
                Entity entityBase = (Entity) this;
                entityBase.move(0, -0.2F, 0);
            } else {
                velocityX = 0.0F;
                velocityZ = 0.0F;
            }

            horizontalSpeed = 0.0F;
            damagedSwingDir = 0.0F;

            if (!frozen) {
                frozen = true;
                swingAnimationProgressHeld = lastSwingAnimationProgress;
                walkAnimationProgressHeld = walkAnimationProgress;
                walkAnimationSpeedHeld = lastWalkAnimationSpeed;
                bodyYawHeld = lastBodyYaw;
                yawHeld = prevYaw;
                pitchHeld = prevPitch;
                deathTimeHeld = deathTime;
            } else {
                swingAnimationProgress = swingAnimationProgressHeld;
                lastSwingAnimationProgress = swingAnimationProgressHeld;
                walkAnimationProgress = walkAnimationProgressHeld;
                walkAnimationSpeed = walkAnimationSpeedHeld;
                lastWalkAnimationSpeed = walkAnimationSpeedHeld;
                bodyYaw = bodyYawHeld;
                lastBodyYaw = bodyYawHeld;
                yaw = yawHeld;
                prevYaw = yawHeld;
                pitch = pitchHeld;
                prevPitch = pitchHeld;
                deathTime = deathTimeHeld;
                ci.cancel();
            }
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
