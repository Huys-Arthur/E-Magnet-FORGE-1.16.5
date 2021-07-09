package com.rangetuur.emagnet.blocks.tileentities.renderer;

import com.rangetuur.emagnet.blocks.tileentities.MagnetJarTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class MagnetJarRenderer extends TileEntityRenderer<MagnetJarTileEntity> {

    public MagnetJarRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }


    @Override
    public void render(MagnetJarTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn.getStackInSlot(1).equals(ItemStack.EMPTY)) return;

        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5, 0.25, 0.5);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees((tileEntityIn.getLevel().getGameTime() + partialTicks) * 4));

        IBakedModel model = Minecraft.getInstance().getItemRenderer().getModel(tileEntityIn.getStackInSlot(1), null, null);

        Minecraft.getInstance().getItemRenderer().render(tileEntityIn.getStackInSlot(1), ItemCameraTransforms.TransformType.GROUND, true, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, model);

        matrixStackIn.popPose();

    }
}