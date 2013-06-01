package com.empireminecraft.entityai;

import net.minecraft.server.AttributeInstance;
import net.minecraft.server.AttributesAccessor;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.IAttribute;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public class AttributesAPI {
    public static boolean setAttribute(LivingEntity livingEntity, Attribute attr, double val) {
        final EntityLiving entity = ((CraftLivingEntity) livingEntity).getHandle();
        final IAttribute attributetype = AttributesAccessor.getAttribute(attr);
        if (attributetype == null) {
            throw new NullPointerException("Bad Attribute Type");
        }
        final AttributeInstance attribute = entity.getAttributeInstance(attributetype);
        if (attribute != null) {
            attribute.setValue(val);
            return true;
        }
        return false;
    }
    public static Double getAttribute(LivingEntity livingEntity, Attribute attr) {
        final EntityLiving entity = ((CraftLivingEntity) livingEntity).getHandle();
        final IAttribute attributetype = AttributesAccessor.getAttribute(attr);

        if (attributetype == null) {
            throw new NullPointerException("Bad Attribute Type");
        }
        final AttributeInstance attribute = entity.getAttributeInstance(attributetype);
        if (attribute != null) {
            return attribute.b(); // Don't use attribute modifier version (getValue)
        }
        return attributetype.b();
    }


    public enum Attribute {
        TARGET_RANGE,
        FOLLOW_RANGE,
        MAX_HEALTH,
        KNOCKBACK_RESISTANCE,
        MOVEMENT_SPEED,
        ATTACK_DAMAGE;
    }
}
