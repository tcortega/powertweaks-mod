package com.tcortega.powertweaks.modules;

import com.tcortega.powertweaks.PowerTweaks;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public abstract class Module {
    protected final MinecraftClient mc;
    public final String name;
    protected KeyBinding keyBinding;
    protected boolean active;

    public Module(String name, int glfwKey) {
        this.mc = MinecraftClient.getInstance();
        this.name = name;
        this.keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                name,
                InputUtil.Type.KEYSYM,
                glfwKey,
                "PowerTweaks"
        ));

        registerKeyBindListener();
    }

    protected void toggle() {
        active = !active;

        if (active) {
            PowerTweaks.EVENT_BUS.subscribe(this);
        } else {
            PowerTweaks.EVENT_BUS.unsubscribe(this);
        }
    }

    private void registerKeyBindListener() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                toggle();
            }
        });
    }

    public boolean isActive() {
        return active;
    }
}
