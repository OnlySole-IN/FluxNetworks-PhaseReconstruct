package com.onlysole.fluxnetworksreconstruct.common.item;

import com.onlysole.fluxnetworksreconstruct.FluxNetworksReconstruct;
import com.onlysole.fluxnetworksreconstruct.common.CommonProxy;
import com.onlysole.fluxnetworksreconstruct.common.registry.RegistryItems;
import net.minecraft.item.Item;

public class ItemCore extends Item {

    public ItemCore(String name) {

        setTranslationKey(FluxNetworksReconstruct.MODID + "." + name.toLowerCase());
        setRegistryName(name.toLowerCase());
        setCreativeTab(CommonProxy.creativeTabs);
        RegistryItems.ITEMS.add(this);
    }

    public void registerModels() {

        FluxNetworksReconstruct.proxy.registerItemModel(this, 0, "inventory");
    }
}
