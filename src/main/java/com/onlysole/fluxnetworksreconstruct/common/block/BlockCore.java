package com.onlysole.fluxnetworksreconstruct.common.block;

import com.onlysole.fluxnetworksreconstruct.FluxNetworks;
import com.onlysole.fluxnetworksreconstruct.common.CommonProxy;
import com.onlysole.fluxnetworksreconstruct.common.item.ItemFluxConnector;
import com.onlysole.fluxnetworksreconstruct.common.registry.RegistryBlocks;
import com.onlysole.fluxnetworksreconstruct.common.registry.RegistryItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockCore extends Block {

    public BlockCore(String name, Material materialIn) {
        super(materialIn);
        setTranslationKey(FluxNetworks.MODID + "." + name.toLowerCase());
        setRegistryName(name.toLowerCase());
        RegistryBlocks.BLOCKS.add(this);
        RegistryItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
        setCreativeTab(CommonProxy.creativeTabs);
    }

    public BlockCore(String name, Material materialIn, boolean special) {
        super(materialIn);
        setTranslationKey(FluxNetworks.MODID + "." + name.toLowerCase());
        setRegistryName(name.toLowerCase());
        RegistryBlocks.BLOCKS.add(this);
        RegistryItems.ITEMS.add(new ItemFluxConnector(this).setRegistryName(this.getRegistryName()));
        setCreativeTab(CommonProxy.creativeTabs);
    }

    public void registerModels() {

        FluxNetworks.proxy.registerItemModel(Item.getItemFromBlock(this), 0, "inventory");
    }
}
