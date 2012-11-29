/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bukkit.event.entity;



import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author aikar
 */
public class SpawnerInitiateEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    Location loc;
    World world;
    String mob;
    HumanEntity entity;
    public SpawnerInitiateEvent(String mob, World world, Location loc, HumanEntity entity) {        
        this.world = world;
        this.loc = loc;
        this.mob = mob;
        this.entity = entity;
    }

    public World getWorld() {
        return this.world;
    }
    
    public String getMobName() {
        return this.mob;
    }
    
    public HumanEntity getTriggeringPlayer() {
        return this.entity;
    }

    public Location getSpawnerLocation() {
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
}
