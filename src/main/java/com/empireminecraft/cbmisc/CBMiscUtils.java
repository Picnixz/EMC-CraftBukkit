package com.empireminecraft.cbmisc;

import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityDamageSource;
import net.minecraft.server.EntityDamageSourceIndirect;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageEvent;

public class CBMiscUtils {
    public static EntityDamageEvent.DamageCause getCause(DamageSource source) {
        if (source.isExplosion()) {
            return EntityDamageEvent.DamageCause.BLOCK_EXPLOSION;
        } else if (source instanceof EntityDamageSource) {
            Entity damager;
            if (source instanceof EntityDamageSourceIndirect) {
                damager = ((EntityDamageSourceIndirect) source).getProximateDamageSource();
                if (damager.getBukkitEntity() instanceof ThrownPotion) {
                    return EntityDamageEvent.DamageCause.MAGIC;
                } else if (damager.getBukkitEntity() instanceof Projectile) {
                    return EntityDamageEvent.DamageCause.PROJECTILE;
                } else {
                    return EntityDamageEvent.DamageCause.ENTITY_ATTACK;
                }
            } else if ("thorns".equals(source.translationIndex)) {
                return EntityDamageEvent.DamageCause.THORNS;
            } else {
                return EntityDamageEvent.DamageCause.ENTITY_ATTACK;
            }
        } else {
            if (source == DamageSource.OUT_OF_WORLD) {
                return EntityDamageEvent.DamageCause.VOID;
            } else if (source == DamageSource.FIRE) {
                return EntityDamageEvent.DamageCause.FIRE;
            } else if (source == DamageSource.STARVE) {
                return EntityDamageEvent.DamageCause.STARVATION;
            } else if (source == DamageSource.WITHER) {
                return EntityDamageEvent.DamageCause.WITHER;
            } else if (source == DamageSource.STUCK) {
                return EntityDamageEvent.DamageCause.SUFFOCATION;
            } else if (source == DamageSource.DROWN) {
                return EntityDamageEvent.DamageCause.DROWNING;
            } else if (source == DamageSource.BURN) {
                return EntityDamageEvent.DamageCause.FIRE_TICK;
            } else if (source == CraftEventFactory.MELTING) {
                return EntityDamageEvent.DamageCause.MELTING;
            } else if (source == CraftEventFactory.POISON) {
                return EntityDamageEvent.DamageCause.POISON;
            } else if (source == DamageSource.MAGIC) {
                return EntityDamageEvent.DamageCause.MAGIC;
            } else {
                return EntityDamageEvent.DamageCause.CUSTOM;
            }
        }
    }
}
