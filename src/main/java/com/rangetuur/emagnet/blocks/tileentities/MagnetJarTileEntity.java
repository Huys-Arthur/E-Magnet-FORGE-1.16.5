package com.rangetuur.emagnet.blocks.tileentities;

import com.rangetuur.emagnet.EMagnetConfig.ServerConfig;
import com.rangetuur.emagnet.items.MagnetItem;
import com.rangetuur.emagnet.registry.ModBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class MagnetJarTileEntity extends TileEntity implements ITickableTileEntity, IItemHandler, ICapabilityProvider {

    private ItemStack content = ItemStack.EMPTY;

    public MagnetJarTileEntity() {
        super(ModBlockEntityTypes.MAGNET_JAR.get());
    }


    @Override
    public void tick() {
        if(content!=ItemStack.EMPTY){
            IEnergyStorage energyStorage = content.getCapability(CapabilityEnergy.ENERGY).orElse(null);
            if(energyStorage!=null) {
                if(energyStorage.getEnergyStored()!=energyStorage.getMaxEnergyStored()) {
                    if (!level.getEntities(EntityType.LIGHTNING_BOLT, new AxisAlignedBB(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), worldPosition.getX() + 1, worldPosition.getY() + 3, worldPosition.getZ() + 1), Objects::nonNull).isEmpty()) {
                        energyStorage.receiveEnergy(energyStorage.getMaxEnergyStored(), false);
                        setChanged();
                    }
                }
                if (content.getItem() instanceof MagnetItem){
                    if (ServerConfig.disable_magnet_jar_with_redstone.get()) {
                        if(!getLevel().hasNeighborSignal(getBlockPos())){
                            attractItemsAroundBlock();
                        }
                    }
                    else {
                        attractItemsAroundBlock();
                    }
                }
            }
        }
    }

    private void attractItemsAroundBlock() {
        int range = ((MagnetItem) content.getItem()).getRange();
        BlockPos pos = getBlockPos();

        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        if(getLevel()!=null){
            List<ItemEntity> items = getLevel().getEntities(EntityType.ITEM, AxisAlignedBB.of(new MutableBoundingBox(((int) x)-range,((int) y)-range,((int) z)-range,((int) x)+range,((int) y)+range,((int) z)+range)), Objects::nonNull);
            List<ItemEntity> itemsInBlock = getLevel().getEntities(EntityType.ITEM, AxisAlignedBB.of(new MutableBoundingBox(pos.getX(),pos.getY(),pos.getZ(),pos.getX(),pos.getY(),pos.getZ())), Objects::nonNull);

            for (ItemEntity item : items) {
                if (!itemsInBlock.contains(item)){
                    int energyForItem = item.getItem().getCount();

                    IEnergyStorage energyStorage = content.getCapability(CapabilityEnergy.ENERGY).orElse(null);
                    if(energyStorage!=null) {
                        if(energyStorage.getEnergyStored()>=energyForItem) {

                            Vector3d itemVector = new Vector3d(item.getX(), item.getY(), item.getZ());
                            Vector3d playerVector = new Vector3d(x+0.5, y+0.5, z+0.5);
                            item.move(null, playerVector.subtract(itemVector).scale(0.5));

                            energyStorage.extractEnergy(energyForItem, false);
                            setChanged();
                        }
                    }
                }
            }
        }
    }

    public void dropContent(){
        InventoryHelper.dropItemStack(getLevel(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), content);
        setChanged();
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot==1){
            return content;
        }
        return null;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot==1 && !simulate && content.isEmpty()){
            content = stack;
            return content;
        }
        return null;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot==1 && amount==1 && !simulate && !content.isEmpty()){
            ItemStack stack = content;
            content = ItemStack.EMPTY;
            return stack;
        }
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        ItemStack stack = ItemStack.of (nbt.getCompound("content"));
        if (!stack.isEmpty()){
            content = stack;
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        if (!content.isEmpty()) {
            nbt.put("content", content.save(new CompoundNBT()));
        } else {
            nbt.put("content", ItemStack.EMPTY.save(new CompoundNBT()));
        }
        return super.save(nbt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        if (!content.isEmpty()) {
            nbt.put("content", content.save(new CompoundNBT()));
        } else {
            nbt.put("content", ItemStack.EMPTY.save(new CompoundNBT()));
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        super.handleUpdateTag(state, nbt);
        ItemStack stack = ItemStack.of (nbt.getCompound("content"));
        if (!stack.isEmpty()){
            content = stack;
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY && side == Direction.UP){
            IEnergyStorage energyStorage = content.getCapability(CapabilityEnergy.ENERGY).orElse(null);
            if(energyStorage!=null) {
                return LazyOptional.of(() -> energyStorage).cast();
            }
        }
        return super.getCapability(cap, side);
    }
}
