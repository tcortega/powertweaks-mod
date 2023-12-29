package com.tcortega.powertweaks.modules;

import com.tcortega.powertweaks.commands.DelayCommand;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

public class AttackEntity extends Module {
    private boolean isToggled = false;
    private Thread attackThread = null;

    public AttackEntity() {
        super("AttackEntity", GLFW.GLFW_KEY_G);
    }

    @Override
    protected void toggle() {
        isToggled = !isToggled;

        if (isToggled) {
            var lookedAtEntity = getLookedAtEntity();
            if (lookedAtEntity == null) return;

            attackThread = new Thread(() -> {
                while (isToggled) {
                    mc.execute(() -> mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(lookedAtEntity, false)));
                    try {
                        Thread.sleep(DelayCommand.getDelay());
                    } catch (InterruptedException ignored) {

                    }
                }
            });

            attackThread.start();
        } else {
            if (attackThread != null) {
                attackThread.interrupt(); // Stop the current attack thread when toggle off
                attackThread = null; // Allow the old thread to be GCed
            }
        }
    }

    private Entity getLookedAtEntity() {
        var hit = mc.crosshairTarget;

        if (hit == null) return null;

        if (hit.getType() == HitResult.Type.ENTITY) {
            return ((EntityHitResult) hit).getEntity();
        }

        return null;
    }
}