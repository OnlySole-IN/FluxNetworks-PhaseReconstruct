package onlysole.fluxnetworks.common.config;

import com.cleanroommc.configanytime.ConfigAnytime;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import onlysole.fluxnetworks.FluxNetworks;
import onlysole.fluxnetworks.Tags;
import onlysole.fluxnetworks.common.handler.ItemEnergyHandler;
import onlysole.fluxnetworks.common.handler.TileEntityHandler;
import net.minecraftforge.common.ForgeChunkManager;

import java.io.File;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
@Config(modid = Tags.MOD_ID, name = Tags.MOD_ID)
public class FluxConfig {

    @Config.Name("Energy")
    public static final Energy energy = new Energy();

    @Config.Name("Networks")
    public static final Networks networks = new Networks();

    @Config.Name("General")
    public static final General general = new General();

    @Config.Name("Client")
    public static final Client client = new Client();

    @Config.Name("BlackList")
    public static final BlackList blacklist = new BlackList();

    @Config.Name("Mixin")
    public static final Mixin mixin = new Mixin();

    public static class Energy {

        @Config.Comment({"The default transfer limit of a flux connector"})
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = 0, max = Integer.MAX_VALUE)
        @Config.Name("Default Transfer Limit")
        public int defaultLimit = 800000;

        @Config.Comment({""})
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = 0, max = Integer.MAX_VALUE)
        @Config.Name("Basic Storage Capacity")
        public int basicCapacity = 1000000;

        @Config.Comment({""})
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = 0, max = Integer.MAX_VALUE)
        @Config.Name("Basic Storage Transfer")
        public int basicTransfer = 20000;

        @Config.Comment({""})
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = 0, max = Integer.MAX_VALUE)
        @Config.Name("Herculean Storage Capacity")
        public int herculeanCapacity = 8000000;

        @Config.Comment({""})
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = 0, max = Integer.MAX_VALUE)
        @Config.Name("Herculean Storage Transfer")
        public int herculeanTransfer = 120000;

        @Config.Comment({""})
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = 0, max = Integer.MAX_VALUE)
        @Config.Name("Gargantuan Storage Capacity")
        public int gargantuanCapacity = 128000000;

        @Config.Comment({""})
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = 0, max = Integer.MAX_VALUE)
        @Config.Name("Gargantuan Storage Transfer")
        public int gargantuanTransfer = 1440000;


    }

    public static class Networks {

        @Config.Comment({"Maximum networks each player can have. -1 = no limit"})
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = -1, max = Integer.MAX_VALUE)
        @Config.Name("Maximum Networks Per Player")
        public int maximumPerPlayer = 3;

        @Config.Comment({"Allows someone to be a network super admin, otherwise, no one can access or dismantle your flux devices or delete your networks without permission"})
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = 0.25F, max = 1.0F)
        @Config.Name("Allow Network Super Admin")
        public boolean enableSuperAdmin = true;

        @Config.Comment({
                "See ops.json. If the player has permission level equal or greater to the value set here they will be able to Activate Super Admin.",
                " Setting this to 0 will allow anyone to active Super Admin."
        })
        @Config.RequiresMcRestart
        @Config.RangeDouble(min = 0, max = Integer.MAX_VALUE)
        @Config.Name("Permission level required to activate Super Admin")
        public int superAdminRequiredPermission = 1;
    }

    public static class General {

        @Config.Comment({"Enables redstones being compressed with the bedrock and obsidian to get flux"})
        @Config.RequiresMcRestart
        @Config.Name("Enable Flux Recipe")
        public boolean enableFluxRecipe = true;

        @Config.Comment({
                "Enables redstone being turned into Flux when dropped in fire. ",
                "(Need \"Enable Flux Recipe\" = true, so the default recipe can't be disabled if turns this on)"
        })
        @Config.RequiresMcRestart
        @Config.Name("Enable Old Recipe")
        public boolean enableOldRecipe = false;

        @Config.Comment({"Allows flux tiles to work as chunk loaders"})
        @Config.RequiresMcRestart
        @Config.Name("Allow Flux Chunk Loading")
        public boolean enableChunkLoading = true;
    }

    public static class Client {

        @Config.Comment({"Enable navigation buttons sound when pressing it"})
        @Config.RequiresMcRestart
        @Config.Name("Enable GUI Button Sound")
        public boolean enableButtonSound = true;

        @Config.Comment({"Displays: Network Name, Live Transfer Rate & Internal Buffer"})
        @Config.RequiresMcRestart
        @Config.Name("Enable Basic One Probe Info")
        public boolean enableOneProbeBasicInfo = true;

        @Config.Comment({"Displays: Transfer Limit & Priority etc"})
        @Config.RequiresMcRestart
        @Config.Name("Enable Advanced One Probe Info")
        public boolean enableOneProbeAdvancedInfo = true;

        @Config.Comment({"Displays Advanced Info when sneaking only"})
        @Config.RequiresMcRestart
        @Config.Name("Enable sneaking to display Advanced One Probe Info")
        public boolean enableOneProbeSneaking = true;
    }

    public static class BlackList {

        @Config.Comment({"a blacklist for blocks which flux connections shouldn't connect to, use format 'modid:name@meta'"})
        @Config.RequiresMcRestart
        @Config.Name("Block Connection Blacklist")
        public String[] blockBlacklistStrings = new String[]{"actuallyadditions:block_phantom_energyface"};

        @Config.Comment({"a blacklist for items which the Flux Controller shouldn't transfer to, use format 'modid:name@meta'"})
        @Config.RequiresMcRestart
        @Config.Name("Item Transfer Blacklist")
        public String[] itemBlackListStrings = new String[]{};
    }

    public static class Mixin {

        @Config.Comment({
                "Allows TheOneProbe to show that Mekanism's machines exceed 2147483647 units of energy.",
                "MEKCEu already includes this feature, so installing MEKCEu will automatically disable it."
        })
        @Config.RequiresMcRestart
        @Config.Name("TOPSupport")
        public boolean topSupport = true;


        @Config.Comment({
                "Allows Mekanism's machines to transmit more than 2147483647 units of energy through FluxNetworks.",
                "MEKCEu already includes this feature, so installing MEKCEu will automatically disable it."
        })
        @Config.Name("FluxNetworksSupport")
        public boolean fluxNetworksSupport = true;
    }

