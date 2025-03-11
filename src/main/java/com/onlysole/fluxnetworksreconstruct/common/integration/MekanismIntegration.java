package com.onlysole.fluxnetworksreconstruct.common.integration;

import com.onlysole.fluxnetworksreconstruct.FluxNetworksReconstruct;
import mekanism.api.MekanismAPI;

public class MekanismIntegration {

    public static void preInit(){
        MekanismAPI.addBoxBlacklistMod(FluxNetworksReconstruct.MODID);
    }
}
