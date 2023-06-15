package com.tcortega.powertweaks.mixin.minecraft;

import com.tcortega.powertweaks.PowerTweaks;
import com.tcortega.powertweaks.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ai.brain.task.VillagerBreedTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Optional;

import static com.tcortega.powertweaks.PowerTweaksGameRules.BABY_VILLAGER_GROWTH_GAME_RULE;
import static com.tcortega.powertweaks.PowerTweaksGameRules.VILLAGER_BREED_COLDOWN_GAME_RULE;

@Mixin(VillagerBreedTask.class)
public class VillagerBreedTaskMixin {
    private static final int BREEDING_COOLDOWN = 6000;
    private static final int BABY_VILLAGER_AGE = -24000;

//    /**
//     * @author tcortega
//     * @reason Needed so we can modify the breeding cooldown and baby villager growth time based on synced game rules.
//     */
//    @Overwrite
//    private Optional<VillagerEntity> createChild(ServerWorld world, VillagerEntity parent, VillagerEntity partner) {
//        var villagerEntity = parent.createChild(world, partner);
//        if (villagerEntity == null) return Optional.empty();
//
//        var gameRules = world.getGameRules();
//        var breedingCooldown = gameRules.getInt(VILLAGER_BREED_COLDOWN_GAME_RULE);
//        parent.setBreedingAge(breedingCooldown);
//        partner.setBreedingAge(breedingCooldown);
//        villagerEntity.setBreedingAge(gameRules.getInt(BABY_VILLAGER_GROWTH_GAME_RULE));
//
//        villagerEntity.refreshPositionAndAngles(parent.getX(), parent.getY(), parent.getZ(), 0.0F, 0.0F);
//        world.spawnEntityAndPassengers(villagerEntity);
//        world.sendEntityStatus(villagerEntity, (byte) 12);
//
//        return Optional.of(villagerEntity);
//
//    }

    @ModifyArgs(method = "createChild", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/VillagerEntity;setBreedingAge(I)V"))
    private void modifyBreedingAge(Args args, ServerWorld world, VillagerEntity parent, VillagerEntity partner) {
        int breedingAge = args.get(0);
        var gameRules = world.getGameRules();

        if (breedingAge == BREEDING_COOLDOWN) {
            args.set(0, Utils.convertSecondsToTicks(gameRules.getInt(VILLAGER_BREED_COLDOWN_GAME_RULE)));
        }

        if (breedingAge == BABY_VILLAGER_AGE) {
            args.set(0, Utils.convertSecondsToTicks(-gameRules.getInt(BABY_VILLAGER_GROWTH_GAME_RULE)));
        }
    }
}
