package onlysole.fluxnetworks.api.network;

import onlysole.fluxnetworks.api.translate.FluxTranslate;
import onlysole.fluxnetworks.api.translate.Translation;
import net.minecraft.util.text.TextFormatting;

public enum AccessLevel {
    OWNER(FluxTranslate.OWNER, 0xFFAA00, TextFormatting.GOLD),
    ADMIN(FluxTranslate.ADMIN, 0x66CC00, TextFormatting.GREEN),
    USER(FluxTranslate.USER, 0x6699FF, TextFormatting.BLUE),
    NONE(FluxTranslate.BLOCKED, 0xA9A9A9, TextFormatting.GRAY),
    SUPER_ADMIN(FluxTranslate.SUPER_ADMIN, 0x4B0082, TextFormatting.DARK_PURPLE);

    public Translation localization;
    public int color;
    public TextFormatting formatting;

    AccessLevel(Translation localization, int color, TextFormatting formatting) {
        this.localization = localization;
        this.color = color;
        this.formatting = formatting;
    }

    public String getName() {
        return formatting + localization.t();
    }

    public int getColor() {
        return color;
    }

    public boolean canAccess() {
        return this != NONE;
    }

    public boolean canEdit() {
        return canAccess() && this != USER;
    }

    public boolean canDelete() {
        return this == OWNER || this == SUPER_ADMIN;
    }

}
