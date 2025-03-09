package com.onlysole.fluxnetworksreconstruct.api.tiles;

import net.minecraft.nbt.NBTTagCompound;

public interface IFluxConfigurable {

    NBTTagCompound copyConfiguration(NBTTagCompound config);

    void pasteConfiguration(NBTTagCompound config);
}
