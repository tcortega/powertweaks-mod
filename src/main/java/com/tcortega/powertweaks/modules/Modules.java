package com.tcortega.powertweaks.modules;

import com.tcortega.powertweaks.PowerTweaks;
import com.tcortega.powertweaks.events.game.GameJoinedEvent;
import com.tcortega.powertweaks.events.game.GameLeftEvent;
import meteordevelopment.orbit.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class Modules {
    private final List<Module> modules = new ArrayList<>();
    public Modules() {
        init();
    }

    public void init() {
       add(new SafeWalk());
    }

    public void add(Module module) {
        modules.add(module);
    }

    @EventHandler
    private void onGameJoined(GameJoinedEvent event) {
        synchronized (modules) {
            for (Module module : modules) {
                if (module.isActive()) {
                    PowerTweaks.EVENT_BUS.subscribe(module);
                }
            }
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        synchronized (modules) {
            for (Module module : modules) {
                if (module.isActive()) {
                    PowerTweaks.EVENT_BUS.unsubscribe(module);
                }
            }
        }
    }
}
