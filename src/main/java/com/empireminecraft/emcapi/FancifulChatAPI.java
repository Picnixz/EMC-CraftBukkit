package com.empireminecraft.emcapi;

/**
 * Credits to original version @
 * http://forums.bukkit.org/threads/lib-fanciful-pleasant-chat-message-formatting.195148/
 *
 * Improved to use proper components to fix issues
 * @author aikar
 *
 */

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.empireminecraft.cbmisc.HiddenItemMeta;
import net.minecraft.server.*;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.JSONException;
import org.json.JSONStringer;
import org.json.JSONWriter;

public class FancifulChatAPI {
    protected final List<MessagePart> messageParts = new ArrayList<MessagePart>();


    public FancifulChatAPI file(final String path) {
        onClick("open_file", path);
        return this;
    }

    public FancifulChatAPI link(final String url) {
        onClick("open_url", url);
        return this;
    }

    public FancifulChatAPI suggest(final String command) {
        onClick("suggest_command", command);
        return this;
    }

    public FancifulChatAPI command(final String command) {
        onClick("run_command", command);
        return this;
    }

    public FancifulChatAPI achievementTooltip(final String name) {
        onHover("show_achievement", "achievement." + name);
        return this;
    }

    public FancifulChatAPI itemTooltip(final String itemJSON) {
        onHover("show_item", itemJSON);
        return this;
    }

    public FancifulChatAPI itemTooltip(final ItemStack itemStack) {
        NBTTagCompound root = CraftItemStack.asNMSCopy(itemStack).save(new NBTTagCompound());
        if (root.hasKey("tag")) {
            NBTTagCompound tag = HiddenItemMeta.filterItemLore(root.getCompound("tag"), false);
            NBTTagCompound display = tag.getCompound("display");
            if (display.hasKey("Lore")) {
                NBTTagList lore = display.getList("Lore", 8);
                NBTTagList newlore = new NBTTagList();

                for (int i = 0; i < lore.size(); i++) {
                    String line = lore.getString(i);
                    String newline = (line.isEmpty() ? " " : line).replace("\"", "\\\"");
                    newlore.add(new NBTTagString(newline));
                }
                display.set("Lore", newlore);
            }

            root.set("tag", tag);
        }
        return itemTooltip(root.toString());
    }

    public FancifulChatAPI tooltip(final String text) {
        final String[] lines = text.split("\\n");
        if (lines.length <= 1) {
            onHover("show_text", text);
        } else {
            itemTooltip(makeMultilineTooltip(lines));
        }
        return this;
    }

    public FancifulChatAPI then(final Object obj) {
        messageParts.add(new MessagePart(obj.toString()));
        return this;
    }

    /*public String toJSONString() {
        final JSONStringer json = new JSONStringer();
        try {
            if (messageParts.size() == 1) {
                latest().writeJson(json);
            } else {
                json.object().key("text").value("").key("extra").array();
                for (final MessagePart part : messageParts) {
                    part.writeJson(json);
                }
                json.endArray().endObject();
            }
        } catch (final JSONException e) {
            throw new RuntimeException("invalid message");
        }
        return json.toString();
    }*/

    public void send(Player player){
        final PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;
        if (conn == null) {
            return;
        }
        ChatComponentText master = new ChatComponentText("");
        for (MessagePart msg : messageParts) {
            for (IChatBaseComponent cmp : msg.getComponents()) {
                master.addSibling(cmp);
            }
        }
        conn.sendPacket(new PacketPlayOutChat(master));
    }

    protected MessagePart latest() {
        return messageParts.get(messageParts.size() - 1);
    }

    protected String makeMultilineTooltip(final String[] lines) {
        final JSONStringer json = new JSONStringer();
        try {
            json.object().key("id").value(1);
            json.key("tag").object().key("display").object();
            json.key("Name").value("\\u00A7f" + lines[0].replace("\"", "\\\""));
            json.key("Lore").array();
            for (int i = 1; i < lines.length; i++) {
                json.value(lines[i].isEmpty() ? " " : lines[i].replace("\"", "\\\""));
            }
            json.endArray().endObject().endObject().endObject();
        } catch (final JSONException e) {
            throw new RuntimeException("invalid tooltip");
        }
        return json.toString();
    }

    protected void onClick(final String name, final String data) {
        final MessagePart latest = latest();
        latest.clickActionName = name;
        latest.clickActionData = data;
    }

    protected void onHover(final String name, final String data) {
        final MessagePart latest = latest();
        latest.hoverActionName = name;
        latest.hoverActionData = data;
    }
    static final class MessagePart {


        String clickActionName = null, clickActionData = null,
            hoverActionName = null, hoverActionData = null;
        final IChatBaseComponent[] components;

        MessagePart(final String text) {
            components = CraftChatMessage.fromString(text);
        }
        public IChatBaseComponent[] getComponents() {
            for (IChatBaseComponent component : components) {
                if (clickActionName != null) {
                    if (clickActionName.equals("open_file")) {
                        component.getChatModifier().setChatClickable(
                            new ChatClickable(EnumClickAction.OPEN_FILE, clickActionData));
                    } else if (clickActionName.equals("open_url")) {
                        component.getChatModifier().setChatClickable(
                            new ChatClickable(EnumClickAction.OPEN_URL, clickActionData));
                    } else if (clickActionName.equals("suggest_command")) {
                        component.getChatModifier().setChatClickable(
                            new ChatClickable(EnumClickAction.SUGGEST_COMMAND, clickActionData));
                    } else if (clickActionName.equals("run_command")) {
                        component.getChatModifier().setChatClickable(
                            new ChatClickable(EnumClickAction.RUN_COMMAND, clickActionData));
                    }
                }
                if (hoverActionName != null) {
                    final ChatComponentText hover = new ChatComponentText(hoverActionData);
                    if (hoverActionName.equals("show_achievement")) {
                        component.getChatModifier().a(
                            new ChatHoverable(EnumHoverAction.SHOW_ACHIEVEMENT, hover));
                    } else if (hoverActionName.equals("show_item")) {
                        component.getChatModifier().a(
                            new ChatHoverable(EnumHoverAction.SHOW_ITEM, hover));
                    } else if (hoverActionName.equals("show_text")) {
                        component.getChatModifier().a(
                            new ChatHoverable(EnumHoverAction.SHOW_TEXT, hover));
                    }
                }
            }

            return components;
        }
/*
        JSONWriter writeJson(final JSONWriter json) throws JSONException {
            json.object().key("text").value(text);
            if (color != null) {
                json.key("color").value(color.name().toLowerCase());
            }
            if (styles != null) {
                for (final ChatColor style : styles) {
                    json.key(style.name().toLowerCase()).value(true);
                }
            }
            if (clickActionName != null && clickActionData != null) {
                json.key("clickEvent")
                    .object()
                    .key("action").value(clickActionName)
                    .key("value").value(clickActionData)
                    .endObject();
            }
            if (hoverActionName != null && hoverActionData != null) {
                json.key("hoverEvent")
                    .object()
                    .key("action").value(hoverActionName)
                    .key("value").value(hoverActionData)
                    .endObject();
            }
            return json.endObject();
        }
*/
    }
}

