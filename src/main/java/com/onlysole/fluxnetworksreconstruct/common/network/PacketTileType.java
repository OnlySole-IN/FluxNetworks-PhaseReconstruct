package com.onlysole.fluxnetworksreconstruct.common.network;

import com.onlysole.fluxnetworksreconstruct.common.tileentity.TileFluxCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public enum PacketTileType {
    SET_NETWORK(PacketTileHandler::handleSetNetworkPacket),
    CHUNK_LOADING(PacketTileHandler::handleChunkLoadPacket);

    public IPacketTileHandler handler;

    PacketTileType(IPacketTileHandler handler) {
        this.handler = handler;
    }

    public interface IPacketTileHandler {
        IMessage handlePacket(TileFluxCore tile, EntityPlayer player, NBTTagCompound nbtTag);
    }
}
