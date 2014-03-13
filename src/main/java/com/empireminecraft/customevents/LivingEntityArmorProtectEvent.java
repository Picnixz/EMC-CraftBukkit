package com.empireminecraft.customevents;

import com.empireminecraft.cbmisc.CBMiscUtils;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityLiving;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityEvent;

public class LivingEntityArmorProtectEvent extends EntityEvent implements Cancellable {


    private static final HandlerList handlers = new HandlerList();
    private final EntityDamageEvent.DamageCause cause;
    private final LivingEntity entity;

    private float itemDamage;
    private int armorValue = 0;

    private boolean cancelled = false;
    private float armorProtectionPct;

    public LivingEntityArmorProtectEvent(DamageSource source,
                                         EntityLiving entity, float itemDamage, int armorValue) {

        super(entity.getBukkitEntity());
        this.itemDamage = itemDamage;
        cause = CBMiscUtils.getCause(source);
        this.entity = (LivingEntity) entity.getBukkitEntity();
        setArmorValue(armorValue);
    }
    public int getArmorValue() {
        return armorValue;
    }

    public void setArmorValue(int armorValue) {
        this.armorValue = armorValue;
        armorProtectionPct = (float) (getMaxArmorValue() - armorValue) / (float) getMaxArmorValue();
    }

    public int getMaxArmorValue() {
        return 25;
    }

    public float getArmorProtectionPct() {
        return armorProtectionPct;
    }

    public void setArmorProtectionPct(float armorProtectionPct) {
        this.armorProtectionPct = armorProtectionPct;
    }

    public float getItemDamage() {
        return itemDamage;
    }

    public void setItemDamage(float itemDamage) {
        this.itemDamage = itemDamage;
    }

    public EntityDamageEvent.DamageCause getCause() {
        return cause;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

}
