package com.onlysole.fluxnetworks.common.tileentity;

import com.onlysole.fluxnetworks.api.network.ConnectionType;
import com.onlysole.fluxnetworks.api.network.ITransferHandler;
import com.onlysole.fluxnetworks.api.tiles.IFluxPlug;
import com.onlysole.fluxnetworks.common.connection.transfer.FluxPlugHandler;

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
