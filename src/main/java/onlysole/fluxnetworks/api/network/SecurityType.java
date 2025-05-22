package onlysole.fluxnetworks.api.network;

import onlysole.fluxnetworks.api.translate.FluxTranslate;
import onlysole.fluxnetworks.api.translate.Translation;

public enum SecurityType {
    PUBLIC(FluxTranslate.PUBLIC),
    ENCRYPTED(FluxTranslate.ENCRYPTED);

    private Translation localization;

    SecurityType(Translation localization) {
        this.localization = localization;
    }

    public String getName() {
        return localization.t();
    }

    public boolean isEncrypted() {
        return this == ENCRYPTED;
    }
}
