package com.onlysole.fluxnetworksreconstruct.common.integration;

import com.onlysole.fluxnetworksreconstruct.Tags;
import mekanism.api.MekanismAPI;

public class MekanismIntegration {

    public static void preInit(){
        MekanismAPI.addBoxBlacklistMod(Tags.MOD_ID);
    }
}
