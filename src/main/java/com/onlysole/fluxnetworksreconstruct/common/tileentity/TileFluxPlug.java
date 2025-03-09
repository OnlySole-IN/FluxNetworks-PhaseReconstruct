package com.onlysole.fluxnetworksreconstruct.common.tileentity;

import com.onlysole.fluxnetworksreconstruct.api.network.ConnectionType;
import com.onlysole.fluxnetworksreconstruct.api.network.ITransferHandler;
import com.onlysole.fluxnetworksreconstruct.api.tiles.IFluxPlug;
import com.onlysole.fluxnetworksreconstruct.common.connection.transfer.FluxPlugHandler;

public class TileFluxPlug extends TileFluxConnector implements IFluxPlug {

    private final FluxPlugHandler handler = new FluxPlugHandler(this);

    public TileFluxPlug() {
        customName = "Flux Plug";
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.PLUG;
    }

    @Override
    public ITransferHandler getTransferHandler() {
        return handler;
    }

    @Override
    public String getPeripheralName() {
        return "flux_plug";
    }
}
