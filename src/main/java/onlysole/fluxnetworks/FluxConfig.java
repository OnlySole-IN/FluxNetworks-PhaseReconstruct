package onlysole.fluxnetworks;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import onlysole.fluxnetworks.common.handler.ItemEnergyHandler;
import onlysole.fluxnetworks.common.handler.TileEntityHandler;
import net.minecraftforge.common.ForgeChunkManager;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
@Config(modid = Tags.MOD_ID, name = Tags.MOD_NAME)
public class FluxConfig {

    //能源存储
    @Config.Name("EnergyStorage")
    public static final EnergyStorage energystorage = new EnergyStorage();

    //能源传输
    @Config.Name("EnergyTransmit")
    public static final EnergyTransmit energytransmit = new EnergyTransmit();

    //网络设置与权限
    @Config.Name("Networks")
    public static final Networks networks = new Networks();

    //基础功能
    @Config.Name("General")
    public static final General general = new General();

    //客户端与界面
    @Config.Name("Client")
    public static final Client client = new Client();

    //黑名单
    @Config.Name("BlackList")
    public static final BlackList blacklist = new BlackList();

    //模组联动-强修改
    @Config.Name("Mixin")
    public static final Mixin mixin = new Mixin();

    public static class EnergyStorage {

        @Config.Comment({"The default capacity limit of a basic flux storage"})
        @Config.RangeDouble(min = 0, max = Long.MAX_VALUE)
        @Config.Name("Basic Storage Capacity")
        public double basicCapacity = 1000000;

        @Config.Comment({"The default transfer limit of a basic flux storage"})
        @Config.RangeDouble(min = 0, max = Long.MAX_VALUE)
        @Config.Name("Basic Storage Transfer")
        public double basicTransfer = 20000;

        @Config.Comment({"The default capacity limit of a herculean flux storage"})
        @Config.RangeDouble(min = 0, max = Long.MAX_VALUE)
        @Config.Name("Herculean Storage Capacity")
        public double herculeanCapacity = 8000000;

        @Config.Comment({"The default transfer limit of a herculean flux storage"})
        @Config.RangeDouble(min = 0, max = Long.MAX_VALUE)
        @Config.Name("Herculean Storage Transfer")
        public double herculeanTransfer = 120000;

        @Config.Comment({"The default capacity limit of a gargantuan flux storage"})
        @Config.RangeDouble(min = 0, max = Long.MAX_VALUE)
        @Config.Name("Gargantuan Storage Capacity")
        public double gargantuanCapacity = 128000000;

        @Config.Comment({"The default transfer limit of a gargantuan flux storage"})
        @Config.RangeDouble(min = 0, max = Long.MAX_VALUE)
        @Config.Name("Gargantuan Storage Transfer")
        public double gargantuanTransfer = 1440000;


    }

    public static class EnergyTransmit {

        @Config.Comment({"The default transfer limit of a flux connector"})
        @Config.RangeDouble(min = 0, max = Long.MAX_VALUE)
        @Config.Name("Default Transfer Limit")
        public double defaultLimit = 800000;
    }

    public static class Networks {

        @Config.Comment({"Maximum networks each player can have. -1 = no limit"})
        @Config.RangeDouble(min = -1, max = Integer.MAX_VALUE)
        @Config.Name("Maximum Networks Per Player")
        public int maximumPerPlayer = 3;

        @Config.Comment({"Allows someone to be a network super admin, otherwise, " +
                "no one can access or dismantle your flux devices or delete your networks without permission"})
        @Config.Name("Allow Network Super Admin")
        public boolean enableSuperAdmin = true;

        @Config.Comment({
                "See ops.json. If the player has permission level equal or greater to the value set here they will be able to Activate Super Admin.",
                "Setting this to 0 will allow anyone to active Super Admin."
        })
        @Config.RangeDouble(min = 0, max = Integer.MAX_VALUE)
        @Config.Name("Permission level required to activate Super Admin")
        public int superAdminRequiredPermission = 1;
    }

    public static class General {

        @Config.Comment({"Enables redstones being compressed with the bedrock and obsidian to get flux"})
        @Config.Name("Enable Flux Recipe")
        public boolean enableFluxRecipe = true;

        @Config.Comment({
                "Enables redstone being turned into Flux when dropped in fire. ",
                "(Need \"Enable Flux Recipe\" = true, so the default recipe can't be disabled if turns this on)"
        })
        @Config.Name("Enable Old Recipe")
        public boolean enableOldRecipe = false;

