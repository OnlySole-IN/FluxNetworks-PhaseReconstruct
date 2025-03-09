package com.onlysole.fluxnetworksreconstruct.api.network;

import com.onlysole.fluxnetworksreconstruct.api.translate.FluxTranslate;
import com.onlysole.fluxnetworksreconstruct.api.translate.Translation;

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
