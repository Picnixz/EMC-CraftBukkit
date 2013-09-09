package com.empireminecraft.customevents;

import net.minecraft.server.IInventory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;


public class HopperEvents {
    public static boolean drain(CraftWorld world, Inventory inventory, double x, double y, double z) {
        Drain event = new Drain(world, inventory, x, y, z);
        Bukkit.getPluginManager().callEvent(event);
        return event.isCancelled();
    }
    public static boolean fill(CraftWorld world, Inventory inventory, double x, double y, double z, double tx, double ty, double tz) {
        Fill event = new Fill(world, inventory, x, y, z, tx, ty, tz);
        Bukkit.getPluginManager().callEvent(event);
        return event.isCancelled();
    }

    public static class Drain extends Event implements Cancellable {
        private static final HandlerList handlers = new HandlerList();
        private final Inventory inventory;
        private boolean canceled;
        Location loc;
        public Drain(World world, Inventory inventory, double x, double y, double z) {
            this.inventory = inventory;
            loc = new Location(world, x, y, z);
        }
        public Location getHopperLocation() {
            return this.loc;
        }

        public boolean isCancelled() {
            return canceled;
        }

        public void setCancelled(boolean cancel) {
            canceled = cancel;
        }

        public HandlerList getHandlers() {
            return handlers;
        }

        public static HandlerList getHandlerList() {
            return handlers;
        }

        public Inventory getInventory() {
            return inventory;
        }
    }

    public static class Fill extends Event implements Cancellable {
        private static final HandlerList handlers = new HandlerList();
        private final Inventory inventory;
        private boolean canceled;
        Location loc;
        Location target;
        public Fill(World world, Inventory inventory, double x, double y, double z, double tx, double ty, double tz) {
            this.inventory = inventory;
            loc = new Location(world, x, y, z);
            target = new Location(world, x, y, z);
        }
        public Location getHopperLocation() {
            return this.loc;
        }
        public Location getTarget() {
            return this.target;
        }

        public boolean isCancelled() {
            return canceled;
        }

        public void setCancelled(boolean cancel) {
            canceled = cancel;
        }

        public HandlerList getHandlers() {
            return handlers;
        }

        public static HandlerList getHandlerList() {
            return handlers;
        }

        public Inventory getInventory() {
            return inventory;
        }
    }
}
