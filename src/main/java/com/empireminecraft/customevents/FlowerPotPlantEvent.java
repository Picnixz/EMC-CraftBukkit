package com.empireminecraft.customevents;

import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntityFlowerPot;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FlowerPotPlantEvent extends Event {
    Block block;
    org.bukkit.inventory.ItemStack item;
    public FlowerPotPlantEvent(TileEntityFlowerPot pot, ItemStack itemstack) {
        this.block = new Location(pot.getWorld().getWorld(), pot.x, pot.y, pot.z).getBlock();
        this.item = itemstack.getBukkitStack();
    }

    public Block getBlock() {
        return block;
    }

    public org.bukkit.inventory.ItemStack getItem() {
        return item;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
