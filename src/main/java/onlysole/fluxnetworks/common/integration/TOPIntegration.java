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
                        final String networkStatus = flux.getNetwork().isInvalid() ?
                                FluxTranslate.ERROR_NO_SELECTED.t() : flux.getNetwork().getNetworkName();
                        iProbeInfo.text(TextFormatting.AQUA + networkStatus);
                        iProbeInfo.text(FluxUtils.getTransferInfo(flux.getConnectionType(), getETEUAndEU(), flux.getTransferChange()));

                        final boolean isStorage = flux.getConnectionType().isStorage();
                        final String energyKey = isStorage ? FluxTranslate.ENERGY_STORED.t() : FluxTranslate.INTERNAL_BUFFER.t();
                        final String formattedValue = entityPlayer.isSneaking() ?
                                NumberFormat.getInstance().format(flux.getTransferBuffer()) + getEUAndEU() :
                                FluxUtils.format(flux.getTransferBuffer(), FluxUtils.TypeNumberFormat.COMPACT, getEUAndEU());

                        iProbeInfo.text(energyKey + ": " + TextFormatting.GREEN + formattedValue);
                    }

                    if (FluxConfig.client.enableOneProbeAdvancedInfo &&
                            (!FluxConfig.client.enableOneProbeSneaking || entityPlayer.isSneaking())) {
                        iProbeInfo.text(FluxTranslate.TRANSFER_LIMIT.t() + ": " + TextFormatting.GREEN +
                                (flux.getDisableLimit() ? FluxTranslate.UNLIMITED.t() : flux.getRawLimit()));
                        iProbeInfo.text(FluxTranslate.PRIORITY.t() + ": " + TextFormatting.GREEN +
                                (flux.getSurgeMode() ? FluxTranslate.SURGE.t() : flux.getRawPriority()));
                        if (flux.isForcedLoading()) {
                            iProbeInfo.text(TextFormatting.GOLD + FluxTranslate.FORCED_LOADING.t());
                        }
                    }
                }
            }
        }
    }

    public static EnergyType getETEUAndEU() {
        if (FluxConfig.client.topDisplayRFAndEU) {
            return EnergyType.RF;
        }
        return EnergyType.EU;
    }

    public static String getEUAndEU() {
        if (FluxConfig.client.topDisplayRFAndEU) {
            return " RF";
        }
        return " EU";
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
