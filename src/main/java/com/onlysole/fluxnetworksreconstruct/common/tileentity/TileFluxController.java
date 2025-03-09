package com.onlysole.fluxnetworksreconstruct.common.tileentity;

import com.onlysole.fluxnetworksreconstruct.api.network.ConnectionType;
import com.onlysole.fluxnetworksreconstruct.api.network.ITransferHandler;
import com.onlysole.fluxnetworksreconstruct.api.tiles.IFluxController;
import com.onlysole.fluxnetworksreconstruct.common.connection.transfer.FluxControllerHandler;

public class TileFluxController extends TileFluxCore implements IFluxController {

    private final FluxControllerHandler handler = new FluxControllerHandler(this);

    public TileFluxController() {
        customName = "Flux Controller";
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.CONTROLLER;
    }

    @Override
    public ITransferHandler getTransferHandler() {
        return handler;
    }

    @Override
    public String getPeripheralName() {
        return "flux_controller";
    }
}
