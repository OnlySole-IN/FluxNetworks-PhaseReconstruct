package onlysole.fluxnetworks.common.integration.mekanism;

import net.minecraftforge.fml.common.Optional;
import onlysole.fluxnetworks.FluxNetworks;
import onlysole.fluxnetworks.Tags;
import mekanism.api.MekanismAPI;
import onlysole.fluxnetworks.common.handler.TileEntityHandler;
import onlysole.fluxnetworks.common.mod.Mods;

public class MekanismIntegration {

    public static void preInit(){
        MekanismAPI.addBoxBlacklistMod(Tags.MOD_ID);
        if (Mods.MEK.loaded() && !Mods.MEKCEU.loaded()) {
            initMekanismIntegration();
        }
    }

    @Optional.Method(modid = "mekanism")
    public static void initMekanismIntegration() {
        //在列表头部插入适配器，保证不被其他类型覆盖结果。
        //Insert adapters in the head of the list to ensure that the results are not overwritten by other types.
        TileEntityHandler.tileEnergyHandlers.add(0, MekanismEnergyHandler.INSTANCE);
        FluxNetworks.logger.info("Mekanism <===> FluxNetworks is initialized!");
    }
}
