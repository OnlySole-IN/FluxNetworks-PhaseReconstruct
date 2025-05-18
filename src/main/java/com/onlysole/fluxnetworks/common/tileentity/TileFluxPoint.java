package com.onlysole.fluxnetworks.common.tileentity;

import com.onlysole.fluxnetworks.api.network.ConnectionType;
import com.onlysole.fluxnetworks.api.network.ITransferHandler;
import com.onlysole.fluxnetworks.api.tiles.IFluxPoint;
import com.onlysole.fluxnetworks.common.connection.transfer.FluxPointHandler;

import javax.annotation.Nonnull;

public class TileFluxPoint extends TileFluxConnector implements IFluxPoint {

    private final FluxPointHandler handler = new FluxPointHandler(this);

    public TileFluxPoint() {
        customName = "Flux Point";
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.POINT;
    }

    @Nonnull
    @Override
    public ITransferHandler getTransferHandler() {
        return handler;
    }

    @Override
    public String getPeripheralName() {
        return "flux_point";
    }
}
