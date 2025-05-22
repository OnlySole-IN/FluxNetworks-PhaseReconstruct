package onlysole.fluxnetworks.common.tileentity;

import onlysole.fluxnetworks.api.network.ConnectionType;
import onlysole.fluxnetworks.api.network.ITransferHandler;
import onlysole.fluxnetworks.api.tiles.IFluxController;
import onlysole.fluxnetworks.common.connection.transfer.FluxControllerHandler;

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
