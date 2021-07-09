package com.rangetuur.emagnet.registry;

import com.rangetuur.emagnet.EMagnet;
import com.rangetuur.emagnet.EMagnetConfig.ServerConfig;
import com.rangetuur.emagnet.blocks.MagnetJarBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EMagnet.MOD_ID);

    public static final RegistryObject<Block> MAGNET_JAR = BLOCKS.register("magnet_jar", MagnetJarBlock::new);

    public static void registerBlocks() {
        if (!ServerConfig.disable_magnet_jar.get()) {
            BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }
    }
}
