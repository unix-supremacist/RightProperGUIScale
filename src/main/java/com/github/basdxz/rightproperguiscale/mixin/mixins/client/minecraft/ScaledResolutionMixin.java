package com.github.basdxz.rightproperguiscale.mixin.mixins.client.minecraft;

import com.github.basdxz.rightproperguiscale.GUIJiggler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ScaledResolution.class)
public abstract class ScaledResolutionMixin {
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;
    @Shadow
    private double scaledWidthD;
    @Shadow
    private double scaledHeightD;
    @Shadow
    private int scaleFactor;
    private float scaleFactorFloat;

    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;II)V",
            at = @At(value = "RETURN"),
            require = 1)
    private void deferredConstructor(Minecraft minecraft, int width, int height, CallbackInfo ci) {
        scaleFactorFloat = 1;

        initScaleFactorFloat(GUIJiggler.guiScaleAsFloat(), width, height);
        initScaledWidth(width);
        initScaledHeight(height);
        initScaleFactor();
    }

    private void initScaleFactorFloat(float guiScale, int width, int height) {
        while (scaleFactorFloat < guiScale &&
               nextScaledWidth(width) >= GUIJiggler.MIN_SCALED_WIDTH &&
               nextScaledHeight(height) >= GUIJiggler.MIN_SCALED_HEIGHT)
            incrementScaleFactor();
    }

    private int nextScaledWidth(int width) {
        return MathHelper.ceiling_double_int(width / (scaleFactorFloat + GUIJiggler.GUI_SCALE_STEP));
    }

    private int nextScaledHeight(int height) {
        return MathHelper.ceiling_double_int(height / (scaleFactorFloat + GUIJiggler.GUI_SCALE_STEP));
    }

    private void incrementScaleFactor() {
        scaleFactorFloat += GUIJiggler.GUI_SCALE_STEP;
    }

    private void initScaledWidth(int width) {
        scaledWidthD = width / scaleFactorFloat;
        scaledWidth = MathHelper.ceiling_double_int(scaledWidthD);
    }

    private void initScaledHeight(int height) {
        scaledHeightD = height / scaleFactorFloat;
        scaledHeight = MathHelper.ceiling_double_int(scaledHeightD);
    }

    private void initScaleFactor() {
        scaleFactor = MathHelper.ceiling_double_int(scaleFactorFloat);
    }
}