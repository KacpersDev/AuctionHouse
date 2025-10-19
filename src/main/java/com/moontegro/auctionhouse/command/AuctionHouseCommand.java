package com.moontegro.auctionhouse.command;

import com.moontegro.auctionhouse.AuctionHouse;
import com.moontegro.auctionhouse.auction.AuctionItem;
import com.moontegro.auctionhouse.utils.color.Color;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class AuctionHouseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        Inventory inventory = Bukkit.createInventory(player, 54,
                Color.translate(Objects.requireNonNull(AuctionHouse.getInstance().getConfiguration().getConfiguration().getString("auction-inventory.title"))));

        int max = 45;
        int currentSlot = 9;

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS));
        }

        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS));
        }

        for (List<AuctionItem> auctionItems : AuctionHouse.getInstance().getAuctionItemManager().getPlayerItems().values()) {
            for (AuctionItem auctionItem : auctionItems) {
                ItemStack item = auctionItem.getItemStack(player);
                inventory.setItem(currentSlot, item);
                currentSlot++;

                if (currentSlot > max) {
                    break;
                }
            }
        }

        player.openInventory(inventory);

        return true;
    }
}
