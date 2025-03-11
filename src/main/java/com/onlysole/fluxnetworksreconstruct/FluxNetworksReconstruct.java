package com.onlysole.fluxnetworksreconstruct;

import com.onlysole.fluxnetworksreconstruct.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = FluxNetworksReconstruct.MODID,
        name = FluxNetworksReconstruct.NAME,
        version = FluxNetworksReconstruct.VERSION,
        dependencies = "required-after:forge@[14.23.4.2854,)",
        acceptedMinecraftVersions = "[1.12.2]",
        guiFactory = "com.onlysole.fluxnetworksreconstruct.common.core.ConfigGuiFactory"
)
public class FluxNetworksReconstruct {

    public static final String MODID = "fluxnetworksreconstruct";
    public static final String NAME = "Flux Networks Reconstruct";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MODID)
    public static FluxNetworksReconstruct instance;

    public static Logger logger = LogManager.getLogger("FluxNetworksReconstruct");

    @SidedProxy(
            clientSide = "com.onlysole.fluxnetworksreconstruct.client.ClientProxy",
            serverSide = "com.onlysole.fluxnetworksreconstruct.common.CommonProxy"
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
