package com.empireminecraft.entityai;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.PathEntity;
import net.minecraft.server.PathPoint;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftCreature;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;

public class EntityMoveApi {
    public static boolean hasEntityPath(Creature entity) {
        final PathEntity pathEntity = ((CraftCreature) entity).getHandle().pathEntity;
        return (pathEntity != null);
    }
    public static Location getEntityPathDestination(Creature entity) {

        final PathEntity pathEntity = ((CraftCreature) entity).getHandle().pathEntity;
        if (pathEntity == null) {
            return null;
        }

        final PathPoint pathPoint = pathEntity.c();
        if (pathPoint == null) {
            return null;
        }
        return new Location(entity.getWorld(), pathPoint.a, pathPoint.b, pathPoint.c);
    }
    public static boolean setEntityDestination(Creature entity, Location loc) {
        final EntityCreature handle = ((CraftCreature) entity).getHandle();

        final boolean onGround = handle.onGround;
        handle.onGround = true;
        PathEntity path = null;
        if (loc != null) {
            path = handle.getNavigation().a(loc.getBlockX(),
                loc.getBlockY(),
                loc.getBlockZ());
        }

        handle.setPathEntity(path);
        handle.onGround = onGround;
        return path != null;
    }

    public static boolean setEntityDestination(Creature entity, LivingEntity target) {
        final EntityCreature handle = ((CraftCreature) entity).getHandle();

        final boolean onGround = handle.onGround;
        handle.onGround = true;
        PathEntity path = null;
        if (target != null) {
            path = handle.getNavigation().a(((CraftLivingEntity)target).getHandle());
        }

        handle.setPathEntity(path);
        handle.onGround = onGround;
        return path != null;
    }
}
