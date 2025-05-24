package onlysole.fluxnetworks.common.util;

import onlysole.fluxnetworks.FluxNetworks;
import onlysole.fluxnetworks.mixin.FluxNetworksLateMixinLoader;

public class FluxEnvironment {

    private static int processors = 0;

    /**
     * 在 {@link FluxNetworksLateMixinLoader} 处静态加载处调用，主要防止线程亲和导致始终返回 1 的问题。
     */
    public static void init() {
        processors = Runtime.getRuntime().availableProcessors();
        FluxNetworks.logger.info("[FluxEnvironment] Processor count: {}", processors);
        if (!shouldParallel()) {
            FluxNetworks.logger.warn("[FluxEnvironment] Processor count is less than 3, parallel processing is disabled.");
        } else {
            FluxNetworks.logger.info("[FluxEnvironment] Parallel execution is enabled.");
        }
    }

    public static boolean shouldParallel() {
        return processors > 2;
    }

    public static int getConcurrency() {
        return shouldParallel() ? processors : 1;
    }

}
