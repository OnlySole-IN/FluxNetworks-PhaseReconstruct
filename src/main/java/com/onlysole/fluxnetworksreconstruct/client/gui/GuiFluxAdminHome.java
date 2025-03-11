package com.onlysole.fluxnetworksreconstruct.client.gui;

import com.onlysole.fluxnetworksreconstruct.FluxNetworksReconstruct;
import com.onlysole.fluxnetworksreconstruct.api.gui.EnumNavigationTabs;
import com.onlysole.fluxnetworksreconstruct.api.network.AccessLevel;
import com.onlysole.fluxnetworksreconstruct.api.network.INetworkConnector;
import com.onlysole.fluxnetworksreconstruct.api.network.NetworkSettings;
import com.onlysole.fluxnetworksreconstruct.api.utils.NBTType;
import com.onlysole.fluxnetworksreconstruct.client.gui.basic.GuiButtonCore;
import com.onlysole.fluxnetworksreconstruct.client.gui.basic.GuiTabCore;
import com.onlysole.fluxnetworksreconstruct.client.gui.button.SlidedSwitchButton;
import com.onlysole.fluxnetworksreconstruct.common.connection.FluxNetworkCache;
import com.onlysole.fluxnetworksreconstruct.common.handler.PacketHandler;
import com.onlysole.fluxnetworksreconstruct.common.network.PacketActivateSuperAdmin;
import com.onlysole.fluxnetworksreconstruct.common.network.PacketNetworkUpdateRequest;
import com.onlysole.fluxnetworksreconstruct.common.network.PacketPermissionRequest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

public class GuiFluxAdminHome extends GuiTabCore {

    private int timer;
    public SlidedSwitchButton detailed_network_view, super_admin;

    public GuiFluxAdminHome(EntityPlayer player, INetworkConnector tileEntity) {
        super(player, tileEntity);
    }

    public EnumNavigationTabs getNavigationTab(){
        return EnumNavigationTabs.TAB_HOME;
    }

    @Override
    protected void drawForegroundLayer(int mouseX, int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        renderNetwork(network.getSetting(NetworkSettings.NETWORK_NAME), network.getSetting(NetworkSettings.NETWORK_COLOR), 20, 8);
        drawCenteredString(fontRenderer, TextFormatting.RED + FluxNetworksReconstruct.proxy.getFeedback(false).getInfo(), 89, 150, 0xffffff);

        fontRenderer.drawString(AccessLevel.SUPER_ADMIN.getName(), 20, 30, network.getSetting(NetworkSettings.NETWORK_COLOR));
        fontRenderer.drawString("Detailed Network View", 20, 42, network.getSetting(NetworkSettings.NETWORK_COLOR));
    }

    @Override
    public void initGui() {
        super.initGui();
        configureNavigationButtons(EnumNavigationTabs.TAB_HOME, navigationTabs);

        super_admin = new SlidedSwitchButton(140, 30, 0, guiLeft, guiTop, FluxNetworkCache.instance.superAdminClient);
        switches.add(super_admin);

        detailed_network_view = new SlidedSwitchButton(140, 42, 1, guiLeft, guiTop, FluxNetworksReconstruct.proxy.detailed_network_view);
        switches.add(detailed_network_view);
    }


    public void onSuperAdminChanged(){
        super.onSuperAdminChanged();
        super_admin.slideControl = FluxNetworkCache.instance.superAdminClient;
    }

    public void onButtonClicked(GuiButtonCore button, int mouseX, int mouseY, int mouseButton){
        super.onButtonClicked(button, mouseX, mouseY, mouseButton);
        if(mouseButton == 0 && button instanceof SlidedSwitchButton) {
            SlidedSwitchButton switchButton = (SlidedSwitchButton) button;
            switchButton.switchButton();
            switch (switchButton.id) {
                case 0:
                    PacketHandler.network.sendToServer(new PacketActivateSuperAdmin.ActivateSuperAdminMessage());
                    break;
                case 1:
                    FluxNetworksReconstruct.proxy.detailed_network_view = switchButton.slideControl;
                    break;
            }
        }

    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if(timer == 0) {
            PacketHandler.network.sendToServer(new PacketNetworkUpdateRequest.UpdateRequestMessage(network.getNetworkID(), NBTType.NETWORK_GENERAL));
            PacketHandler.network.sendToServer(new PacketPermissionRequest.PermissionRequestMessage(network.getNetworkID(), player.getUniqueID()));
        }
        timer++;
        timer %= 100;
    }

}
