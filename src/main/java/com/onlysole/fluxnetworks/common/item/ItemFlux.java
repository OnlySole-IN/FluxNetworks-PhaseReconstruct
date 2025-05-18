package com.onlysole.fluxnetworks.common.item;

import com.onlysole.fluxnetworks.FluxConfig;
import com.onlysole.fluxnetworks.api.translate.FluxTranslate;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFlux extends ItemCore {

    public ItemFlux() {
        super("Flux");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(FluxConfig.enableFluxRecipe) {
            tooltip.add(FluxTranslate.FLUX_TOOLTIP.t());
        }
    }
}
