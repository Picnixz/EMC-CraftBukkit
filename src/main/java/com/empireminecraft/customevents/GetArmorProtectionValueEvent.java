package com.empireminecraft.customevents;

import net.minecraft.server.ItemArmor;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class GetArmorProtectionValueEvent extends Event {
    private final ItemStack item;
    private int armorProtectionValue;

    public static int GetArmorProtectionValue(net.minecraft.server.ItemStack item, int orig) {
        if (!(item.getItem() instanceof ItemArmor)) {
            return 0;
        }
        GetArmorProtectionValueEvent event = new GetArmorProtectionValueEvent(item, orig);
        event.callEvent();
        return event.getArmorProtectionValue();
    }
    private GetArmorProtectionValueEvent(net.minecraft.server.ItemStack item, int orig) {
        this.item = CraftItemStack.asCraftMirror(item);
        this.armorProtectionValue = orig;
    }

    public int getArmorProtectionValue() {
        return armorProtectionValue;
    }

    public void setArmorProtectionValue(int armorProtectionValue) {
        this.armorProtectionValue = armorProtectionValue;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ItemStack getItem() {
        return item;
    }
}
