package com.rangetuur.emagnet;

import com.rangetuur.emagnet.blocks.tileentities.renderer.MagnetJarRenderer;
import com.rangetuur.emagnet.registry.ModBlockEntityTypes;
import com.rangetuur.emagnet.registry.ModBlocks;
import com.rangetuur.emagnet.registry.ModItems;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EMagnet.MOD_ID)
public class EMagnet
{
    public static final String MOD_ID ="emagnet";
    private static final Logger LOGGER = LogManager.getLogger();

    public EMagnet() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        ModBlocks.registerBlocks();
        ModItems.registerItems();
        ModBlockEntityTypes.registerBlockEntityTypes();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(ModBlocks.MAGNET_JAR.get(), RenderType.translucent());
        ClientRegistry.bindTileEntityRenderer(ModBlockEntityTypes.MAGNET_JAR.get(), MagnetJarRenderer::new);
    }
}
