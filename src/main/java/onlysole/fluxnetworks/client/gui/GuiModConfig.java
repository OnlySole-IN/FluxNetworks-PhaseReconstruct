package onlysole.fluxnetworks.client.gui;

import onlysole.fluxnetworks.FluxConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;
import onlysole.fluxnetworks.Tags;

public class GuiModConfig extends GuiConfig {

    public GuiModConfig(GuiScreen parentScreen) {
        super(parentScreen, null, Tags.MOD_NAME, false, false, Tags.MOD_NAME);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        FluxConfig.verifyAndReadBlacklist();
    }
}