        @Config.Comment({"Allows flux tiles to work as chunk loaders"})
        @Config.Name("Allow Flux Chunk Loading")
        public boolean enableChunkLoading = true;

        @Config.Comment("(Server Performance | Experimental) Rewriting the flux network calculation logic to improve performance using multithreading.")
        @Config.Name("Parallel Network Calculation")
        public boolean parallelNetworkCalculation = false;

        @Config.Comment("(Server Performance) Removing the secondary judgement of energy transfer may help improve performance.")
        @Config.Name("Connection Transfer Improvements")
        public boolean connectionTransfer = true;

        @Config.Comment("Possible fix for duplicate users or even crashes on player networks in some cases.")
        @Config.Name("Synchronize Fixes")
        public boolean synchronize = true;

        @Config.Comment("(Server) Make FluxNetworks to generate a random int uid for each network, instead of using the self-incrementing ID.")
        @Config.Name("Random Network Unique ID")
        public boolean randomNetworkUniqueID = false;
    }

    public static class Client {

        @Config.Comment({"Enable navigation buttons sound when pressing it"})
        @Config.Name("Enable GUI Button Sound")
        public boolean enableButtonSound = true;

        @Config.Comment({"Displays: Network Name, Live Transfer Rate & Internal Buffer"})
        @Config.Name("Enable Basic One Probe Info")
        public boolean enableOneProbeBasicInfo = true;

        @Config.Comment({"Displays: Transfer Limit & Priority etc"})
        @Config.Name("Enable Advanced One Probe Info")
        public boolean enableOneProbeAdvancedInfo = true;

        @Config.Comment({"Displays Advanced Info when sneaking only"})
        @Config.Name("Enable sneaking to display Advanced One Probe Info")
        public boolean enableOneProbeSneaking = true;

    }

    public static class BlackList {

        @Config.Comment({"a blacklist for blocks which flux connections shouldn't connect to, use format 'modid:name@meta'"})
        @Config.Name("Block Connection Blacklist")
        public String[] blockBlacklistStrings = new String[]{"actuallyadditions:block_phantom_energyface"};

        @Config.Comment({"a blacklist for items which the Flux Controller shouldn't transfer to, use format 'modid:name@meta'"})
        @Config.Name("Item Transfer Blacklist")
        public String[] itemBlackListStrings = new String[]{};
    }

    public static class Mixin {

        @Config.Comment({
                "Allows Mekanism's machines to transmit more than 2147483647 units of energy through FluxNetworks.",
                "MEKCEu already includes this feature, so installing MEKCEu will automatically disable it."
        })
        @Config.Name("Flux Networks Support")
        public boolean fluxNetworksSupport = true;
    }

    public static void init() {
        verifyAndReadBlacklist(); // 验证并加载黑名单数据
        generateFluxChunkConfig(); // 生成区块加载配置
    }

    public static void verifyAndReadBlacklist() {
        TileEntityHandler.blockBlacklist.clear();

        for (String str : blacklist.blockBlacklistStrings) {
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
                    TileEntityHandler.blockBlacklist.put(root, meta);
                } catch (Exception e) {
                    FluxNetworks.logger.error("BLACKLIST ERROR: " + str + " has incorrect formatting, meta must be positive integer'");
                }
            } else {
                TileEntityHandler.blockBlacklist.put(root, meta);
            }
        }

        ItemEnergyHandler.itemBlackList.clear();

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

/*    public static Configuration config;
    public static final String BLACKLIST = "blacklists";
    public static String[] blockBlacklistStrings, itemBlackListStrings;

    public static void read() {
        blockBlacklistStrings = getBlackList("Block Connection Blacklist", BLACKLIST, new String[]{"actuallyadditions:block_phantom_energyface"},
        "a blacklist for blocks which flux connections shouldn't connect to, use format 'modid:name@meta'");
        itemBlackListStrings = getBlackList("Item Transfer Blacklist", BLACKLIST, new String[]{},
        "a blacklist for items which the Flux Controller shouldn't transfer to, use format 'modid:name@meta'");
    }

    public static String[] getBlackList(String name, String category, String[] defaultValue, String comment) {
        Property prop = config.get(category, name, defaultValue);

        prop.setLanguageKey(name);

        prop.setValidValues(null);

        prop.setComment(comment);

        return prop.getStringList();
    }*/
}
