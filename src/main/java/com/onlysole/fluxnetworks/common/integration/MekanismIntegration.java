package com.onlysole.fluxnetworks.common.integration;

import com.onlysole.fluxnetworks.Tags;
import mekanism.api.MekanismAPI;

public class MekanismIntegration {

    public static void preInit(){
        MekanismAPI.addBoxBlacklistMod(Tags.MOD_ID);
    }
}
