package onlysole.fluxnetworks.common.tileentity;

import onlysole.fluxnetworks.api.network.ConnectionType;
import onlysole.fluxnetworks.api.network.ITransferHandler;
import onlysole.fluxnetworks.api.tiles.IFluxPlug;
import onlysole.fluxnetworks.api.translate.FluxTranslate;
import onlysole.fluxnetworks.common.connection.transfer.FluxPlugHandler;

public class TileFluxPlug extends TileFluxConnector implements IFluxPlug {

    private final FluxPlugHandler handler = new FluxPlugHandler(this);

    public TileFluxPlug() {
        customName = FluxTranslate.TRANSMIT_FLUXPLUG.t();
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
