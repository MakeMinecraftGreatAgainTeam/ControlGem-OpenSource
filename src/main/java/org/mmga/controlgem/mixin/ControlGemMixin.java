package org.mmga.controlgem.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.mmga.controlgem.ControlGem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created On 2022/7/12 19:47
 *
 * @author wzp
 * @version 1.0.0
 */
@Mixin(TitleScreen.class)
public class ControlGemMixin {
    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        ControlGem.LOGGER.info("This line is printed by an example mod mixin!");
    }
}
