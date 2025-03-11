package com.onlysole.fluxnetworksreconstruct.common.handler;

import com.onlysole.fluxnetworksreconstruct.FluxNetworksReconstruct;
import com.onlysole.fluxnetworksreconstruct.common.block.BlockCore;
import com.onlysole.fluxnetworksreconstruct.common.item.ItemCore;
import com.onlysole.fluxnetworksreconstruct.common.registry.RegistryBlocks;
import com.onlysole.fluxnetworksreconstruct.common.registry.RegistryItems;
import com.onlysole.fluxnetworksreconstruct.common.registry.RegistryRecipes;
import com.onlysole.fluxnetworksreconstruct.common.registry.RegistrySounds;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber
public class RegistryEventHandler {

    @SubscribeEvent
    public static void registerItems(@Nonnull RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(RegistryItems.ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void registerBlocks(@Nonnull RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(RegistryBlocks.BLOCKS.toArray(new Block[0]));
        TileEntityHandler.registerTileEntity();
        NetworkRegistry.INSTANCE.registerGuiHandler(FluxNetworksReconstruct.instance, new GuiHandler());
    }

    @SubscribeEvent
    public static void registerRecipes(@Nonnull RegistryEvent.Register<IRecipe> event) {
        RegistryRecipes.registerRecipes(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (Item item : RegistryItems.ITEMS) {
            if (item instanceof ItemCore)
                ((ItemCore) item).registerModels();
        }
        for (Block block : RegistryBlocks.BLOCKS) {
            if (block instanceof BlockCore)
                ((BlockCore) block).registerModels();
        }
    }

    @SubscribeEvent
    public static void registerSounds(@Nonnull RegistryEvent.Register<SoundEvent> event) {
        RegistrySounds.registerSounds(event.getRegistry());
    }
}
