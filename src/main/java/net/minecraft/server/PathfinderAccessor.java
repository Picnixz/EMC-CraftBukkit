package net.minecraft.server;

public class PathfinderAccessor {
    public static PathfinderGoal getPathfinderGoal(Object object) {
        if (object instanceof PathfinderGoalSelectorItem) {
            return ((PathfinderGoalSelectorItem) object).a;
        }
        return null;
    }
}
