package com.empireminecraft.customevents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class AnvilEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    final Player player;
    final ItemStack left;
    final ItemStack right;
    ItemStack result;
    int cost;
    public AnvilEvent(Player player, ItemStack left, ItemStack right, ItemStack result, int cost) {
        this.player = player;
        this.left = left;
        this.right = right;
        this.result = result;
        this.cost = cost;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getLeft() {
        return left;
    }

    public ItemStack getRight() {
        return right;
    }

    public ItemStack getResult() {
        return result;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setResult(ItemStack result) {
        this.result = result;
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
