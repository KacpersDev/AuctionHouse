package com.moontegro.auctionhouse.command;

import com.moontegro.auctionhouse.AuctionHouse;
import com.moontegro.auctionhouse.auction.AuctionItem;
import com.moontegro.auctionhouse.utils.color.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class TradeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        if (args.length == 0) {
            usage(player);
        } else {
            try {
                int number = Integer.parseInt(args[0]);

                if (player.getInventory().getItemInMainHand() == null) {
                    player.sendMessage(Color.translate(AuctionHouse.getInstance().getLanguage()
                            .getConfiguration().getString("item-null")));
                    return false;
                }

                AuctionHouse.getInstance().getAuctionItemManager().getPlayerItems().putIfAbsent(player.getUniqueId(), new ArrayList<>());
                AuctionHouse.getInstance().getAuctionItemManager().getPlayerItems().get(player.getUniqueId()).add(new AuctionItem(UUID.randomUUID(), player.getUniqueId(), player.getInventory().getItemInMainHand(), number));
                player.sendMessage(Color.translate(Objects.requireNonNull(AuctionHouse.getInstance().getLanguage().getConfiguration().getString("auction-item-added"))));
            } catch (NumberFormatException e) {
                player.sendMessage(Color.translate("&cAmount must be a number."));
            }
        }

        return true;
    }

    private void usage(Player player) {
        for (final String s : AuctionHouse.getInstance().getLanguage().getConfiguration().getStringList("trade-usage")) {
            player.sendMessage(Color.translate(s));
        }
    }
}
