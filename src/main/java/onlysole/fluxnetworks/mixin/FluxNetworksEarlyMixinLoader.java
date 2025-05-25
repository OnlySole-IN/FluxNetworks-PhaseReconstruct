package onlysole.fluxnetworks.mixin;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import onlysole.fluxnetworks.FluxNetworks;
import onlysole.fluxnetworks.common.util.FluxEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
public class FluxNetworksEarlyMixinLoader implements IFMLLoadingPlugin {
    private static final Map<String, BooleanSupplier> MIXIN_CONFIGS = new LinkedHashMap<>();

    static {
        FluxEnvironment.init();
    }

    private static void addMixinCFG(final String mixinConfig) {
        MIXIN_CONFIGS.put(mixinConfig, () -> true);
    }

    private static void addMixinCFG(final String mixinConfig, final BooleanSupplier conditions) {
        MIXIN_CONFIGS.put(mixinConfig, conditions);
    }

    public static boolean isCleanroomLoader() {
        try {
            Class.forName("com.cleanroommc.boot.Main");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    // Noop

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(final Map<String, Object> data) {
        MIXIN_CONFIGS.forEach((config, supplier) -> {
            if (supplier == null) {
                FluxNetworks.logger.warn("[ FluxNetworks-MixinLoader] Mixin config {} is not found in config map! It will never be loaded.", config);
                return;
            }
            boolean shouldLoad = supplier.getAsBoolean();
            if (!shouldLoad) {
                FluxNetworks.logger.info("[ FluxNetworks-MixinLoader] Mixin config {} is disabled by config or mod is not loaded.", config);
                return;
            }
            FluxNetworks.logger.info("[ FluxNetworks-MixinLoader] Adding {} to mixin configuration.", config);
            Mixins.addConfiguration(config);
        });
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
