package com.tcortega.powertweaks.mixin.minecraft;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface IScreenMixinAccessor {
    @Accessor
    int getWidth();

    @Invoker
    <T extends Element & Drawable & Selectable> T callAddDrawableChild(T drawableElement);
}
