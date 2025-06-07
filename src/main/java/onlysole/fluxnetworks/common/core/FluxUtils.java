package onlysole.fluxnetworks.common.core;

import onlysole.fluxnetworks.api.network.ConnectionType;
import onlysole.fluxnetworks.api.network.FluxLogicType;
import onlysole.fluxnetworks.api.network.IFluxNetwork;
import onlysole.fluxnetworks.api.tiles.IFluxConnector;
import onlysole.fluxnetworks.api.translate.FluxTranslate;
import onlysole.fluxnetworks.api.utils.EnergyType;
import onlysole.fluxnetworks.api.utils.FluxConfigurationType;
import onlysole.fluxnetworks.client.gui.button.SlidedSwitchButton;
import onlysole.fluxnetworks.client.gui.button.TextboxButton;
import onlysole.fluxnetworks.common.connection.FluxNetworkCache;
import onlysole.fluxnetworks.common.item.ItemFluxConnector;
import onlysole.fluxnetworks.common.tileentity.TileFluxCore;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.UUID;

public class FluxUtils {

    public static final String FLUX_DATA = "FluxData";
    public static final String GUI_COLOR = "GuiColor";
    public static final String CONFIGS_TAG = "Configs";

    public static UUID UUID_DEFAULT = new UUID(-1, -1);

    public static Material MACHINE = new Material(MapColor.BLACK);

    public static <E extends Enum> E incrementEnum(E enumObj, E[] values) {
        int ordinal = enumObj.ordinal() + 1;
        if (ordinal < values.length) {
            return values[ordinal];
        } else {
            return values[0];
        }
    }

    @Nullable
    public static EnumFacing getBlockDirection(BlockPos pos, BlockPos other) {
        for(EnumFacing face : EnumFacing.VALUES) {
            if(pos.offset(face).equals(other))
                return face;
        }
        return null;
    }

    public static String getTransferInfo(ConnectionType type, EnergyType energyType, long change) {
        if(type.isPlug()) {
            String b = FluxUtils.format(change, TypeNumberFormat.COMMAS, energyType, true);
            if(change == 0) {
                return FluxTranslate.INPUT.t() + ": " + TextFormatting.GOLD + b;
            } else {
                return FluxTranslate.INPUT.t() + ": " + TextFormatting.GREEN + "+" + b;
            }
        }
        if(type.isPoint() || type.isController()) {
            String b = FluxUtils.format(-change, TypeNumberFormat.COMMAS, energyType, true);
            if(change == 0) {
                return FluxTranslate.OUTPUT.t() + ": " + TextFormatting.GOLD + b;
            } else {
                return FluxTranslate.OUTPUT.t() + ": " + TextFormatting.RED + "-" + b;
            }
        }
        // Storage are inverted
        if(type == ConnectionType.STORAGE) {
            if(change == 0) {
                return FluxTranslate.CHANGE.t() + ": " + TextFormatting.GOLD + change + energyType.getUsageSuffix();
            } else if(change > 0) {
                return FluxTranslate.CHANGE.t() + ": " + TextFormatting.GREEN + "+" + FluxUtils.format(change, TypeNumberFormat.COMMAS, energyType, true);
            } else {
                return FluxTranslate.CHANGE.t() + ": " + TextFormatting.RED + "-" + FluxUtils.format(-change, TypeNumberFormat.COMMAS, energyType, true);
            }
        }
        return "";
    }

    //注释掉的经验值(XP)计算系统
    /*public static int getPlayerXP(EntityPlayer player) {
        return (int) (getExperienceForLevel(player.experienceLevel) + (player.experience * player.xpBarCap()));
    }

    public static void addPlayerXP(EntityPlayer player, int amount) {
        int experience = getPlayerXP(player) + amount;
        player.experienceTotal = experience;
        player.experienceLevel = getLevelForExperience(experience);
        int expForLevel = getExperienceForLevel(player.experienceLevel);
        player.experience = (float) (experience - expForLevel) / (float) player.xpBarCap();
    }

    public static boolean removePlayerXP(EntityPlayer player, int amount) {
        if(getPlayerXP(player) >= amount) {
            addPlayerXP(player, -amount);
            return true;
        }
        return false;
    }

    public static int xpBarCap(int level) {
        if (level >= 30)
            return 112 + (level - 30) * 9;

        if (level >= 15)
            return 37 + (level - 15) * 5;

        return 7 + level * 2;
    }

    private static int sum(int n, int a0, int d) {
        return n * (2 * a0 + (n - 1) * d) / 2;
    }

    public static int getExperienceForLevel(int level) {
        if (level == 0) return 0;
        if (level <= 15) return sum(level, 7, 2);
        if (level <= 30) return 315 + sum(level - 15, 37, 5);
        return 1395 + sum(level - 30, 112, 9);
    }

    public static int getLevelForExperience(int targetXp) {
        int level = 0;
        while (true) {
            final int xpToNextLevel = xpBarCap(level);
            if (targetXp < xpToNextLevel) return level;
            level++;
            targetXp -= xpToNextLevel;
        }
    }*/

