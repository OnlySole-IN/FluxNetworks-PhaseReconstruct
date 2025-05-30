package onlysole.fluxnetworks.common.connection.transfer;

import onlysole.fluxnetworks.FluxConfig;
import onlysole.fluxnetworks.api.energy.ITileEnergyHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;

public class ConnectionTransfer {

    private final ITileEnergyHandler energyHandler;
    private final TileEntity tile;
    private final EnumFacing side;
    private final ItemStack displayStack;

    public long outbound;
    public long inbound;

    private boolean canAddEnergy = false;

    public ConnectionTransfer(ITileEnergyHandler energyHandler, @Nonnull TileEntity tile, @Nonnull EnumFacing dir) {
        this.energyHandler = energyHandler;
        this.tile = tile;
        this.side = dir.getOpposite(); // the tile is on our north side, we charge it from its south side
        this.displayStack = new ItemStack(tile.getBlockType());
    }

    public long sendToTile(long amount, boolean simulate) {
        if (!FluxConfig.general.connectionTransfer) {
            if (energyHandler.canAddEnergy(tile, side)) {
                long added = energyHandler.addEnergy(amount, tile, side, simulate);
                if (!simulate) {
                    inbound += added;
                }
                return added;
            }
            return 0;
        } else {
            if (simulate) {
                if (energyHandler.canAddEnergy(tile, side)) {
                    canAddEnergy = true;
                    return (energyHandler.addEnergy(amount, tile, side, true));
                }
                canAddEnergy = false;
                return 0L;
            }

            if (canAddEnergy) {
                canAddEnergy = false;
                long added = energyHandler.addEnergy(amount, tile, side, false);
                inbound += added;
                return added;
            }
            return 0L;
        }
    }

    public void onEnergyReceived(long amount) {
        outbound += amount;
    }

    public void onCycleStart() {
        outbound = 0;
        inbound = 0;
    }

    public TileEntity getTile() {
        return tile;
    }

    public ItemStack getDisplayStack() {
        return displayStack;
    }
}
