package onlysole.fluxnetworks.api.tiles;

import onlysole.fluxnetworks.api.network.ConnectionType;
import onlysole.fluxnetworks.api.network.IFluxNetwork;
import onlysole.fluxnetworks.api.network.INetworkConnector;
import onlysole.fluxnetworks.api.network.ITransferHandler;
import onlysole.fluxnetworks.api.utils.Coord4D;
import onlysole.fluxnetworks.api.utils.EnergyType;
import onlysole.fluxnetworks.api.utils.NBTType;
import onlysole.fluxnetworks.common.core.FluxGuiStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * Extended by IFluxPoint and IFluxPlug
 */
public interface IFluxConnector extends INetworkConnector {

    /**用于NBT数据的读写*/
    NBTTagCompound writeCustomNBT(NBTTagCompound tag, NBTType type);

     /**用于NBT数据的读写*/
    void readCustomNBT(NBTTagCompound tag, NBTType type);

    /**获取优先级（考虑浪涌模式）*/
    int getLogicPriority();

    /**获取默认优先级*/
    int getRawPriority(); // ignore surge

    /***/
    UUID getConnectionOwner();

    /** 获取设备类型（POINT/PLUG/CONTROLLER）*/
    ConnectionType getConnectionType();

    /**检查玩家是否有操作权限*/
    boolean canAccess(EntityPlayer player);

     /**获取实际生效的传输上限（考虑禁用限制设置）*/
    long getLogicLimit();

    /**获取默认配置(忽略 禁用 限制)*/
    long getRawLimit();

    /**
     * 如果此设备是存储设备，该方法返回其最大能量存储容量；
     * 否则返回 Long.MAX_VALUE。
     *
     * @return 最大传输限制
     */
    long getMaxTransferLimit();

    /**设备是否处于激活状态*/
    boolean isActive();

     /**是否区块加载*/
    boolean isChunkLoaded();

    /**是否强制加载*/
    boolean isForcedLoading();

     /**用于连接网络*/
    void connect(IFluxNetwork network);

     /**用于断开网络*/
    void disconnect(IFluxNetwork network);

     /**获取能量传输处理器*/
    ITransferHandler getTransferHandler();

    /**获取设备所在维度*/
    World getFluxWorld();

     /**获取设备所在坐标*/
    Coord4D getCoords();

    /***/
    int getFolderID();

     /**获取自定义名称*/
    String getCustomName();

     /**是否禁用传输限制*/
    boolean getDisableLimit();

     /**是否开启浪涌模式*/
    boolean getSurgeMode();

    /**
     * 传输处理器在客户端不可用，此方法主要用于客户端的GUI显示。
     * 若此设备为存储设备，则返回其存储的能量值。
     * <p>
     * 内部缓冲区值*/
    long getTransferBuffer();

    /**存储的能量值*/
    long getTransferChange();

    /***/
    default void setChunkLoaded(boolean chunkLoaded) {}

    /**返回GUI显示的物品图标*/
    default ItemStack getDisplayStack() {
        switch (getConnectionType()) {
            case POINT:
                return FluxGuiStack.FLUX_POINT;
            case PLUG:
                return FluxGuiStack.FLUX_PLUG;
            case CONTROLLER:
                return FluxGuiStack.FLUX_CONTROLLER;
            default:
                return ItemStack.EMPTY;
        }
    }
}
