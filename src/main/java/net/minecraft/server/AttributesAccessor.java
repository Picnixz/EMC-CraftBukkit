package net.minecraft.server;

import com.empireminecraft.entityai.AttributesAPI;
import com.google.common.collect.Maps;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wither;

import java.util.HashMap;

public class AttributesAccessor {
    public static final IAttribute targetRange = (new AttributeRanged("generic.targetRange", 16.0D, 0.0D, 2048.0D)).a("Target Range"); // EMC

    static final HashMap<AttributesAPI.Attribute, IAttribute> attributeMap = Maps.newHashMap();
    static {
        attributeMap.put(AttributesAPI.Attribute.TARGET_RANGE, targetRange);
        attributeMap.put(AttributesAPI.Attribute.MAX_HEALTH, GenericAttributes.a);
        attributeMap.put(AttributesAPI.Attribute.FOLLOW_RANGE, GenericAttributes.b);
        attributeMap.put(AttributesAPI.Attribute.KNOCKBACK_RESISTANCE, GenericAttributes.c);
        attributeMap.put(AttributesAPI.Attribute.MOVEMENT_SPEED, GenericAttributes.d);
        attributeMap.put(AttributesAPI.Attribute.ATTACK_DAMAGE, GenericAttributes.e);
    }
    public static IAttribute getAttribute(AttributesAPI.Attribute attribute) {
        return attributeMap.get(attribute);
    }

    public static void configureAttributes(EntityInsentient entity) {
        entity.getAttributeInstance(targetRange)
              .a(new AttributeModifier("Random spawn bonus", entity.random.nextGaussian() * 0.05D, 1));
    }

    public static void initializeAttributes(EntityLiving entity, AttributeMapBase map) {
        if (entity instanceof EntityInsentient) {
            map.b(targetRange);
            if (entity instanceof Wither) {
                AttributesAPI.setAttribute((LivingEntity) entity.getBukkitEntity(), AttributesAPI.Attribute.TARGET_RANGE, 30D);
            }
        }
    }
}
