package com.empireminecraft.cbmisc;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;

public class HiddenItemMeta {
    public static NBTTagCompound filterItemLore(NBTTagCompound nbttagcompound, boolean storeOriginal) {
        if (nbttagcompound != null && nbttagcompound.hasKey("display")) {
            NBTTagCompound display = nbttagcompound.getCompound("display");
            if (display.hasKey("Lore")) {
                NBTTagList lore = display.getList("Lore", 8);
                int lastLine = 0;
                boolean hasSpecial = false;
                boolean hasShiny = false;
                for (int i = 0; i < lore.size(); i++) {
                    String line = lore.getString(i);

                    if (line.equals("&&::META")) {
                        hasSpecial = true;
                        break;
                    } else if (!line.isEmpty()) {
                        if (line.equals("&&::SHINY")) {
                            hasShiny = true;
                            hasSpecial = true;
                            break;
                        } else {
                            lastLine = i+1;
                        }
                    }
                }
                if (hasSpecial) {
                    NBTTagList newlore = new NBTTagList();
                    for (int x = 0; x < lastLine; x++) {
                        newlore.add(new NBTTagString(lore.getString(x)));
                    }

                    nbttagcompound = (NBTTagCompound) nbttagcompound.clone();
                    if (hasShiny && !nbttagcompound.hasKey("ench")) {
                        nbttagcompound.set("ench" , new NBTTagList());
                    }
                    display = nbttagcompound.getCompound("display");
                    display.set("Lore", newlore);
                    if (storeOriginal) {
                        display.set("OriginalLore", lore);
                    }
                }
            }
        }
        return nbttagcompound;
    }
}
