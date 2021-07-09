package com.rangetuur.emagnet.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;

public class MagnetItem extends EnergyItem{

    private final int range;
    private final int maxEnergy;
    private int energy;

    public MagnetItem(Properties properties, int range, int maxEnergy) {
        super(properties, maxEnergy);
        this.range = range;
        this.maxEnergy = maxEnergy;
        this.energy = 0;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        ItemStack mainHandStack = ((PlayerEntity) entity).getMainHandItem();
        ItemStack offHandStack = ((PlayerEntity) entity).getOffhandItem();
        if (mainHandStack.getItem() instanceof MagnetItem) {
            attractItemsToPlayer(world, entity, mainHandStack);
        }
        if (offHandStack.getItem() instanceof MagnetItem) {
            attractItemsToPlayer(world, entity, offHandStack);
        }
    }

    private void attractItemsToPlayer(World world, Entity entity, ItemStack stack) {

        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        List<Entity> items = world.getEntities(entity, AxisAlignedBB.of(new MutableBoundingBox((int) x-range,(int) y-range,(int) z-range,(int) x+range,(int) y+range,(int) z+range)));

        for (Entity item : items) {
            if (item instanceof ItemEntity) {
                ItemEntity itemEntity = ((ItemEntity) item);
                itemEntity.setNoPickUpDelay();

                Vector3d itemVector = new Vector3d(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ());
                Vector3d playerVector = new Vector3d(x, y+0.75, z);
                itemEntity.move(null, playerVector.subtract(itemVector).scale(0.5));
            }
        }
    }

    public int getRange() {
        return range;
    }
}
