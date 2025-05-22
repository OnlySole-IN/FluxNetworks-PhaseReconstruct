package onlysole.fluxnetworks.mixin;

import mekanism.common.integration.fluxnetworks.FluxNetworksIntegration;
import onlysole.fluxnetworks.common.handler.TileEntityHandler;
import onlysole.fluxnetworks.common.integration.mekanism.MekanismEnergyHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluxNetworksIntegration.class)
public class MixinFluxNetworksIntegration {

    @Inject(method = "preInit", at = @At("HEAD"), cancellable = true, remap = false)
    private static void injectPreInit(CallbackInfo ci) {
        //在列表头部插入适配器，保证不被其他类型覆盖结果。
        //Insert adapters in the head of the list to ensure that the results are not overwritten by other types.
        TileEntityHandler.tileEnergyHandlers.add(0, MekanismEnergyHandler.INSTANCE);
        ci.cancel();
    }
}
