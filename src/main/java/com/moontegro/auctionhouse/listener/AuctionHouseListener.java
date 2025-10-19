package com.moontegro.auctionhouse.listener;

import com.moontegro.auctionhouse.AuctionHouse;
import com.moontegro.auctionhouse.auction.AuctionItem;
import com.moontegro.auctionhouse.utils.color.Color;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class AuctionHouseListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (MiniMessage.miniMessage().serialize(event.getView().title())
                .equalsIgnoreCase(AuctionHouse.getInstance().getConfiguration().getConfiguration().getString("auction-inventory.title"))
                || LegacyComponentSerializer.legacyAmpersand().serialize(event.getView().title()).equalsIgnoreCase(AuctionHouse.getInstance()
                .getConfiguration().getConfiguration().getString("auction-inventory.title"))) {
            event.setCancelled(true);
        }

        if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null || !event.getCurrentItem().getItemMeta().getPersistentDataContainer()
                .has(AuctionHouse.getInstance().getPrice())) return;

        long price = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(AuctionHouse.getInstance().getPrice(), PersistentDataType.LONG);
        UUID itemId = UUID.fromString(event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(AuctionHouse.getInstance().getUuid(), PersistentDataType.STRING));

        AuctionItem auctionItem = AuctionHouse.getInstance().getAuctionItemManager().getById(itemId);
        if (auctionItem == null) {
            player.closeInventory();
            player.performCommand("auctionhouse"); // IN CASE
            return;
        }

        if (event.isLeftClick()) {
            if (!AuctionHouse.getEcon().has(player, price)) {
                player.sendMessage(Color.translate(AuctionHouse.getInstance().getLanguage().getConfiguration().getString("insufficient-balance")));
                return;
            }

            AuctionHouse.getEcon().withdrawPlayer(player, price);
            AuctionHouse.getEcon().depositPlayer(Bukkit.getOfflinePlayer(auctionItem.getOwner()), price);
            player.getInventory().addItem(auctionItem.getItemStack());
            AuctionHouse.getInstance().getAuctionItemManager().getPlayerItems().get(auctionItem.getOwner()).remove(auctionItem);
            player.closeInventory();
            player.sendMessage(Color.translate(AuctionHouse.getInstance().getLanguage().getConfiguration().getString("auction-purchased")));
        } else if (event.isRightClick()) {
            if (!player.isOp()) return;
            AuctionHouse.getInstance().getAuctionItemManager().getPlayerItems().get(auctionItem.getOwner()).remove(auctionItem);
            player.closeInventory();
            player.sendMessage(Color.translate(AuctionHouse.getInstance().getLanguage().getConfiguration().getString("auction-admin-remove")));
        }
    }
}