/*        public static void read() {
        defaultLimit = config.getInt("Default Transfer Limit", ENERGY, 800000, 0, Integer.MAX_VALUE, "The default transfer limit of a flux connector");

        basicCapacity = config.getInt("Basic Storage Capacity", ENERGY, 1000000, 0, Integer.MAX_VALUE, "");
        basicTransfer = config.getInt("Basic Storage Transfer", ENERGY, 20000, 0, Integer.MAX_VALUE, "");
        herculeanCapacity = config.getInt("Herculean Storage Capacity", ENERGY, 8000000, 0, Integer.MAX_VALUE, "");
        herculeanTransfer = config.getInt("Herculean Storage Transfer", ENERGY, 120000, 0, Integer.MAX_VALUE, "");
        gargantuanCapacity = config.getInt("Gargantuan Storage Capacity", ENERGY, 128000000, 0, Integer.MAX_VALUE, "");
        gargantuanTransfer = config.getInt("Gargantuan Storage Transfer", ENERGY, 1440000, 0, Integer.MAX_VALUE, "");

        maximumPerPlayer = config.getInt("Maximum Networks Per Player", NETWORKS, 3, -1, Integer.MAX_VALUE, "Maximum networks each player can have. -1 = no limit");
        enableSuperAdmin = config.getBoolean("Allow Network Super Admin", NETWORKS, true, "Allows someone to be a network super admin, otherwise, no one can access or dismantle your flux devices or delete your networks without permission");
        superAdminRequiredPermission = config.getInt("Permission level required to activate Super Admin", NETWORKS, 1, 0, Integer.MAX_VALUE, "See ops.json. If the player has permission level equal or greater to the value set here they will be able to Activate Super Admin. Setting this to 0 will allow anyone to active Super Admin.");

        enableFluxRecipe = config.getBoolean("Enable Flux Recipe", GENERAL, true, "Enables redstones being compressed with the bedrock and obsidian to get flux");
        enableOldRecipe = config.getBoolean("Enable Old Recipe", GENERAL, false, "Enables redstone being turned into Flux when dropped in fire. (Need \"Enable Flux Recipe\" = true, so the default recipe can't be disabled if turns this on)");
        enableChunkLoading = config.getBoolean("Allow Flux Chunk Loading", GENERAL, true, "Allows flux tiles to work as chunk loaders");

        enableButtonSound = config.getBoolean("Enable GUI Button Sound", CLIENT, true, "Enable navigation buttons sound when pressing it");
        enableOneProbeBasicInfo = config.getBoolean("Enable Basic One Probe Info", CLIENT, true, "Displays: Network Name, Live Transfer Rate & Internal Buffer");
        enableOneProbeAdvancedInfo = config.getBoolean("Enable Advanced One Probe Info", CLIENT, true, "Displays: Transfer Limit & Priority etc");
        enableOneProbeSneaking = config.getBoolean("Enable sneaking to display Advanced One Probe Info", CLIENT, true, "Displays Advanced Info when sneaking only");
        fluxNetworksSupport = config.getBoolean("Enable sneaking to display Advanced One Probe Info", CLIENT, true, "Displays Advanced Info when sneaking only");

        blockBlacklistStrings = getBlackList("Block Connection Blacklist", BLACKLIST, new String[]{"actuallyadditions:block_phantom_energyface"}, "a blacklist for blocks which flux connections shouldn't connect to, use format 'modid:name@meta'");
        itemBlackListStrings = getBlackList("Item Transfer Blacklist", BLACKLIST, new String[]{}, "a blacklist for items which the Flux Controller shouldn't transfer to, use format 'modid:name@meta'");
    }*/

