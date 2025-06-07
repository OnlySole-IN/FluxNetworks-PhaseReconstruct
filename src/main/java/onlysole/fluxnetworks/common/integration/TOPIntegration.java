package onlysole.fluxnetworks.common.integration;

import onlysole.fluxnetworks.FluxConfig;
import onlysole.fluxnetworks.Tags;
import onlysole.fluxnetworks.api.tiles.IFluxConnector;
import onlysole.fluxnetworks.api.translate.FluxTranslate;
import onlysole.fluxnetworks.api.utils.EnergyType;
import onlysole.fluxnetworks.common.block.BlockFluxCore;
import onlysole.fluxnetworks.common.core.FluxUtils;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.text.NumberFormat;
import java.util.function.Function;

public class TOPIntegration implements Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe iTheOneProbe) {
        iTheOneProbe.registerProvider(new FluxConnectorInfoProvider());
        iTheOneProbe.registerBlockDisplayOverride(new FluxConnectorDisplayOverride());
        return null;
    }

    public static class FluxConnectorInfoProvider implements IProbeInfoProvider {

        @Override
        public String getID() {
            return Tags.MOD_ID;
        }

        @Override
        public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, EntityPlayer entityPlayer, World world, IBlockState iBlockState, IProbeHitData iProbeHitData) {
            if (!(FluxConfig.client.enableOneProbeBasicInfo || FluxConfig.client.enableOneProbeAdvancedInfo)) {
                return;
            }

            if (iBlockState.getBlock() instanceof BlockFluxCore) {
                TileEntity tile = world.getTileEntity(iProbeHitData.getPos());
                if (tile instanceof IFluxConnector) {
                    IFluxConnector flux = (IFluxConnector) tile;

                    if (FluxConfig.client.enableOneProbeBasicInfo) {

                        // 显示网络状态（水蓝色）
                        iProbeInfo.text(TextFormatting.AQUA + (flux.getNetwork().isInvalid() ? FluxTranslate.ERROR_NO_SELECTED.t() : flux.getNetwork().getNetworkName()));

                        // 显示传输速率
                        iProbeInfo.text(FluxUtils.getTransferInfo(flux.getConnectionType(), flux.getNetwork().getEnergyType(), flux.getTransferChange()));

                        // 根据潜行状态显示不同格式的能源信息
                        final boolean isStorage = flux.getConnectionType().isStorage();
                        final String energyKey = isStorage ? FluxTranslate.ENERGY_STORED.t() : FluxTranslate.INTERNAL_BUFFER.t();

                        final boolean isTypeNumberFormat = entityPlayer.isSneaking();
                        final FluxUtils.TypeNumberFormat typeNumberFormat = isTypeNumberFormat ? FluxUtils.TypeNumberFormat.COMMAS : FluxUtils.TypeNumberFormat.COMPACT;

                        iProbeInfo.text(energyKey + ": " + TextFormatting.GREEN + FluxUtils.format(flux.getTransferBuffer(), typeNumberFormat, flux.getNetwork().getEnergyType(), false));
                    }
                    if (FluxConfig.client.enableOneProbeAdvancedInfo && (!FluxConfig.client.enableOneProbeSneaking || entityPlayer.isSneaking())) {
                        iProbeInfo.text(FluxTranslate.TRANSFER_LIMIT.t() + ": " + TextFormatting.GREEN + (flux.getDisableLimit() ? FluxTranslate.UNLIMITED.t() : flux.getRawLimit()));
                        iProbeInfo.text(FluxTranslate.PRIORITY.t() + ": " + TextFormatting.GREEN + (flux.getSurgeMode() ? FluxTranslate.SURGE.t() : flux.getRawPriority()));
                        if (flux.isForcedLoading()) {
                            iProbeInfo.text(TextFormatting.GOLD + FluxTranslate.FORCED_LOADING.t());
                        }
                    }
                }
            }
        }
    }

    public static class FluxConnectorDisplayOverride implements IBlockDisplayOverride {

        @Override
        public boolean overrideStandardInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, EntityPlayer entityPlayer, World world, IBlockState iBlockState, IProbeHitData iProbeHitData) {
            if(iBlockState.getBlock() instanceof BlockFluxCore) {
                TileEntity tile = world.getTileEntity(iProbeHitData.getPos());
                if(tile instanceof IFluxConnector) {
                    IFluxConnector flux = (IFluxConnector) tile;
                    ItemStack pickBlock = flux.getDisplayStack().setStackDisplayName(flux.getCustomName());
                    iProbeInfo.horizontal().item(pickBlock).vertical().itemLabel(pickBlock).text(TextStyleClass.MODNAME + Tags.MOD_NAME);
                    return true;
                }
            }
            return false;
        }
    }
}
