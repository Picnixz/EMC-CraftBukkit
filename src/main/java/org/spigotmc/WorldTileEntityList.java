package org.spigotmc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WorldTileEntityList extends HashSet {
    final Map<Class, List<TileEntity>> tickList = Maps.newHashMap();

    private final World world;

    public WorldTileEntityList(World world) {
        this.world = world;
    }

    @Override
    public boolean add(Object o) {
        if (getInterval(o.getClass()) != 0) {
            add((TileEntity) o);
        }
        return true;
    }

    public void add(TileEntity entity) {
        if (entity.isAdded) {
            return;
        }
        Class cls = entity.getClass();
        List<TileEntity> list = tickList.get(cls);
        if (list == null) {
            list = Lists.newArrayList();
            tickList.put(cls, list);
        }
        list.add(entity);
        entity.isAdded = true;
    }

    @Override
    public boolean remove(Object o) {
        final Class cls = o.getClass();
        final List<TileEntity> list = tickList.get(cls);
        if (list != null) {
            list.remove(o);
            ((TileEntity) o).isAdded = false;
        }
        return true;
    }

    @Override
    public Iterator iterator() {
        return new Iterator() {
            Iterator<Map.Entry<Class, List<TileEntity>>> typeIterator;
            Map.Entry<Class, List<TileEntity>> curType = null;
            Iterator<TileEntity> listIterator = null;
            {
                typeIterator = tickList.entrySet().iterator();
                nextType();
            }

            private boolean nextType() {
                if (typeIterator.hasNext()) {
                    curType = typeIterator.next();
                    final Integer interval = getInterval(curType.getKey());
                    if (world.getTime() % interval != 0) {
                        listIterator = curType.getValue().iterator();
                    } else {
                        listIterator = null;
                    }
                    return true;
                } else {
                    curType = null;
                    listIterator = null;
                    return false;
                }
            }

            @Override
            public boolean hasNext() {
                do {
                    if (listIterator != null && listIterator.hasNext()) {
                        return true;
                    }
                } while (nextType());
                return false;
            }

            @Override
            public Object next() {
                return listIterator.next();
            }

            @Override
            public void remove() {
                listIterator.remove();
            }
        };
    }

    @Override
    public boolean contains(Object o) {
        return ((TileEntity) o).isAdded;
    }
    public Integer getInterval(Class cls) {
        Integer tickInterval = world.spigotConfig.tileEntityTickIntervals.get(cls);
        return tickInterval != null ? tickInterval : 1;
    }
}
