package com.empireminecraft.customevents;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntitySnowball;
import net.minecraft.server.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SnowballThrowEvent extends Event implements Cancellable {
    private final org.bukkit.inventory.ItemStack item;
    private final Snowball entity;
    private final Player player;
    private boolean consumeOnThrow = true;
    public SnowballThrowEvent(EntityHuman player, ItemStack item, EntitySnowball entity) {
        this.player = (Player) player.getBukkitEntity();
        this.entity = (Snowball) entity.getBukkitEntity();
        this.item = item.getBukkitStack();
        this.consumeOnThrow = !player.abilities.canInstantlyBuild;
    }

    public boolean shouldConsumeOnThrow() {
        return consumeOnThrow;
    }

    public void setConsumeOnThrow(boolean consumeOnThrow) {
        this.consumeOnThrow = consumeOnThrow;
    }

    public Player getPlayer() {
        return player;
    }

    public Snowball getEntity() {
        return entity;
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

    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
