/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bukkit.event.entity;



import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author aikar
 */
public class SpawnTickEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    byte radius;
    World world;
    HumanEntity entity;
    public SpawnTickEvent(World world, byte radius, HumanEntity entity) {        
        this.world = world;
        this.radius = radius;
        this.entity = entity;
    }

    public World getWorld() {
        return this.world;
    }

    public HumanEntity getEntity() {
        return this.entity;
    }

    public byte getSpawnRadius() {
        return this.radius;
    }

    public void setSpawnRadius(byte radius) {
        this.radius = radius;
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
