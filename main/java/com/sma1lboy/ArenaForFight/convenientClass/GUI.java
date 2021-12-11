package com.sma1lboy.ArenaForFight.convenientClass;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUI {
    Inventory gui;
//    int guiSize;
//
//    ItemStack


    private Inventory getGui() {
        return this.gui;
    }
    private ItemStack setButton(Material icon, int position) {
        ItemStack button = new ItemStack(icon);
        ItemMeta buttonMeta = setButtonMeta(button, Enchantment.ARROW_FIRE);

        return button;
    }
    private ItemMeta setButtonMeta(ItemStack button, Enchantment enchantment) {
        ItemMeta res = button.getItemMeta();
        res.addEnchant(enchantment,1,true);
        res.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);

        return null;
    }
}
