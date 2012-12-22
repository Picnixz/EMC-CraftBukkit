package org.bukkit.event.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerUseItemEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    Player player;
    ItemStack item;
    boolean consume = true;
    public PlayerUseItemEvent(Player player, ItemStack item) {        
        this.player = player;
        this.item = item;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ItemStack getItem() {
        return this.item;
    }
    public void setItem(ItemStack item) {
        if (item == null) {
            canceled = true;
        }
        this.item = item;
    }
    public boolean getConsumeItem() {
        return this.consume;
    }
    
    public void setConsumeItem(boolean consume) {
        this.consume = consume;
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
}