    public static <T> boolean addWithCheck(Collection<T> list, T toAdd) {
        if(toAdd != null && !list.contains(toAdd)) {
            list.add(toAdd);
            return true;
        }
        return false;
    }

    public static ItemStack getBlockItem(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return new ItemStack(Item.getItemFromBlock(state.getBlock()));
    }


    public static boolean addConnection(IFluxConnector fluxConnector) {
        if(fluxConnector.getNetworkID() != -1) {
            IFluxNetwork network = FluxNetworkCache.instance.getNetwork(fluxConnector.getNetworkID());
            if(!network.isInvalid()) {
                if(fluxConnector.getConnectionType().isController() && network.getConnections(FluxLogicType.CONTROLLER).size() > 0) {
                    return false;
                }
                network.queueConnectionAddition(fluxConnector);
                return true;
            }
        }
        return false;
    }

    public static void removeConnection(IFluxConnector fluxConnector, boolean isChunkUnload) {
        if(fluxConnector.getNetworkID() != -1) {
            IFluxNetwork network = FluxNetworkCache.instance.getNetwork(fluxConnector.getNetworkID());
            if(!network.isInvalid()) {
                network.queueConnectionRemoval(fluxConnector, isChunkUnload);
                return;
            }
        }
    }

    public static int getIntFromColor(int red, int green, int blue) {
        red = red << 16 & 0x00FF0000;
        green = green << 8 & 0x0000FF00;
        blue = blue & 0x000000FF;

        return 0xFF000000 | red | green | blue;
    }

    public static int getBrighterColor(int color, double index) {
        int red = (color >> 16) & 0x000000FF;
        int green = (color >> 8) & 0x000000FF;
        int blue = (color) & 0x000000FF;
        return getIntFromColor((int) Math.min(red * index, 255), (int) Math.min(green * index, 255), (int) Math.min(blue * index, 255));
    }

    public enum TypeNumberFormat {
        FULL,                   // 完整显示模式（如：1000000FE）
        COMPACT,                // 紧凑格式（如：1.0MFE）
        COMMAS,                 // 千分位分隔格式（如：1,000,000FE）
        NONE                    // 仅显示单位（如：FE）
    }

    public static String format(long in, TypeNumberFormat style, String suffix) {
        switch (style) {
            case FULL:
                return in + suffix;
            case COMPACT: {
                int unit = 1000;
                if (in < unit) {
                    return in + " " + suffix;
                }
                int exp = (int) (Math.log(in) / Math.log(unit));
                char pre;
                if (suffix.startsWith("m")) {
                    suffix = suffix.substring(1);
                    if (exp - 2 >= 0) {
                        pre = "kMGTPE".charAt(exp - 2);
                        return String.format("%.1f%s", in / Math.pow(unit, exp), pre) + suffix;
                    } else {
                        return String.format("%.1f%s", in / Math.pow(unit, exp), suffix);
                    }
                } else {
                    pre = "kMGTPE".charAt(exp - 1);
                    return String.format("%.1f%s", in / Math.pow(unit, exp), pre) + suffix;
                }
            }
            case COMMAS:
                return NumberFormat.getInstance().format(in) + suffix;
            case NONE:
                return suffix;
        }
        return Long.toString(in);
    }

    public static String format(long in, TypeNumberFormat style, EnergyType energy, boolean usage) {
        if(energy == EnergyType.EU) {
            return format(in / 4, style, usage ? energy.getUsageSuffix() : energy.getStorageSuffix());
        }
        return format(in, style, usage ? energy.getUsageSuffix() : energy.getStorageSuffix());
    }

    public static boolean checkPassword(String str) {
        for(int i = 0; i < str.length(); i++) {
            if(!Character.isLetterOrDigit(str.charAt(i)))
                return false;
        }
        return true;
    }

    public static NBTTagCompound copyConfiguration(TileFluxCore flux, NBTTagCompound config) {
        for(FluxConfigurationType type : FluxConfigurationType.VALUES){
            type.copy.copyFromTile(config, type.getNBTName(), flux);
        }
        return config;
    }

    public static void pasteConfiguration(TileFluxCore flux, NBTTagCompound config) {
        for(FluxConfigurationType type : FluxConfigurationType.VALUES){
            if(config.hasKey(type.getNBTName())) {
                type.paste.pasteToTile(config, type.getNBTName(), flux);
            }
        }
    }

    public static NBTTagCompound getBatchEditingTag(TextboxButton a, TextboxButton b, TextboxButton c, SlidedSwitchButton d, SlidedSwitchButton e, SlidedSwitchButton f) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString(ItemFluxConnector.CUSTOM_NAME, a.getText());
        tag.setInteger(ItemFluxConnector.PRIORITY, b.getIntegerFromText(false));
        tag.setLong(ItemFluxConnector.LIMIT, c.getLongFromText(true));
        tag.setBoolean(ItemFluxConnector.SURGE_MODE, d != null && d.slideControl);
        tag.setBoolean(ItemFluxConnector.DISABLE_LIMIT, e != null && e.slideControl);
        tag.setBoolean("chunkLoad", f != null && f.slideControl);
        return tag;
    }

}
