package onlysole.fluxnetworks.client.gui;

import onlysole.fluxnetworks.FluxConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;
import onlysole.fluxnetworks.Tags;

public class GuiModConfig extends GuiConfig {

    public GuiModConfig(GuiScreen parentScreen) {
        super(parentScreen, null, Tags.MOD_ID, false, false, Tags.MOD_NAME);
    }


//    private static List<IConfigElement> getConfigElements() {
//        /*        list.add(new ConfigElement(FluxConfig.config.getCategory(FluxConfig.GENERAL)));
//        list.add(new ConfigElement(FluxConfig.config.getCategory(FluxConfig.CLIENT)));
//        list.add(new ConfigElement(FluxConfig.config.getCategory(FluxConfig.NETWORKS)));
//        list.add(new ConfigElement(FluxConfig.config.getCategory(FluxConfig.ENERGY)));
//        list.add(new ConfigElement(FluxConfig.config.getCategory(FluxConfig.BLACKLIST)));*/
//        return new ArrayList<>();
//    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        FluxConfig.verifyAndReadBlacklist();
    }
}
