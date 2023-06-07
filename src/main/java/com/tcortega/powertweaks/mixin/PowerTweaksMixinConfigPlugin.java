package com.tcortega.powertweaks.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.HashMap;
import java.util.Map;

import java.util.*;

public class PowerTweaksMixinConfigPlugin implements IMixinConfigPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger("powertweaks");

    private static final Map<String, String> TARGET_MODS = new HashMap<>() {{
        put("xaerominimap", "23.4.4");
    }};

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        var loader = FabricLoader.getInstance();
        var modid = mixinClassName.split("\\.")[4];

        var modContainerOpt = loader.getModContainer(modid);

        if (modContainerOpt.isEmpty()) {
            LOGGER.info("Target mod " + modid + " not found. Skipping Mixin " + mixinClassName + ".");
            return false;
        }

        ModContainer modContainer = modContainerOpt.get();
        Version currentVersion = modContainer.getMetadata().getVersion();

        String expectedVersion = TARGET_MODS.get(modid);

        if (!expectedVersion.equals(currentVersion.getFriendlyString())) {
            LOGGER.error("Incompatible mod version for " + modid + ". Expected version: "
                    + expectedVersion + ", but found version: " + currentVersion.getFriendlyString());
            return false;
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
