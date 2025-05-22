package onlysole.fluxnetworks.common.handler;

import onlysole.fluxnetworks.FluxNetworks;
import onlysole.fluxnetworks.common.block.BlockCore;
import onlysole.fluxnetworks.common.item.ItemCore;
import onlysole.fluxnetworks.common.registry.RegistryBlocks;
import onlysole.fluxnetworks.common.registry.RegistryItems;
import onlysole.fluxnetworks.common.registry.RegistryRecipes;
import onlysole.fluxnetworks.common.registry.RegistrySounds;
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
        NetworkRegistry.INSTANCE.registerGuiHandler(FluxNetworks.instance, new GuiHandler());
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
