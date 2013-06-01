package com.empireminecraft.entityai;

import net.minecraft.server.*;
import net.minecraft.server.Entity;
import org.bukkit.craftbukkit.entity.CraftAnimals;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftCreature;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;

public class EntityAIApi {
    /**
     * Is this entity blocked from ticking
     * @param entity
     * @return
     */
    public static boolean isEntityDisabled(org.bukkit.entity.Entity entity) {
        return ((CraftEntity) entity).getHandle().isDisabled;
    }

    /**
     * Disables Ticking on an Entity
     * @param entity
     * @param disabled
     */
    public static void setDisabledEntity(org.bukkit.entity.Entity entity, boolean disabled) {
        ((CraftEntity) entity).getHandle().isDisabled = disabled;
    }
    public static int getLove(Animals animal) {
        return ((CraftAnimals) animal).getHandle().love;
    }
    public static void setLove(Animals animal, int love) {
        ((CraftAnimals) animal).getHandle().love = love;
    }
    public static void setFireProof(Creature creature, boolean flag) {
        EntityCreature entity = ((CraftCreature)creature).getHandle();
        entity.fireProof = flag;
    }
    public static void makeAggressive(Creature creature, float range) {
        EntityCreature entity = ((CraftCreature)creature).getHandle();
        entity.goalSelector.a(2, new PathfinderGoalMeleeAttack(entity, EntityHuman.class, 1.0F, true));
        entity.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(entity, EntityHuman.class,  0, true));
        setTargetRange(creature, range);
    }

    public static void makePeaceful(Creature creature) {
        EntityCreature entity = (EntityCreature) ((CraftEntity)creature).getHandle();

        List check = new ArrayList();
        check.addAll(entity.goalSelector.b);
        check.addAll(entity.goalSelector.c);
        check.addAll(entity.targetSelector.b);
        check.addAll(entity.targetSelector.c);


        entity.peaceful = true;

        for (Object o : check) {
            PathfinderGoal goal = PathfinderAccessor.getPathfinderGoal(o);
            if (goal instanceof PathfinderGoalMeleeAttack ||
                goal instanceof PathfinderGoalArrowAttack ||
                goal instanceof PathfinderGoalMoveThroughVillage ||
                goal instanceof PathfinderGoalBreakDoor ||
                goal instanceof PathfinderGoalNearestAttackableTarget ||
                goal instanceof PathfinderGoalHurtByTarget) {

                entity.goalSelector.b.remove(o);
                entity.goalSelector.c.remove(o);
                entity.targetSelector.b.remove(o);
                entity.targetSelector.c.remove(o);
            }

        }
    }
    public static void setEntitySize(Creature creature, float width, float height) {
        EntityCreature entity = ((CraftCreature)creature).getHandle();
        entity.setSize(width, height);
    }

    public static void setTargetRange(Creature creature, float range) {
        AttributesAPI.setAttribute(creature, AttributesAPI.Attribute.TARGET_RANGE, range);
    }

    public static void setArrowAttackRange(Monster monster, float range) {
        setTargetRange(monster, range);
        EntityMonster entity = (EntityMonster) ((CraftEntity)monster).getHandle();
        for (Object o : entity.goalSelector.b) {
            PathfinderGoal goal = PathfinderAccessor.getPathfinderGoal(o);
            if (goal instanceof PathfinderGoalArrowAttack) {
                ((PathfinderGoalArrowAttack) goal).i = range;
                ((PathfinderGoalArrowAttack) goal).j = range*range;
            }
        }
    }

    public static void setArrowAttackSpeed(Monster monster, Integer min, Integer max) {
        EntityMonster entity = (EntityMonster) ((CraftEntity)monster).getHandle();
        for (Object o : entity.goalSelector.b) {
            PathfinderGoal goal = PathfinderAccessor.getPathfinderGoal(o);
            if (goal instanceof PathfinderGoalArrowAttack) {
                if (min != null) {
                    ((PathfinderGoalArrowAttack) goal).g = min;
                }
                if (max != null) {
                    ((PathfinderGoalArrowAttack) goal).h = max;
                }
            }
        }
    }
    public static void setEntityMaxPathfindingRange(Creature creature, float range) {
        AttributesAPI.setAttribute(creature, AttributesAPI.Attribute.FOLLOW_RANGE, range);
    }
}
