package com.empireminecraft.customevents;

import net.minecraft.server.EntityLiving;
import net.minecraft.server.MobEffect;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EntityEffectAddedEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;

    final EntityLiving entity;
    private PotionEffect effect;

    public EntityEffectAddedEvent(EntityLiving entity, MobEffect mcEffect) {
        this.entity = entity;
        effect = new PotionEffect(PotionEffectType.getById(mcEffect.getEffectId()),
            mcEffect.getDuration(), mcEffect.getAmplifier());
    }

    public PotionEffect getEffect() {
        return effect;
    }

    public void setEffect(PotionEffect effect) {
        this.effect = effect;
    }

    public LivingEntity getEntity() {
        return (LivingEntity) entity.getBukkitEntity();
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
