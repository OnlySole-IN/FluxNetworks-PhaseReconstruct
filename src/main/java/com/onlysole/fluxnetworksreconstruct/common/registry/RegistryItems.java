package com.onlysole.fluxnetworksreconstruct.common.registry;

import com.onlysole.fluxnetworksreconstruct.common.item.ItemAdminConfigurator;
import com.onlysole.fluxnetworksreconstruct.common.item.ItemConfigurator;
import com.onlysole.fluxnetworksreconstruct.common.item.ItemCore;
import com.onlysole.fluxnetworksreconstruct.common.item.ItemFlux;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class RegistryItems {

    public static final List<Item> ITEMS = new ArrayList<Item>();

    public static final Item FLUX = new ItemFlux();
    public static final Item FLUX_CORE = new ItemCore("FluxCore");
    public static final Item FLUX_CONFIGURATOR = new ItemConfigurator();
    public static final Item ADMIN_CONFIGURATOR = new ItemAdminConfigurator();

}
