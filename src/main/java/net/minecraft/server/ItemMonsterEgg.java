package net.minecraft.server;
//
import com.empireminecraft.customevents.MonsterEggSpawnEvent;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity; //

public class ItemMonsterEgg extends Item {

    public ItemMonsterEgg() {
        this.a(true);
        this.a(CreativeModeTab.f);
    }

    public String n(ItemStack itemstack) {
        String s = ("" + LocaleI18n.get(this.getName() + ".name")).trim();
        String s1 = EntityTypes.b(itemstack.getData());

        if (s1 != null) {
            s = s + " " + LocaleI18n.get("entity." + s1 + ".name");
        }

        return s;
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        // CraftBukkit - check ItemStack data
        if (world.isStatic || itemstack.getData() == 48 || itemstack.getData() == 49 || itemstack.getData() == 63 || itemstack.getData() == 64) {
            return true;
        } else {
            Block block = world.getType(i, j, k);

            i += Facing.b[l];
            j += Facing.c[l];
            k += Facing.d[l];
            double d0 = 0.0D;

            if (l == 1 && block.b() == 11) {
                d0 = 0.5D;
            }

            Entity entity = spawnCreature(world, entityhuman, itemstack, itemstack.getData(), (double) i + 0.5D, (double) j + d0, (double) k + 0.5D); // EMC

            if (entity != null) {
                if (entity instanceof EntityLiving && itemstack.hasName()) {
                    ((EntityInsentient) entity).setCustomName(itemstack.getName());
                }

                if (!entityhuman.abilities.canInstantlyBuild) {
                    --itemstack.count;
                }
            }

            return true;
        }
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (world.isStatic) {
            return itemstack;
        } else {
            MovingObjectPosition movingobjectposition = this.a(world, entityhuman, true);

            if (movingobjectposition == null) {
                return itemstack;
            } else {
                if (movingobjectposition.type == EnumMovingObjectType.BLOCK) {
                    int i = movingobjectposition.b;
                    int j = movingobjectposition.c;
                    int k = movingobjectposition.d;

                    if (!world.a(entityhuman, i, j, k)) {
                        return itemstack;
                    }

                    if (!entityhuman.a(i, j, k, movingobjectposition.face, itemstack)) {
                        return itemstack;
                    }

                    if (world.getType(i, j, k) instanceof BlockFluids) {
                        Entity entity = spawnCreature(world, entityhuman, itemstack, itemstack.getData(), (double) i, (double) j, (double) k); // EMC

                        if (entity != null) {
                            if (entity instanceof EntityLiving && itemstack.hasName()) {
                                ((EntityInsentient) entity).setCustomName(itemstack.getName());
                            }

                            if (!entityhuman.abilities.canInstantlyBuild) {
                                --itemstack.count;
                            }
                        }
                    }
                }

                return itemstack;
            }
        }
    }

    public static Entity a(World world, int i, double d0, double d1, double d2) {
        // CraftBukkit start - delegate to spawnCreature
        return spawnCreature(world, i, d0, d1, d2, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG);
    }

    // EMC start
    public static Entity spawnCreature(World world, int i, double d0, double d1, double d2, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason spawnReason) { // EMC
        return spawnCreature(world, null, null, i, d0, d1, d2, spawnReason);
    }
    public static Entity spawnCreature(World world, EntityHuman player, ItemStack item, int i, double d0, double d1, double d2) {
        return spawnCreature(world, player, item, i, d0, d1, d2, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG);
    }
    public static Entity spawnCreature(World world, EntityHuman player, ItemStack item, int i, double d0, double d1, double d2, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason spawnReason) {
    // EMC end
        // CraftBukkit end
        if (!EntityTypes.eggInfo.containsKey(Integer.valueOf(i))) {
            return null;
        } else {
            Entity entity = null;

            for (int j = 0; j < 1; ++j) {
                entity = EntityTypes.a(i, world);
                if (entity != null && entity instanceof EntityLiving) {
                    EntityInsentient entityinsentient = (EntityInsentient) entity;

                    entity.setPositionRotation(d0, d1, d2, MathHelper.g(world.random.nextFloat() * 360.0F), 0.0F);
                    entityinsentient.aO = entityinsentient.yaw;
                    entityinsentient.aM = entityinsentient.yaw;
                    entityinsentient.prepare((GroupDataEntity) null);

                    // EMC start - if false the spawn was cancelled, add new event
                    if (!world.addEntity(entity, spawnReason)) { // CraftBukkit
                        return null;
                    }
                    if (item != null) {
                        final MonsterEggSpawnEvent event = new MonsterEggSpawnEvent(player.getBukkitEntity(), (LivingEntity) entity.getBukkitEntity(), CraftItemStack.asCraftMirror(item));

                        if (!event.callEvent()) {
                            world.removeEntity(entity);
                            return null;
                        }
                        if (event.getEntity().getEntityId() != entity.getId()) {
                            world.removeEntity(entity);
                            entity = entityinsentient = (EntityInsentient) ((CraftEntity) event.getEntity()).getHandle();
                        }
                    }
                    // EMC end
                    entityinsentient.r();
                }
            }

            return entity;
        }
    }
}
