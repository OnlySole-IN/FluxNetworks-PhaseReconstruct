package onlysole.fluxnetworks.mixin;

import net.minecraftforge.fml.common.Loader;
import onlysole.fluxnetworks.FluxNetworks;
import onlysole.fluxnetworks.common.config.FluxConfig;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.*;
import java.util.function.BooleanSupplier;

@SuppressWarnings({"unused", "SameParameterValue"})
public class FluxNetworksLateMixinLoader implements ILateMixinLoader {

    private static final Map<String, BooleanSupplier> MIXIN_CONFIGS = new LinkedHashMap<>();

    static {
        addModdedMixinCFG("mixins.fluxnetworks_mek.json",         "mekanism");
    }

    @Override
    public List<String> getMixinConfigs() {
        return new ArrayList<>(MIXIN_CONFIGS.keySet());
    }

    @Override
    public boolean shouldMixinConfigQueue(final String mixinConfig) {
        BooleanSupplier supplier = MIXIN_CONFIGS.get(mixinConfig);
        if (supplier == null) {
            FluxNetworks.logger.warn("[StellarCore-MixinLoader] Mixin config {} is not found in config map! It will never be loaded.", mixinConfig);
            return false;
        }
        boolean shouldLoad = supplier.getAsBoolean();
        if (!shouldLoad) {
            FluxNetworks.logger.info("[StellarCore-MixinLoader] Mixin config {} is disabled by config or mod is not loaded.", mixinConfig);
        }
        return shouldLoad;
    }

    private static boolean modLoaded(final String modID) {
        return Loader.isModLoaded(modID);
    }

    private static void addModdedMixinCFG(final String mixinConfig, final String modID) {
        MIXIN_CONFIGS.put(mixinConfig, () -> modLoaded(modID));
    }

    private static void addModdedMixinCFG(final String mixinConfig, final String modID, final BooleanSupplier condition) {
        MIXIN_CONFIGS.put(mixinConfig, () -> modLoaded(modID) && condition.getAsBoolean());
    }

    private static void addModdedMixinCFG(final String mixinConfig, final String[] modIDs, final BooleanSupplier condition) {
        MIXIN_CONFIGS.put(mixinConfig, () -> Arrays.stream(modIDs).allMatch(Loader::isModLoaded) && condition.getAsBoolean());
    }

    private static void addModdedMixinCFG(final String mixinConfig, final String modID, final String... modIDs) {
        MIXIN_CONFIGS.put(mixinConfig, () -> modLoaded(modID) && Arrays.stream(modIDs).allMatch(Loader::isModLoaded));
    }

    private static void addMixinCFG(final String mixinConfig) {
        MIXIN_CONFIGS.put(mixinConfig, () -> true);
    }

    private static void addMixinCFG(final String mixinConfig, final BooleanSupplier conditions) {
        MIXIN_CONFIGS.put(mixinConfig, conditions);
    }
}
