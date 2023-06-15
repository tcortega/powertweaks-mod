package com.tcortega.powertweaks.modules;

import com.tcortega.powertweaks.events.entity.player.ClipAtLedgeEvent;
import meteordevelopment.orbit.EventHandler;
import org.lwjgl.glfw.GLFW;

public class SafeWalk extends Module {
    public SafeWalk() {
        super("SafeWalk", GLFW.GLFW_KEY_V);
    }

    @EventHandler
    private void onClipAtLedge(ClipAtLedgeEvent event) {
        if (!mc.player.isSneaking()) event.setClip(true);
    }
}
