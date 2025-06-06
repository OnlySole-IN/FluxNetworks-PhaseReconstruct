package onlysole.fluxnetworks.common.tileentity;

import onlysole.fluxnetworks.FluxConfig;
import onlysole.fluxnetworks.api.network.ConnectionType;
import onlysole.fluxnetworks.api.network.ITransferHandler;
import onlysole.fluxnetworks.api.tiles.IFluxStorage;
import onlysole.fluxnetworks.api.translate.FluxTranslate;
import onlysole.fluxnetworks.common.connection.transfer.FluxStorageHandler;
import onlysole.fluxnetworks.common.core.FluxUtils;
import onlysole.fluxnetworks.common.data.FluxNetworkData;
import onlysole.fluxnetworks.common.registry.RegistryBlocks;
import li.cil.oc.api.machine.Arguments;
import mcjty.lib.api.power.IBigPower;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Optional;

import java.util.HashMap;
import java.util.Map;

@Optional.Interface(iface = "mcjty.lib.api.power.IBigPower", modid = "theoneprobe")
public class TileFluxStorage extends TileFluxCore implements IFluxStorage, IBigPower {

    private final FluxStorageHandler handler = new FluxStorageHandler(this);

    public static final int PRI_DIFF = 1000000; // to get the lowest priority across the network
    public static final int PRI_UPPER = -10000; // the priority upper limit for storages

    private boolean needSyncEnergy = false;

    private final ItemStack stack;

    public TileFluxStorage() {
        this(new ItemStack(RegistryBlocks.FLUX_STORAGE_1));
        customName = FluxTranslate.STORAGE_FLUXSTORAGE.t();
        limit = (long) FluxConfig.energystorage.basicTransfer;
    }

    private TileFluxStorage(ItemStack stack) {
        this.stack = stack;
    }

    public static class Herculean extends TileFluxStorage {

        public Herculean() {
            super(new ItemStack(RegistryBlocks.FLUX_STORAGE_2));
            customName = FluxTranslate.STORAGE_HERCULEANFLUXSTORAGE.t();
            limit = (long) FluxConfig.energystorage.herculeanTransfer;
        }

        @Override
        public long getMaxTransferLimit() {
            return (long) FluxConfig.energystorage.herculeanCapacity;
        }
    }

    public static class Gargantuan extends TileFluxStorage {

        public Gargantuan() {
            super(new ItemStack(RegistryBlocks.FLUX_STORAGE_3));
            customName = FluxTranslate.STORAGE_GARGANTUANFLUXSTORAGE.t();
            limit = (long) FluxConfig.energystorage.gargantuanTransfer;
        }

        @Override
        public long getMaxTransferLimit() {
            return (long) FluxConfig.energystorage.gargantuanCapacity;
        }
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.STORAGE;
    }

    @Override
    public ITransferHandler getTransferHandler() {
        return handler;
    }

    @Override
    public void update() {
        super.update();
        if (!world.isRemote) {
            sendPacketIfNeeded();
        }
    }

    /** on server side **/
    private void sendPacketIfNeeded() {
        if (needSyncEnergy) {
            if ((world.getWorldTime() & 3) == 0) {
                sendPackets();
                needSyncEnergy = false;
            }
        }
    }

    public void markServerEnergyChanged() {
        needSyncEnergy = true;
    }

    @Override
    public int getLogicPriority() {
        return surgeMode ? PRI_UPPER : Math.min(priority - PRI_DIFF, PRI_UPPER);
    }

    @Override
    public long getMaxTransferLimit() {
        return (long) FluxConfig.energystorage.basicCapacity;
    }

    public ItemStack writeStorageToDisplayStack(ItemStack stack) {
        NBTTagCompound tag = new NBTTagCompound();

        NBTTagCompound subTag = new NBTTagCompound();
        subTag.setLong("energy", getTransferBuffer());
        subTag.setInteger(FluxNetworkData.NETWORK_ID, networkID);

        tag.setTag(FluxUtils.FLUX_DATA, subTag);
        tag.setBoolean(FluxUtils.GUI_COLOR, true);

        stack.setTagCompound(tag);
        return stack;
    }

    @Override
    public ItemStack getDisplayStack() {
        return writeStorageToDisplayStack(stack);
    }

    @Override
    @Optional.Method(modid = "theoneprobe")
    public long getStoredPower(){
        return getTransferBuffer();
    }

    @Override
    @Optional.Method(modid = "theoneprobe")
    public long getCapacity(){
        return getMaxTransferLimit();
    }

    @Override
    public String getPeripheralName() {
        return "flux_storage";
    }

    @Override
    public Object[] invokeMethods(String method, Arguments arguments) {
        if(method.equals("getFluxInfo")) {
            Map<Object, Object> map = new HashMap<>();
            map.put("customName", customName);
            map.put("priority", priority);
            map.put("transferLimit", limit);
            map.put("surgeMode", surgeMode);
            map.put("unlimited", disableLimit);
            map.put("energyStored", getTransferBuffer());
            map.put("maxStorage", getMaxTransferLimit());
            return new Object[]{map};
        }
        return super.invokeMethods(method, arguments);
    }
}
