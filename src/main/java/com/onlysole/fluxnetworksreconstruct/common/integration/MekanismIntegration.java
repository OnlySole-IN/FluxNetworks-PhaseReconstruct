package com.onlysole.fluxnetworksreconstruct.common.integration;

import com.onlysole.fluxnetworksreconstruct.FluxNetworks;
import mekanism.api.MekanismAPI;

public class MekanismIntegration {

    public static void preInit(){
        MekanismAPI.addBoxBlacklistMod(FluxNetworks.MODID);
    }
}
