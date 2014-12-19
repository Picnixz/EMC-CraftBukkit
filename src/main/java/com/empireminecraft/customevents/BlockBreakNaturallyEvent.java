package com.empireminecraft.customevents;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

public class BlockBreakNaturallyEvent extends BlockEvent {
    final Item item;
    public BlockBreakNaturallyEvent(net.minecraft.server.World world, int x, int y, int z, net.minecraft.server.EntityItem item) {
        super(new Location(world.getWorld(), x, y, z).getBlock());
        this.item = (Item) item.getBukkitEntity();
    }

    public Item getItem() {
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
