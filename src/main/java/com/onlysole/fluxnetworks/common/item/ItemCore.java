package com.onlysole.fluxnetworks.common.item;

import com.onlysole.fluxnetworks.FluxNetworks;
import com.onlysole.fluxnetworks.Tags;
import com.onlysole.fluxnetworks.common.CommonProxy;
import com.onlysole.fluxnetworks.common.registry.RegistryItems;
import net.minecraft.item.Item;

public class ItemCore extends Item {

    public ItemCore(String name) {

        setTranslationKey(Tags.MOD_ID + "." + name.toLowerCase());
        setRegistryName(name.toLowerCase());
        setCreativeTab(CommonProxy.creativeTabs);
        RegistryItems.ITEMS.add(this);
    }

    public void registerModels() {

        FluxNetworks.proxy.registerItemModel(this, 0, "inventory");
    }
}
