package com.empireminecraft.customevents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

public class AchievementBroadcastEvent extends Event {
    final Player player;
    List<Player> receivers = new ArrayList<Player>();

    public Player getPlayer() {
        return player;
    }

    public List<Player> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<Player> receivers) {
        this.receivers = receivers;
    }

    private static final HandlerList handlers = new HandlerList();

    public AchievementBroadcastEvent(Player player) {this.player = player;}

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
