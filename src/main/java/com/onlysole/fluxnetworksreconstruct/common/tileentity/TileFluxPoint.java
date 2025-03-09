package com.onlysole.fluxnetworksreconstruct.common.tileentity;

import com.onlysole.fluxnetworksreconstruct.api.network.ConnectionType;
import com.onlysole.fluxnetworksreconstruct.api.network.ITransferHandler;
import com.onlysole.fluxnetworksreconstruct.api.tiles.IFluxPoint;
import com.onlysole.fluxnetworksreconstruct.common.connection.transfer.FluxPointHandler;

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
