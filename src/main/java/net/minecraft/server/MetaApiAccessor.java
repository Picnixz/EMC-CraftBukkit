package net.minecraft.server;

import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;

import java.util.*;

import static com.empireminecraft.metaapi.MetaApi.*;

public class MetaApiAccessor {

    public static MetaMap getWorldMetaMap(CraftWorld world) {
        return ((WorldNBTStorage)world.getHandle().dataManager).metaMap;
    }
    /**
     * Util for getting a MetaMap by location
     * @param loc
     * @return
     */
    public static MetaMap getBlockMetaMap(org.bukkit.Location loc, boolean isWrite) {
        ChunkMeta meta = ((CraftChunk)loc.getChunk()).getHandle().chunkMeta;
        return getBlockMetaMap(meta, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), isWrite);
    }

    /**
     * Util for getting a MetaMap for a chunk
     * @param chunk
     * @return
     */
    public static MetaMap getChunkMetaMap(org.bukkit.Chunk chunk, boolean isWrite) {
        ChunkMeta meta = ((CraftChunk) chunk).getHandle().chunkMeta;
        return getBlockMetaMap(meta, 0, -1, 0, isWrite);
    }

    /**
     * Gets the metamap for the specified coords, creating it if it doesnt exists
     * @param meta
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static MetaMap getBlockMetaMap(ChunkMeta meta, int x, int y, int z, boolean isWrite) {
        ChunkCoordinates coords = new ChunkCoordinates(x, y,  z);
        MetaMap ret = meta.get(coords);
        if (ret == null) {
            if (isWrite) {
                ret = new MetaMap();
                meta.put(coords, ret);
            }
        }
        return ret;
    }

    /**
     * Util for getting a MetaMap for an Entity
     * @param craftentity
     * @return
     */
    public static MetaMap getEntityMetaMap(org.bukkit.entity.Entity craftentity, boolean isWrite) {
        Entity entity = ((CraftEntity) craftentity).getHandle();
        if (entity.metaMap == null) {
            if (isWrite) {
                entity.metaMap = new MetaMap();
            }
        }
        return entity.metaMap;
    }

    public static void loadEntityMeta(Entity entity, NBTTagCompound nbt) {
        if (nbt.hasKey("_EntityMeta")) {
            NBTTagCompound nbtmeta = nbt.getCompound("_EntityMeta");
            entity.metaMap = getMetaMapFromCompound(nbtmeta);
        }
        if (entity.metaMap == null) {
            entity.metaMap = new MetaMap();
        }
    }
    public static void saveEntityMeta(Entity entity, NBTTagCompound nbt) {
        if (entity.metaMap != null && entity.metaMap.size() > 0) {
            NBTBase nbtmeta = getNbtFromObject(entity.metaMap);
            nbt.set("_EntityMeta", nbtmeta);
        }
    }

    public static void loadWorldMeta(WorldNBTStorage world, NBTTagCompound nbt) {
        if (nbt.hasKey("_WorldMeta")) {
            NBTTagCompound nbtmeta = nbt.getCompound("_WorldMeta");
            world.metaMap = getMetaMapFromCompound(nbtmeta);
        }
        if (world.metaMap == null) {
            world.metaMap = new MetaMap();
        }
    }

    public static void saveWorldMeta(WorldNBTStorage world, NBTTagCompound nbt) {
        if (world.metaMap != null && world.metaMap.size() > 0) {
            NBTBase nbtmeta = getNbtFromObject(world.metaMap);
            nbt.set("_WorldMeta", nbtmeta);
        }
    }

    /**
     * Saves this chunks Meta Data into NBT
     * @param cmp
     * @param chunk
     */
    static void saveChunkMetaNbt(NBTTagCompound cmp, Chunk chunk) {
        NBTTagCompound meta = new NBTTagCompound();
        for (Map.Entry<ChunkCoordinates, MetaMap> entry : chunk.chunkMeta.entrySet()) {
            ChunkCoordinates coords = entry.getKey();
            MetaMap list = entry.getValue();
            NBTTagCompound metalist = getCompoundFromMetaMap(list);
            if (!metalist.isEmpty()) {
                meta.set(getCoordAsStr(coords), metalist);
            }
        }

        if (!meta.isEmpty()) {
            cmp.set("_ChunkMeta", meta);
        }
    }

    /**
     * Loads this chunks Meta Data from NBT
     * @param cmp
     * @param chunk
     */
    static void loadChunkMetaNbt(NBTTagCompound cmp, Chunk chunk) {
        ChunkMeta meta = chunk.chunkMeta;
        if (cmp.hasKey("_ChunkMeta")) {
            NBTTagCompound chunkMeta = cmp.getCompound("_ChunkMeta");
            for (String key : (Collection <String>) chunkMeta.c()) {
                NBTTagCompound e = chunkMeta.getCompound(key);
                MetaMap metalist = getMetaMapFromCompound(e);
                if (!metalist.isEmpty()) {
                    meta.put(getStrAsCoord(key), metalist);
                }
            }
        }
    }

    /**
     * Converts an Object into NBT
     * @param value
     * @return
     */
    private static NBTBase getNbtFromObject(Object value) {
        if (value instanceof String) {
            return new NBTTagString((String) value);
        } else if (value instanceof ItemStack || value instanceof org.bukkit.inventory.ItemStack) {
            ItemStack item;
            if (value instanceof org.bukkit.inventory.ItemStack) {
                item = CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) value);
            } else {
                item = (ItemStack) value;
            }
            NBTTagCompound itemnbt = new NBTTagCompound();
            itemnbt.setString("MetaType", "Item");
            return item.save(itemnbt);
        } else if (value instanceof Long) {
            return new NBTTagLong((Long) value);
        } else if (value instanceof Integer) {
            return new NBTTagInt((Integer) value);
        } else if (value instanceof Double) {
            return new NBTTagDouble((Double) value);
        } else if (value instanceof Float) {
            return new NBTTagFloat((Float) value);
        } else if (value instanceof MetaMap) {
            return getCompoundFromMetaMap((MetaMap) value);
        } else if (value instanceof MetaList) {
            NBTTagList list = new NBTTagList();
            for (Object obj : (List) value) {
                NBTBase add = getNbtFromObject(obj);
                if (add != null) {
                    list.add(add);
                }
            }
            return list;
        }

        return null;
    }

    /**
     * Converts NBT into an Object
     * @param nbt
     * @return
     */
    private static Object getObjectFromNbt(NBTBase nbt) {
        if (nbt instanceof NBTTagString) {
            return nbt.a_();
        } else if (nbt instanceof NBTTagInt) {
            return ((NBTTagInt) nbt).d();
        } else if (nbt instanceof NBTTagLong) {
            return ((NBTTagLong) nbt).c();
        } else if (nbt instanceof NBTTagFloat) {
            return ((NBTTagFloat) nbt).h();
        } else if (nbt instanceof NBTTagDouble) {
            return ((NBTTagDouble) nbt).g();
        } else if (nbt instanceof NBTTagList) {
            MetaList<Object> list = new MetaList<Object>();
            NBTTagList nbtlist = (NBTTagList) nbt;
            for (int i = 0; i < nbtlist.size(); i++) {
                final Object obj = getObjectFromNbt(nbtlist.getBase(i));
                if (obj != null) {
                    list.add(obj);
                }
            }
            return list;
        } else if (nbt instanceof NBTTagCompound) {
            NBTTagCompound cmp = (NBTTagCompound) nbt.clone();
            if (cmp.hasKey("MetaType")) {
                String type = cmp.getString("MetaType");
                cmp.remove("MetaType");
                if ("Item".equals(type)) {
                    return CraftItemStack.asBukkitCopy(ItemStack.createStack(cmp));
                } else if ("MetaMap".equals(type)) {
                    return getMetaMapFromCompound(cmp);
                }
            }
        }
        return null;
    }

    /**
     * Converts a NBTTagCompound to a MetaMap
     * @param cmp
     * @return
     */
    private static MetaMap getMetaMapFromCompound(NBTTagCompound cmp) {
        MetaMap list = new MetaMap();
        for (String key : (Collection<String>) cmp.c()) {
            NBTBase nbt = cmp.get(key);
            if (nbt != null) {
                list.put(key, getObjectFromNbt(nbt));
            }
        }
        return list;
    }

    /**
     * Converts a MetaMap into an NBTTagCompount
     * @param map
     * @return
     */
    private static NBTTagCompound getCompoundFromMetaMap(MetaMap map) {
        NBTTagCompound cmp = new NBTTagCompound();
        cmp.setString("MetaType", "MetaMap");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            NBTBase add = getNbtFromObject(entry.getValue());
            if (add != null) {
                cmp.set(entry.getKey(), add);
            }
        }
        return cmp;
    }

    /**
     * Translates ChunkCoordinates to a String form
     * @param coords
     * @return
     */
    static String getCoordAsStr(ChunkCoordinates coords) {
        if (coords == null) {
            return null;
        }
        return "" + coords.x + ":" + coords.y + ":" + coords.z;
    }

    /**
     * Parses a string into ChunkCoordinates
     * @param loc
     * @return
     */
    static ChunkCoordinates getStrAsCoord(String loc) {
        if (loc != null)  {
            String[] args = loc.split(":", 3);
            if (args.length == 3) {
                try {
                    int x = (int) Math.floor(Float.parseFloat(args[0]));
                    int y = (int) Math.floor(Float.parseFloat(args[1]));
                    int z = (int) Math.floor(Float.parseFloat(args[2]));
                    return new ChunkCoordinates(x, y, z);
                } catch (NumberFormatException e) {

                }
            }
        }
        return null;
    }

    public static class ChunkMeta extends HashMap<ChunkCoordinates, MetaMap> {}
}
