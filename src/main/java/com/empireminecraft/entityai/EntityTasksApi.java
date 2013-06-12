package com.empireminecraft.entityai;

import net.minecraft.server.EntityTasksHandler;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityTasksApi {
    public static EntityTask scheduleTask(Entity entity, int interval, EntityTask task) {
        final EntityTasksHandler.TaskList entityTasks = ((CraftEntity) entity).getHandle().entityTasks;
        List<EntityTask> entityTasksList = entityTasks.get(interval);
        if (entityTasksList == null) {
            entityTasksList = new ArrayList<EntityTask>(8);
            entityTasks.put(interval, entityTasksList);
        }

        entityTasksList.add(task);
        return task;
    }
    public static void cancelTasks(Entity entity) {
        ((CraftEntity) entity).getHandle().entityTasks.clear();
    }

    public abstract static class EntityTask {
        public int limit = -1;
        public int count = 0;

        public EntityTask() {}
        public EntityTask(int limit) {
            this.limit = limit;
        }

        public abstract void run(Entity entity);

        public void abort() {
            this.limit = 1;
            this.count = 1;
        }
    }
}
