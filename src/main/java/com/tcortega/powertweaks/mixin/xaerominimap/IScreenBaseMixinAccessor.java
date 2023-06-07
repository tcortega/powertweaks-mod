package com.tcortega.powertweaks.mixin.xaerominimap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import xaero.common.AXaeroMinimap;
import xaero.common.gui.ScreenBase;

@Mixin(ScreenBase.class)
public interface IScreenBaseMixinAccessor {
    @Accessor
    AXaeroMinimap getModMain();
}
