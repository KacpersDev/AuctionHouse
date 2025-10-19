package com.moontegro.auctionhouse.auction;

import com.moontegro.auctionhouse.AuctionHouse;
import com.moontegro.auctionhouse.utils.color.Color;
import com.moontegro.auctionhouse.utils.itemstack.ItemStackSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AuctionItem {

    private UUID uuid, owner;
    private ItemStack itemStack;
    private long price;

    public String toString() {
        return uuid.toString() + "-" + ItemStackSerializer.serialize(itemStack) + "-" + price;
    }

    public ItemStack getItemStack(Player viewer) {
        ItemStack item = itemStack.clone();
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(AuctionHouse.getInstance().getPrice(), PersistentDataType.LONG, price);
        meta.getPersistentDataContainer().set(AuctionHouse.getInstance().getPrice(), PersistentDataType.STRING, uuid.toString());
        List<Component> lore = itemStack.clone().getItemMeta().lore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(Color.translate(" "));
        lore.add(Color.translate("&6Price&7:&f " + price));
        lore.add(Color.translate("&aLeft click to purchase"));
        if (viewer.isOp()) {
            lore.add(Color.translate("&cRight click to remove"));
        }
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
