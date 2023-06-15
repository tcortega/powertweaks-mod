package com.tcortega.powertweaks;

import com.tcortega.powertweaks.modules.Modules;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class PowerTweaks implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("powertweaks");
    public static final String PACKAGE = "com.tcortega.powertweaks";

    public static MinecraftClient mc;
    private static Modules modules;
    public static final IEventBus EVENT_BUS = new EventBus();

    @Override
    public void onInitializeClient() {
        LOGGER.info("PowerTweaks initialized.");

        mc = MinecraftClient.getInstance();
        modules = new Modules();

        EVENT_BUS.registerLambdaFactory(PACKAGE, (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
        EVENT_BUS.subscribe(this);
        EVENT_BUS.subscribe(modules);

        PowerTweaksGameRules.init();
    }
}
