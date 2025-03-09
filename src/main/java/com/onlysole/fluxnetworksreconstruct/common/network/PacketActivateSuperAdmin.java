package com.onlysole.fluxnetworksreconstruct.common.network;

import com.onlysole.fluxnetworksreconstruct.FluxNetworks;
import com.onlysole.fluxnetworksreconstruct.api.network.ISuperAdmin;
import com.onlysole.fluxnetworksreconstruct.api.utils.Capabilities;
import com.onlysole.fluxnetworksreconstruct.common.capabilities.DefaultSuperAdmin;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketActivateSuperAdmin implements IMessageHandler<PacketActivateSuperAdmin.ActivateSuperAdminMessage, IMessage> {

    @SuppressWarnings("ConstantConditions")
    @Override
    public IMessage onMessage(ActivateSuperAdminMessage message, MessageContext ctx) {
        EntityPlayer player = FluxNetworks.proxy.getPlayer(ctx);

        ISuperAdmin iSuperAdmin = player.getCapability(Capabilities.SUPER_ADMIN, null);
        if(iSuperAdmin != null){
            if(iSuperAdmin.getPermission() || DefaultSuperAdmin.canActivateSuperAdmin(player)){
                iSuperAdmin.changePermission();
                return new PacketSuperAdmin.SuperAdminMessage(iSuperAdmin.getPermission());
            }
        }
        return null;
    }

    public static class ActivateSuperAdminMessage implements IMessage {

        public ActivateSuperAdminMessage() {}
        @Override
        public void fromBytes(ByteBuf buf) {}

        @Override
        public void toBytes(ByteBuf buf) {}
    }
}
