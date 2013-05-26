package com.empireminecraft.customevents;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EntityEvents {
    public static class EntitySpawnEvent extends Event {
        private static final HandlerList handlers = new HandlerList();

        private final Entity entity;

        public EntitySpawnEvent(Entity entity) {
            this.entity = entity;
        }

        public Entity getEntity() {
            return entity;
        }

        @Override
        public HandlerList getHandlers() {
            return handlers;
        }

        public static HandlerList getHandlerList() {
            return handlers;
        }
    }
    public static class EntityDespawnEvent extends Event {
        private static final HandlerList handlers = new HandlerList();

        private final Entity entity;

        public EntityDespawnEvent(Entity entity) {
            this.entity = entity;
        }

        public Entity getEntity() {
            return entity;
        }

        @Override
        public HandlerList getHandlers() {
            return handlers;
        }

        public static HandlerList getHandlerList() {
            return handlers;
        }
    }
}
