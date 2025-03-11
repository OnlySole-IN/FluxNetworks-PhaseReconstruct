package com.onlysole.fluxnetworksreconstruct.common;

import com.google.common.collect.Lists;
import com.onlysole.fluxnetworksreconstruct.FluxConfig;
import com.onlysole.fluxnetworksreconstruct.FluxNetworksReconstruct;
import com.onlysole.fluxnetworksreconstruct.Tags;
import com.onlysole.fluxnetworksreconstruct.api.gui.EnumFeedbackInfo;
import com.onlysole.fluxnetworksreconstruct.api.network.IFluxNetwork;
import com.onlysole.fluxnetworksreconstruct.api.utils.NBTType;
import com.onlysole.fluxnetworksreconstruct.common.capabilities.DefaultSuperAdmin;
import com.onlysole.fluxnetworksreconstruct.common.connection.FluxNetworkCache;
import com.onlysole.fluxnetworksreconstruct.common.connection.FluxNetworkInvalid;
import com.onlysole.fluxnetworksreconstruct.common.core.EntityFireItem;
import com.onlysole.fluxnetworksreconstruct.common.data.FluxChunkManager;
import com.onlysole.fluxnetworksreconstruct.common.event.FluxConnectionEvent;
import com.onlysole.fluxnetworksreconstruct.common.handler.CapabilityHandler;
import com.onlysole.fluxnetworksreconstruct.common.handler.PacketHandler;
import com.onlysole.fluxnetworksreconstruct.common.handler.TileEntityHandler;
import com.onlysole.fluxnetworksreconstruct.common.integration.MekanismIntegration;
import com.onlysole.fluxnetworksreconstruct.common.integration.TOPIntegration;
import com.onlysole.fluxnetworksreconstruct.common.integration.oc.OCIntegration;
import com.onlysole.fluxnetworksreconstruct.common.network.PacketNetworkUpdate;
import com.onlysole.fluxnetworksreconstruct.common.network.PacketSuperAdmin;
import com.onlysole.fluxnetworksreconstruct.common.registry.RegistryBlocks;
import com.onlysole.fluxnetworksreconstruct.common.registry.RegistryItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CommonProxy {

    public boolean baublesLoaded;
    public boolean ocLoaded;

    public int admin_viewing_network_id = -1;
    public boolean detailed_network_view;
    public IFluxNetwork admin_viewing_network = FluxNetworkInvalid.instance;

    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        PacketHandler.registerMessages();
        TileEntityHandler.registerEnergyHandler();
        FluxConfig.init(event.getModConfigurationDirectory());
        EntityRegistry.registerModEntity(new ResourceLocation(Tags.MOD_ID, "Flux"), EntityFireItem.class, "Flux", 0, FluxNetworksReconstruct.instance, 64, 10, true);
        if(Loader.isModLoaded("mekanism")){
            MekanismIntegration.preInit();
        }
        this.ocLoaded = Loader.isModLoaded("opencomputers");
        this.baublesLoaded = Loader.isModLoaded("baubles");
    }

    public void init(FMLInitializationEvent event) {
        DefaultSuperAdmin.register();
        FMLInterModComms.sendMessage("carryon", "blacklistBlock",  Tags.MOD_ID + ":*");
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", TOPIntegration.class.getName());
        if(ocLoaded) {
            OCIntegration.init();
        }
    }

    public void postInit(FMLPostInitializationEvent event) {
        ForgeChunkManager.setForcedChunkLoadingCallback(FluxNetworksReconstruct.instance, FluxChunkManager::callback);
        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
    }

    public void onServerStarted() {

    }

    public void onServerStopped() {
        FluxNetworkCache.instance.clearNetworks();
        FluxChunkManager.clear();
    }

    public static CreativeTabs creativeTabs = new CreativeTabs(Tags.MOD_ID) {

        @Override
        public ItemStack createIcon() {
            return new ItemStack(RegistryBlocks.FLUX_PLUG);
        }
    };

    public void registerItemModel(Item item, int meta, String variant) {

    }

    @SubscribeEvent(receiveCanceled = true)
    public void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event) {
        if(event.getSide().isServer()) {
            if(!FluxConfig.enableFluxRecipe) {
                return;
            }
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            if (world.getBlockState(pos).getBlock().equals(Blocks.OBSIDIAN) && world.getBlockState(pos.down(2)).getBlock().equals(Blocks.BEDROCK)) {
                List<EntityItem> entities = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.down()));
                if(entities.isEmpty())
                    return;
                List<EntityItem> s = Lists.newArrayList();
                AtomicInteger count = new AtomicInteger();
                entities.forEach(e -> {
                    if (e.getItem().getItem().equals(Items.REDSTONE)) {
                        s.add(e);
                        count.addAndGet(e.getItem().getCount());
                    }
                });
                if (s.isEmpty())
                    return;
                ItemStack stack = new ItemStack(RegistryItems.FLUX, count.getAndIncrement());
                s.forEach(Entity::setDead);
                world.setBlockToAir(pos);
                world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack));
                world.setBlockState(pos.down(), Blocks.OBSIDIAN.getDefaultState());
                world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEntityAdded(EntityJoinWorldEvent event) {
        if (!FluxConfig.enableFluxRecipe || !FluxConfig.enableOldRecipe || event.getWorld().isRemote) {
            return;
        }
        final Entity entity = event.getEntity();
        if (entity instanceof EntityItem && !(entity instanceof EntityFireItem)) {
            EntityItem entityItem = (EntityItem) entity;
            ItemStack stack = entityItem.getItem();
            if (!stack.isEmpty() && stack.getItem() == Items.REDSTONE) {
                EntityFireItem newEntity = new EntityFireItem(entityItem);
                entityItem.setDead();

                int i = MathHelper.floor(newEntity.posX / 16.0D);
                int j = MathHelper.floor(newEntity.posZ / 16.0D);
                event.getWorld().getChunk(i, j).addEntity(newEntity);
                event.getWorld().loadedEntityList.add(newEntity);
                event.getWorld().onEntityAdded(newEntity);

                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        /*if(event.phase == TickEvent.Phase.START) {
            for(IFluxNetwork network : FluxNetworkCache.instance.getAllNetworks()) {
                network.onStartServerTick();
            }
        }*/
        if(event.phase == TickEvent.Phase.END) {
            for(IFluxNetwork network : FluxNetworkCache.instance.getAllNetworks()) {
                network.onEndServerTick();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if(!player.world.isRemote) {
            PacketHandler.network.sendTo(new PacketNetworkUpdate.NetworkUpdateMessage(new ArrayList<>(FluxNetworkCache.instance.getAllNetworks()), NBTType.NETWORK_GENERAL), (EntityPlayerMP) player);
            PacketHandler.network.sendTo(new PacketSuperAdmin.SuperAdminMessage(DefaultSuperAdmin.isPlayerSuperAdmin(player)), (EntityPlayerMP) player);
        }
    }

    @SubscribeEvent
    public void onFluxConnected(FluxConnectionEvent.Connected event) {
        if(!event.flux.getFluxWorld().isRemote) {
            event.flux.connect(event.network);
        }
    }

    @SubscribeEvent
    public void onFluxDisconnect(FluxConnectionEvent.Disconnected event) {
        if(!event.flux.getFluxWorld().isRemote) {
            event.flux.disconnect(event.network);
        }
    }

    public EnumFeedbackInfo getFeedback(boolean operation) {
        return null;
    }

    public void setFeedback(EnumFeedbackInfo info, boolean operation) {}

    public void receiveColorCache(Map<Integer, Tuple<Integer, String>> cache) {}

    public EntityPlayer getPlayer(MessageContext ctx) {
        return ctx.getServerHandler().player;
    }
}
