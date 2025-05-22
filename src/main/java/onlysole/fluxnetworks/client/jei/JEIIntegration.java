package onlysole.fluxnetworks.client.jei;

import onlysole.fluxnetworks.common.config.FluxConfig;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

import javax.annotation.Nonnull;

@JEIPlugin
public class JEIIntegration implements IModPlugin {

    @Override
    public void register(@Nonnull IModRegistry registry) {
        if (FluxConfig.general.enableFluxRecipe) {
            FluxCraftingCategory.register(registry);
        }
    }

    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registry) {
        if (FluxConfig.general.enableFluxRecipe) {
            registry.addRecipeCategories(new FluxCraftingCategory(registry.getJeiHelpers().getGuiHelper()));
        }
    }
}
