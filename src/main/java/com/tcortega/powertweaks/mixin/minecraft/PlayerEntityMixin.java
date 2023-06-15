package com.tcortega.powertweaks.mixin.minecraft;

import com.tcortega.powertweaks.PowerTweaks;
import com.tcortega.powertweaks.events.entity.player.ClipAtLedgeEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "clipAtLedge", at = @At("HEAD"), cancellable = true)
    protected void clipAtLedge(CallbackInfoReturnable<Boolean> info) {
        if (!world.isClient) return;

        var event = PowerTweaks.EVENT_BUS.post(ClipAtLedgeEvent.get());
        if (event.isSet()) info.setReturnValue(event.isClip());
    }
}
