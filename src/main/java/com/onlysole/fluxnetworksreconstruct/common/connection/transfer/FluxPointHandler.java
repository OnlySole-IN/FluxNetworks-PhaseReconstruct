package com.onlysole.fluxnetworksreconstruct.common.connection.transfer;

import com.onlysole.fluxnetworksreconstruct.common.tileentity.TileFluxPoint;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;

public class FluxPointHandler extends BasicPointHandler<TileFluxPoint> {

    private final Map<EnumFacing, ConnectionTransfer> transfers = new EnumMap<>(EnumFacing.class);

    public FluxPointHandler(TileFluxPoint fluxPoint) {
        super(fluxPoint);
    }

    @Override
    public void onCycleStart() {
        for (ConnectionTransfer transfer : transfers.values()) {
            if (transfer != null) {
                transfer.onCycleStart();
            }
        }
        demand = sendToConsumers(device.getLogicLimit(), true);
    }

    @Override
    public long sendToConsumers(long energy, boolean simulate) {
        if (!device.isActive()) {
            return 0;
        }
        long leftover = energy;
        for (ConnectionTransfer transfer : transfers.values()) {
            if (transfer != null) {
                leftover -= transfer.sendToTile(leftover, simulate);
                if (leftover <= 0) {
                    return energy;
                }
            }
        }
        return energy - leftover;
    }

    @Override
    public void updateTransfers(@Nonnull EnumFacing... faces) {
        FluxPlugHandler.updateSidedTransfers(device.getFluxWorld(), device.getPos(), transfers, faces);
    }
}
