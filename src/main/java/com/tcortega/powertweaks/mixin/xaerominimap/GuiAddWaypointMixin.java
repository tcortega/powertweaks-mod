package com.tcortega.powertweaks.mixin.xaerominimap;

import com.tcortega.powertweaks.PowerTweaks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.AXaeroMinimap;
import xaero.common.gui.GuiAddWaypoint;

import java.util.stream.Collectors;

import xaero.common.gui.ScreenBase;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.waypoints.WaypointsManager;

import java.io.IOException;
import java.util.ArrayList;

@Mixin(GuiAddWaypoint.class)
public abstract class GuiAddWaypointMixin extends ScreenBase {
    private static final String OVERWORLD_DIM = "dim%0";
    private static final String NETHER_DIM = "dim%-1";

    private boolean shouldMirrorToNether = true;

    protected GuiAddWaypointMixin(AXaeroMinimap modMain, Screen parent, Screen escape, Text titleIn) {
        super(modMain, parent, escape, titleIn);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo info) {
        var currentWorldKey = ((IGuiAddWaypointMixinAccessor) this).getWorlds().getCurrentKeys()[0];
        if (!isInOverworld(currentWorldKey)) return;

        addMirrorToNetherButton();
    }

    @Redirect(method = "lambda$init$3", at = @At(value = "INVOKE", target = "Lxaero/common/minimap/waypoints/WaypointsManager;getWorld(Ljava/lang/String;Ljava/lang/String;)Lxaero/common/minimap/waypoints/WaypointWorld;"))
    private WaypointWorld onGetWorld(WaypointsManager instance, String container, String world) {
        var originalWorld = instance.getWorld(container, world);
        if (!shouldMirrorToNether || !isInOverworld(originalWorld)) return originalWorld;

        mirrorWaypointsToNether(instance, container, world);

        return instance.getWorld(container, world);
    }

    private void addMirrorToNetherButton() {
        int x = this.width / 2 + 105, y = 56, width = 120, height = 20;
        var button = buildCyclingButton(x, y, width, height);
        addDrawableChild(button);
    }

    private CyclingButtonWidget<Boolean> buildCyclingButton(int x, int y, int width, int height) {
        return CyclingButtonWidget.builder(this::getDisplayText)
                .values(true, false)
                .initially(true)
                .build(x, y, width, height, Text.literal("Mirror to Nether"), this::toggleMirrorToNether);
    }

    private void toggleMirrorToNether(CyclingButtonWidget<Boolean> button, Boolean value) {
        shouldMirrorToNether = !shouldMirrorToNether;
    }

    private Text getDisplayText(boolean newValue) {
        return Text.translatable(newValue ? "powertweaks.mirror_to_nether.enabled" : "powertweaks.mirror_to_nether.disabled");
    }

    private void mirrorWaypointsToNether(WaypointsManager instance, String container, String world) {
        PowerTweaks.LOGGER.info("Mirroring waypoints to nether");
        var netherContainer = container.replace(OVERWORLD_DIM, NETHER_DIM);
        var netherWorld = instance.getWorld(netherContainer, world);
        if (netherWorld == null) {
            PowerTweaks.LOGGER.error("Nether world is null");
            return;
        }

        var currentSetKey = ((IGuiAddWaypointMixinAccessor) this).getSets().getCurrentSetKey();
        var destinationSet = netherWorld.getSets().get(currentSetKey);
        var mirroredWaypoints = getMirroredWaypoints();
        destinationSet.getList().addAll(mirroredWaypoints);

        saveWaypoints(netherWorld, mirroredWaypoints.size());
    }

    private ArrayList<Waypoint> getMirroredWaypoints() {
        return ((IGuiAddWaypointMixinAccessor) this).getWaypointsEdited().stream()
                .map(this::mirrorWaypoint)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Waypoint mirrorWaypoint(Waypoint waypoint) {
        int x = waypoint.getX() / 8, y = waypoint.getY(), z = waypoint.getZ() / 8;
        return new Waypoint(x, y, z, waypoint.getName(), waypoint.getSymbol(), waypoint.getColor());
    }

    private void saveWaypoints(WaypointWorld netherWorld, int waypointCount) {
        try {
            modMain.getSettings().saveWaypoints(netherWorld);
            PowerTweaks.LOGGER.info("Successfully mirrored " + waypointCount + " waypoints to nether");
        } catch (IOException exception) {
            PowerTweaks.LOGGER.error("Failed to save waypoints to nether", exception);
        }
    }


    private boolean isInOverworld(String container) {
        return container.contains(OVERWORLD_DIM);
    }

    private boolean isInOverworld(WaypointWorld world) {
        return world.getContainer().getKey().contains(OVERWORLD_DIM);
    }
}
