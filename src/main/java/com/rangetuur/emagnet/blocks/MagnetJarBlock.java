package com.rangetuur.emagnet.blocks;

import com.rangetuur.emagnet.blocks.tileentities.MagnetJarTileEntity;
import com.rangetuur.emagnet.registry.ModBlockEntityTypes;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class MagnetJarBlock extends Block{

    private static final VoxelShape SHAPE = Stream.of(
            Block.box(2, 0, 2, 14, 2, 14),
            Block.box(2, 2, 2, 14, 12, 14),
            Block.box(3, 12, 3, 13, 14, 13),
            Block.box(5, 14, 5, 11, 16, 11))
            .reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();

    public MagnetJarBlock() {
        super(AbstractBlock.Properties.of(Material.GLASS)
                .harvestLevel(3)
                .strength(50, 500)
                .harvestTool(ToolType.PICKAXE)
                .sound(SoundType.GLASS)
                .lightLevel((light) -> 10));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModBlockEntityTypes.MAGNET_JAR.get().create();
    }

    @Override
    public boolean hasDynamicShape() {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        MagnetJarTileEntity tileEntity = (MagnetJarTileEntity) world.getBlockEntity(pos);
        ItemStack handStack = player.getItemInHand(hand);
        if (handStack.isEmpty()){
            ItemStack stack = tileEntity.extractItem(1, 1, false);
            if (stack != null) {
                player.setItemInHand(hand, stack);
                tileEntity.setChanged();
            }
        }
        else {
            IEnergyStorage energyStorage = handStack.getCapability(CapabilityEnergy.ENERGY).orElse(null);
            if(energyStorage!=null){
                if (tileEntity.isItemValid(1, handStack)) {
                    tileEntity.insertItem(1, handStack, false);
                    player.setItemInHand(hand, ItemStack.EMPTY);
                    tileEntity.setChanged();
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onRemove(BlockState p_196243_1_, World world, BlockPos pos, BlockState p_196243_4_, boolean p_196243_5_) {
        if (!p_196243_1_.is(p_196243_4_.getBlock())) {
            TileEntity tileentity = world.getBlockEntity(pos);
            if (tileentity instanceof MagnetJarTileEntity) {
                ((MagnetJarTileEntity) tileentity).dropContent();
            }

            super.onRemove(p_196243_1_, world, pos, p_196243_4_, p_196243_5_);
        }
    }
}