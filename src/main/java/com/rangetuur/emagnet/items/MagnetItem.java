package com.rangetuur.emagnet.items;

import com.rangetuur.emagnet.EMagnetConfig.ServerConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;
import java.util.Objects;

public class MagnetItem extends EnergyItem{

    private final int range;
    private boolean magnet_always_works;

    public MagnetItem(Properties properties, int range, int maxEnergy) {
        super(properties, maxEnergy);
        this.range = range;
        this.magnet_always_works = ServerConfig.magnet_always_works.get();
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY).orElse(null);
        if(energyStorage!=null && energyStorage.getEnergyStored()>energyStorage.getMaxEnergyStored()) {
            energyStorage.extractEnergy((energyStorage.getEnergyStored()-energyStorage.getMaxEnergyStored()), false);
        }

        ItemStack mainHandStack = ((PlayerEntity) entity).getMainHandItem();
        ItemStack offHandStack = ((PlayerEntity) entity).getOffhandItem();
        if (magnet_always_works) {
            attractItemsToPlayer(world, entity, stack);
        }
        else {
            if (mainHandStack.getItem() instanceof MagnetItem) {
                attractItemsToPlayer(world, entity, mainHandStack);
            }
            if (offHandStack.getItem() instanceof MagnetItem) {
                attractItemsToPlayer(world, entity, offHandStack);
            }
        }
    }

    private void attractItemsToPlayer(World world, Entity entity, ItemStack stack) {

        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        List<ItemEntity> items = world.getEntities(EntityType.ITEM, AxisAlignedBB.of(new MutableBoundingBox((int) x-1-range,(int) y+1-range,(int) z-range,(int) x-1+range,(int) y+1+range,(int) z+range)), Objects::nonNull);

        for (ItemEntity item : items) {
            int energyForItem = item.getItem().getCount();

            IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY).orElse(null);
            if(energyStorage!=null) {
                if(energyStorage.getEnergyStored()>=energyForItem) {
                    item.setNoPickUpDelay();

                    Vector3d itemVector = new Vector3d(item.getX(), item.getY(), item.getZ());
                    Vector3d playerVector = new Vector3d(x, y+0.75, z);
                    item.move(null, playerVector.subtract(itemVector).scale(0.5));

                    energyStorage.extractEnergy(energyForItem, false);
                }
            }
        }
    }

    public int getRange() {
        return range;
    }
}
