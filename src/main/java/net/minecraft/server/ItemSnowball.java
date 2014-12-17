package net.minecraft.server;

import com.empireminecraft.customevents.SnowballThrowEvent; // EMC

public class ItemSnowball extends Item {

    public ItemSnowball() {
        this.maxStackSize = 16;
        this.a(CreativeModeTab.f);
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        // EMC start
        EntitySnowball entity = new EntitySnowball(world, entityhuman);
        if (!world.addEntity(entity)) {
            return itemstack;
        }
        SnowballThrowEvent event = new SnowballThrowEvent(entityhuman, itemstack, entity);
        if (!event.callEvent()) {
            entity.die();
            return itemstack;
        }
        if (event.shouldConsumeOnThrow()) {
        // EMC end
            --itemstack.count;
        }

        world.makeSound(entityhuman, "random.bow", 0.5F, 0.4F / (g.nextFloat() * 0.4F + 0.8F));
        if (!world.isStatic) {
            //world.addEntity(new EntitySnowball(world, entityhuman)); // EMC
        }

        return itemstack;
    }
}
