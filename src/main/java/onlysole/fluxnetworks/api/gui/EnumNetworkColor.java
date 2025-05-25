package onlysole.fluxnetworks.api.gui;

public enum EnumNetworkColor {
    flux1(0x295E8A), flux2(0x343477), flux3(0x582A72), flux4(0x882D60), flux5(0xAA3939), flux6(0xAA6F39),
    flux7(0xC6B900), flux8(0x609732), lightBlue(0x87CEfA), lilac(0x86608A), lightCoral(0xF08080), pink(0xFFC0CB),
    peach(0xffDAB9), flax(0xEEDC82);

    public int color;

    EnumNetworkColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
