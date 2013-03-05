package com.empireminecraft.metaapi;

import net.minecraft.server.MetaApiAccessor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;

public class MetaApi {

    //////////////////////////////////////////////////
    //// WORLD META
    //////////////////////////////////////////////////

    public static MetaMap getWorldMetaMap(World world) {
        return MetaApiAccessor.getWorldMetaMap((CraftWorld) world);
    }

    public static <T> T getWorldMeta(World world, String key) {
        return getWorldMeta(world, key, null);
    }

    public static <T> T getWorldMeta(World world, String key, T def) {
        final MetaMap worldMetaMap = getWorldMetaMap(world);
        T ret = worldMetaMap != null && !worldMetaMap.isEmpty() ? (T) worldMetaMap.get(key) : null;
        return ret != null ? ret : null;
    }

    public static boolean hasWorldMeta(World world, String key) {
        final MetaMap worldMetaMap = getWorldMetaMap(world);
        return worldMetaMap != null && !worldMetaMap.isEmpty() && worldMetaMap.containsKey(key);
    }

    public static <T> T setWorldMeta(World world, String key, Object val) {
        return setMetaMapValue(getWorldMetaMap(world), key, val);
    }

    public static <T> T removeWorldMeta(World world, String key) {
        return setWorldMeta(world, key, null);
    }
    
    //////////////////////////////////////////////////
    //// ENTITY META
    //////////////////////////////////////////////////

    public static MetaMap getEntityMetaMap(Entity entity, boolean isWrite) {
        return MetaApiAccessor.getEntityMetaMap(entity, isWrite);
    }

    public static <T> T getEntityMeta(Entity entity, String key) {
        return getEntityMeta(entity, key, null);
    }

    public static <T> T getEntityMeta(Entity entity, String key, T def) {
        final MetaMap entityMetaMap = getEntityMetaMap(entity, false);
        T ret = entityMetaMap != null && !entityMetaMap.isEmpty() ? (T) entityMetaMap.get(key) : null;
        return ret != null ? ret : null;
    }

    public static boolean hasEntityMeta(Entity entity, String key) {
        final MetaMap entityMetaMap = getEntityMetaMap(entity, false);
        return entityMetaMap != null && !entityMetaMap.isEmpty() && entityMetaMap.containsKey(key);
    }

    public static <T> T setEntityMeta(Entity entity, String key, Object val) {
        return setMetaMapValue(getEntityMetaMap(entity, true), key, val);
    }

    public static <T> T removeEntityMeta(Entity entity, String key) {
        return setEntityMeta(entity, key, null);
    }

    //////////////////////////////////////////////////
    //// CHUNK META
    //////////////////////////////////////////////////

    public static MetaMap getChunkMetaMap(Chunk chunk, boolean isWrite) {
        return MetaApiAccessor.getChunkMetaMap(chunk, isWrite);
    }

    public static boolean hasChunkMeta(Chunk chunk, String key) {
        final MetaMap chunkMetaMap = getChunkMetaMap(chunk, false);
        return chunkMetaMap != null && !chunkMetaMap.isEmpty() && chunkMetaMap.containsKey(key);
    }

    public static <T> T getChunkMeta(Chunk chunk, String key) {
        return getChunkMeta(chunk, key, null);
    }

    public static <T> T getChunkMeta(Chunk chunk, String key, T def) {
        final MetaMap chunkMetaMap = getChunkMetaMap(chunk, false);
        T ret = chunkMetaMap != null && !chunkMetaMap.isEmpty() ? (T) getChunkMetaMap(chunk, false).get(key) : null;
        return ret != null ? ret : null;
    }

    public static <T> T setChunkMeta(Chunk chunk, String key, Object val) {
        return setMetaMapValue(getChunkMetaMap(chunk, true), key, val);
    }

    public static <T> T removeChunkMeta(Chunk chunk, String key) {
        return setChunkMeta(chunk, key, null);
    }

    //////////////////////////////////////////////////
    //// BLOCK META
    //////////////////////////////////////////////////

    public static MetaMap getBlockMetaMap(Location loc, boolean isWrite) {
        return MetaApiAccessor.getBlockMetaMap(loc, isWrite);
    }

    public static boolean hasBlockMeta(Location loc, String key) {
        final MetaMap blockMetaMap = getBlockMetaMap(loc, false);
        return blockMetaMap != null && !blockMetaMap.isEmpty() && blockMetaMap.containsKey(key);
    }

    public static <T> T getBlockMeta(Location loc, String key) {
        return getBlockMeta(loc, key, null);
    }

    public static <T> T getBlockMeta(Location loc, String key, T def) {
        final MetaMap blockMetaMap = getBlockMetaMap(loc, false);
        T ret = blockMetaMap != null && !blockMetaMap.isEmpty() ? (T) blockMetaMap.get(key) : null;
        return ret != null ? ret : null;
    }

    public static <T> T setBlockMeta(Location loc, String key, Object val) {
        return setMetaMapValue(getBlockMetaMap(loc, true), key, val);
    }

    public static <T> T removeBlockMeta(Location loc, String key) {
        return setBlockMeta(loc, key, null);
    }

    //////////////////////////////////////////////////
    //// UTIL / CLASSES
    //////////////////////////////////////////////////

    /**
     * General method for determining if value is null to remove it, else set.
     * Return previous value
     *
     * @param map
     * @param key
     * @param val
     * @param <T>
     * @return
     */
    private static <T> T setMetaMapValue(MetaMap map, String key, Object val) {
        if (val == null) {
            return (T) map.remove(key);
        } else {
            return (T) map.put(key, val);
        }
    }

    /**
     * Checks if the passed object can be stored as Meta Data
     * @param value
     * @return
     */
    public static boolean isValidMeta(Object value) {
        return (value instanceof String) || (value instanceof Long) ||
            (value instanceof Integer) || (value instanceof ItemStack) ||
            (value instanceof Float) || (value instanceof Double) ||
            (value instanceof MetaMap) || (value instanceof MetaList);
    }

    /**
     * A type protected array for storing meta values
     */
    public static class MetaList<T> extends ArrayList<T> {
        @Override
        public boolean add(T o) {
            if (!isValidMeta(o)) {
                throw new InvalidParameterException();
            }
            return super.add(o);
        }

        @Override
        public T set(int index, T element) {
            if (!isValidMeta(element)) {
                throw new InvalidParameterException();
            }
            return super.set(index, element);
        }

        @Override
        public void add(int index, T element) {
            if (!isValidMeta(element)) {
                throw new InvalidParameterException();
            }
            super.add(index, element);
        }
    }

    /**
     * A type protected hashmap for storing meta values
     */
    public static class MetaMap extends HashMap<String, Object> {
        @Override
        public Object put(String key, Object value) {
            if (!isValidMeta(value)) {
                throw new InvalidParameterException();
            }
            return super.put(key, value);
        }
    }
}
