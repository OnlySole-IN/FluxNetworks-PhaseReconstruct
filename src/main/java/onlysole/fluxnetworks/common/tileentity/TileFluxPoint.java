package onlysole.fluxnetworks.common.tileentity;

import onlysole.fluxnetworks.api.network.ConnectionType;
import onlysole.fluxnetworks.api.network.ITransferHandler;
import onlysole.fluxnetworks.api.tiles.IFluxPoint;
import onlysole.fluxnetworks.api.translate.FluxTranslate;
import onlysole.fluxnetworks.common.connection.transfer.FluxPointHandler;

import javax.annotation.Nonnull;

public class TileFluxPoint extends TileFluxConnector implements IFluxPoint {

    private final FluxPointHandler handler = new FluxPointHandler(this);

    public TileFluxPoint() {
        customName = FluxTranslate.TRANSMIT_FLUXPOINT.t();
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