/*    public static final String ENERGY = "energy";
    public static int defaultLimit, basicCapacity, basicTransfer, herculeanCapacity, herculeanTransfer, gargantuanCapacity, gargantuanTransfer;

    public static final String NETWORKS = "networks";

    public static final String GENERAL = "general";
    public static final String CLIENT = "client";

    public static Configuration config;
    public static final String BLACKLIST = "blacklists";

    public static boolean enableButtonSound, enableOneProbeBasicInfo, enableOneProbeAdvancedInfo, enableOneProbeSneaking,fluxNetworksSupport;
    public static boolean enableFluxRecipe, enableOldRecipe, enableChunkLoading, enableSuperAdmin;

    public static int maximumPerPlayer, superAdminRequiredPermission;
    public static String[] blockBlacklistStrings, itemBlackListStrings;*/

    public static void init(File file) {
        //config = new Configuration(new File(file.getPath(), "flux_networks.cfg")); // 创建配置文件对象
        //config.load();        // 加载配置文件
        //read();               // 读取配置值到内存
        verifyAndReadBlacklist(); // 验证并加载黑名单数据
        //config.save();        // 保存配置（确保默认值写入文件）
        generateFluxChunkConfig(); // 生成区块加载配置
    }

    public static void verifyAndReadBlacklist() {
        // 清空方块黑名单缓存
        TileEntityHandler.blockBlacklist.clear();

        // 处理每个方块黑名单条目（格式：modid:name@meta）
        for (String str : blacklist.blockBlacklistStrings) {
            // 格式基础验证
            if (!str.contains(":")) {
                FluxNetworks.logger.error("BLACKLIST ERROR: " + str + " has incorrect formatting, please use 'modid:name@meta'");
            }

            // 解析条目
            String root = str;
            int meta = -1; // -1 表示匹配所有元数据

            // 处理带元数据的条目（如"mod:block@123"）
            if (str.contains("@")) {
                String[] split = str.split("@");
                root = split[0];
                try {
                    meta = Integer.parseInt(split[1]); // 转换元数据为数字
                    TileEntityHandler.blockBlacklist.put(root, meta);
                } catch (Exception e) {
                    FluxNetworks.logger.error("BLACKLIST ERROR: " + str + " has incorrect formatting, meta must be positive integer'");
                }
            } else {
                TileEntityHandler.blockBlacklist.put(root, meta);
            }
        }

        // 清空物品黑名单缓存
        ItemEnergyHandler.itemBlackList.clear();

        // 对物品黑名单重复相同的处理流程
        for (String str : blacklist.itemBlackListStrings) {
            if (!str.contains(":")) {
                FluxNetworks.logger.error("BLACKLIST ERROR: " + str + " has incorrect formatting, please use 'modid:name@meta'");
            }
            String root = str;
            int meta = -1;
            if (str.contains("@")) {
                String[] split = str.split("@");
                root = split[0];
                try {
                    meta = Integer.parseInt(split[1]);
                    ItemEnergyHandler.itemBlackList.put(root, meta);
                } catch (Exception e) {
                    FluxNetworks.logger.error("BLACKLIST ERROR: " + str + " has incorrect formatting, meta must be positive integer'");
                }
            } else {
                ItemEnergyHandler.itemBlackList.put(root, meta);
            }
        }
    }

    public static void generateFluxChunkConfig() {
        // 检查是否已存在本模组的区块加载配置
        if (!ForgeChunkManager.getConfig().hasCategory(Tags.MOD_ID)) {
            // 设置单个ticket最大加载区块数（默认100万，最小值0）
            ForgeChunkManager.getConfig().get(Tags.MOD_ID, "maximumChunksPerTicket", 1000000).setMinValue(0);

            // 设置最大ticket数量（默认100万，最小值0）
            ForgeChunkManager.getConfig().get(Tags.MOD_ID, "maximumTicketCount", 1000000).setMinValue(0);

            // 保存配置到文件
            ForgeChunkManager.getConfig().save();
        }
    }

/*    public static String[] getBlackList(String name, String category, String[] defaultValue, String comment) {
        // 获取或创建配置属性（分类，名称，默认值）
        Property prop = config.get(category, name, defaultValue);

        // 设置本地化键（用于语言文件）
        prop.setLanguageKey(name);

        // 允许任意输入值（不限制有效值范围）
        prop.setValidValues(null);

        // 添加配置项注释说明
        prop.setComment(comment);

        // 返回字符串数组类型的配置值
        return prop.getStringList();
    }*/

    /*
    必须在最后加载。
    */
    static {
        ConfigAnytime.register(FluxConfig.class);
    }
}
