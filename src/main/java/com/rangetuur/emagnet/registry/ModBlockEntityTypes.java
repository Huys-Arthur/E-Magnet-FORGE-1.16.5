package com.rangetuur.emagnet.registry;

import com.rangetuur.emagnet.EMagnet;
import com.rangetuur.emagnet.blocks.tileentities.MagnetJarTileEntity;
import net.java.games.input.Component;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlockEntityTypes {

    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, EMagnet.MOD_ID);

    public static final RegistryObject<TileEntityType<MagnetJarTileEntity>> MAGNET_JAR = TILE_ENTITY_TYPES.register("magnet_jar", () -> TileEntityType.Builder.of(MagnetJarTileEntity::new, ModBlocks.MAGNET_JAR.get()).build(null));

    public static void registerBlockEntityTypes() {
        TILE_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
