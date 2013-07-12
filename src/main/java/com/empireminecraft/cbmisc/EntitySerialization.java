package com.empireminecraft.cbmisc;

import net.minecraft.server.*;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;

import java.io.ByteArrayInputStream;

public class EntitySerialization {
    public static byte[] serializeEntity(Entity craftentity) {
        if (craftentity == null) {
            throw new NullPointerException("Entity can not be null for serialization");
        }
        net.minecraft.server.Entity entity = ((CraftEntity) craftentity).getHandle();
        NBTTagCompound cmp = new NBTTagCompound();
        entity.serializeEntity(cmp);

        return NBTCompressedStreamTools.a(cmp);
    }

    public static Entity deserializeEntity(byte[] data, org.bukkit.World world) {
        if (data == null || data.length == 0) {
            throw new NullPointerException("Data can not be null or empty for deserialization");
        }
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        final NBTTagCompound cmp = NBTCompressedStreamTools.a(stream);
        return EntityTypes.a(cmp, ((CraftWorld) world).getHandle()).getBukkitEntity();
    }
}
