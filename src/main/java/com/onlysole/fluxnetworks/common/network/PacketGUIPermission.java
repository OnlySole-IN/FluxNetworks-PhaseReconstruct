package com.onlysole.fluxnetworks.common.network;

import com.onlysole.fluxnetworks.api.network.AccessLevel;
import com.onlysole.fluxnetworks.client.gui.basic.GuiFluxCore;
import com.onlysole.fluxnetworks.common.handler.PacketHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketGUIPermission implements IMessageHandler<PacketGUIPermission.GUIPermissionMessage, IMessage> {

    @Override
    public IMessage onMessage(GUIPermissionMessage message, MessageContext ctx) {
        EntityPlayer player = PacketHandler.getPlayer(ctx);
        if(player != null) {
            Gui gui = Minecraft.getMinecraft().currentScreen;
            if (gui instanceof GuiFluxCore) {
                GuiFluxCore guiFluxCore = (GuiFluxCore) gui;
                guiFluxCore.accessPermission = message.accessPermission;
                guiFluxCore.onSuperAdminChanged();
            }
        }
        return null;
    }

    public static class GUIPermissionMessage implements IMessage {

        public AccessLevel accessPermission;

        public GUIPermissionMessage() {
        }

        public GUIPermissionMessage(AccessLevel permission) {
            this.accessPermission = permission;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            accessPermission = AccessLevel.values()[buf.readInt()];
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(accessPermission.ordinal());
        }
    }
}
