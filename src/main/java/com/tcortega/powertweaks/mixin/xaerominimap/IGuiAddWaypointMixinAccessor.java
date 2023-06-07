package com.tcortega.powertweaks.mixin.xaerominimap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import xaero.common.gui.GuiAddWaypoint;
import xaero.common.gui.GuiWaypointSets;
import xaero.common.gui.GuiWaypointWorlds;
import xaero.common.minimap.waypoints.Waypoint;

import java.util.ArrayList;

@Mixin(GuiAddWaypoint.class)
public interface IGuiAddWaypointMixinAccessor {
    @Accessor
    ArrayList<Waypoint> getWaypointsEdited();

    @Accessor
    GuiWaypointSets getSets();

    @Accessor
    GuiWaypointWorlds getWorlds();
}
