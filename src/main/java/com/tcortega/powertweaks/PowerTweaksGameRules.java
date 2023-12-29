package com.tcortega.powertweaks;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class PowerTweaksGameRules {

    public static GameRules.Key<GameRules.IntRule> VILLAGER_BREED_COLDOWN_GAME_RULE;

    public static GameRules.Key<GameRules.IntRule> BABY_VILLAGER_GROWTH_GAME_RULE;

    public static void init() {
        VILLAGER_BREED_COLDOWN_GAME_RULE =
                GameRuleRegistry.register(
                        "villagerBreedColdownGameRule",
                        GameRules.Category.MOBS,
                        GameRuleFactory.createIntRule(150)
                );

        BABY_VILLAGER_GROWTH_GAME_RULE =
                GameRuleRegistry.register(
                        "babyVillagerGrowthGameRule",
                        GameRules.Category.MOBS,
                        GameRuleFactory.createIntRule(600)
                );
    }
}
