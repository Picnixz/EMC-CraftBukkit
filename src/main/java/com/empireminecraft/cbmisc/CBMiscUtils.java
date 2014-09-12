package com.empireminecraft.cbmisc;

import com.empireminecraft.metaapi.MetaApi;
import net.minecraft.server.DamageSource;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityDamageSource;
import net.minecraft.server.EntityDamageSourceIndirect;
import net.minecraft.server.EntityHorse;
import net.minecraft.server.EntityWitherSkull;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutAttachEntity;
import net.minecraft.server.PacketPlayOutEntityMetadata;
import net.minecraft.server.PacketPlayOutSpawnEntity;
import net.minecraft.server.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.PlayerConnection;
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

    static DataWatcher convertToAS(DataWatcher old) {
        DataWatcher data = new DataWatcher(old.a);

        data.a(0, Byte.valueOf((byte) 32)); // invisible = 0x20
        data.a(1, Short.valueOf((short) 300));
        data.a(2, old.getString(2));
        data.a(3, old.getObject(3));

        data.a(7, Integer.valueOf(0));
        data.a(8, Byte.valueOf((byte) 0));
        data.a(9, Byte.valueOf((byte) 0));
        data.a(6, Float.valueOf(1.0F));

        data.a(10, Byte.valueOf((byte) 8)); // 8 = nograv
        return data;
    }
    public static boolean isHolo(Entity entity) {
        return entity != null && MetaApi.hasEntityMeta(entity.getBukkitEntity(), "holoId");
    }
    public static Packet checkHologram(Packet packet, PlayerConnection conn) {
        if (!conn.is18) return packet;

        if (packet instanceof PacketPlayOutSpawnEntityLiving) {
            PacketPlayOutSpawnEntityLiving ple = (PacketPlayOutSpawnEntityLiving) packet;
            if (isHolo(ple.entityLiving) && ple.entityLiving instanceof EntityHorse) {
                ple.b = 30; // Armor Stand
                ple.d = MathHelper.floor((ple.entityLiving.getBukkitEntity().getLocation().getY()-56.7) * 32.0D);
                ple.l = convertToAS(ple.l);
            }
        } else if (packet instanceof PacketPlayOutSpawnEntity) {
            PacketPlayOutSpawnEntity pe = (PacketPlayOutSpawnEntity) packet;
            if (isHolo(pe.entity) && pe.entity instanceof EntityWitherSkull) {
                return null;
            }
        } else if (packet instanceof PacketPlayOutEntityMetadata) {
            PacketPlayOutEntityMetadata pm = (PacketPlayOutEntityMetadata) packet;
            if (isHolo(pm.data.a) && pm.data.a instanceof EntityHorse) {
               packet = new PacketPlayOutEntityMetadata(pm.a, convertToAS(pm.data), pm.flag);
            }
        } else if (packet instanceof PacketPlayOutAttachEntity) {
            PacketPlayOutAttachEntity pae = (PacketPlayOutAttachEntity) packet;
            if (isHolo(pae.vehicle)) {
                return null;
            }
        }
        return packet;
    }
}
