package com.onlysole.fluxnetworks;

import com.onlysole.fluxnetworks.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Tags.MOD_ID,
        name = Tags.MOD_NAME,
        version = Tags.VERSION,
        dependencies = "required-after:forge@[14.23.4.2854,)",
        acceptedMinecraftVersions = "[1.12.2]",
        guiFactory = "com.onlysole.fluxnetworks.common.core.ConfigGuiFactory"
)
public class FluxNetworks {

    @Mod.Instance(Tags.MOD_ID)
    public static FluxNetworks instance;

    public static Logger logger = LogManager.getLogger(Tags.MOD_NAME);

    @SidedProxy(
            clientSide = "com.onlysole.fluxnetworks.client.ClientProxy",
            serverSide = "com.onlysole.fluxnetworks.common.CommonProxy"
    )
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        proxy.onServerStarted();
    }

    @Mod.EventHandler
    public void onServerStopped(FMLServerStoppedEvent event) {
        proxy.onServerStopped();
    }
}
