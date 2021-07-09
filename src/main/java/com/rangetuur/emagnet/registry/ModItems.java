package com.rangetuur.emagnet.registry;

import com.rangetuur.emagnet.EMagnet;
import com.rangetuur.emagnet.items.MagnetItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EMagnet.MOD_ID);

    //Items
    public static final RegistryObject<Item> BASIC_MAGNET = ITEMS.register("basic_magnet", () -> new MagnetItem(new Item.Properties().tab(ItemGroup.TAB_TOOLS),6,10000));
    public static final RegistryObject<Item> ADVANCED_MAGNET = ITEMS.register("advanced_magnet", () -> new MagnetItem(new Item.Properties().tab(ItemGroup.TAB_TOOLS),8,30000));

    //Block Items
    public static final RegistryObject<BlockItem> MAGNET_JAR = ITEMS.register("magnet_jar", () -> new BlockItem(ModBlocks.MAGNET_JAR.get(), new Item.Properties().tab(ItemGroup.TAB_TRANSPORTATION)));

    public static void registerItems() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
