package com.moontegro.auctionhouse.database.impl;

import com.moontegro.auctionhouse.AuctionHouse;
import com.moontegro.auctionhouse.auction.AuctionItem;
import com.moontegro.auctionhouse.database.IDatabase;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class FlatFile implements IDatabase {

    @Override
    public void loadItems() {
        if (AuctionHouse.getInstance().getData().getConfiguration().getConfigurationSection("profiles") == null) return;
        for (final String s : Objects.requireNonNull(AuctionHouse.getInstance().getData().getConfiguration().getConfigurationSection("profiles")).getKeys(false)) {
            for (final String x : Objects.requireNonNull(AuctionHouse.getInstance().getData().getConfiguration().getConfigurationSection("profiles." + s + ".items")).getKeys(false)) {
                UUID uuid = UUID.fromString(Objects.requireNonNull(AuctionHouse.getInstance().getData().getConfiguration().getString("profiles." + s + ".items." + x + ".uuid")));
                ItemStack itemStack = AuctionHouse.getInstance().getData().getConfiguration().getItemStack("profiles." + s + ".items." + x + ".item");
                long price = AuctionHouse.getInstance().getData().getConfiguration().getLong("profiles." + s + ".items." + x + ".price");

                AuctionHouse.getInstance().getAuctionItemManager().getPlayerItems().putIfAbsent(UUID.fromString(s), new ArrayList<>());
                AuctionHouse.getInstance().getAuctionItemManager().getPlayerItems().get(UUID.fromString(s)).add(new AuctionItem(uuid, UUID.fromString(s), itemStack, price));
            }
        }
    }

    @Override
    public void saveItems() {
        for (UUID uuid : AuctionHouse.getInstance().getAuctionItemManager().getPlayerItems().keySet()) {
            for (AuctionItem auctionItem : AuctionHouse.getInstance().getAuctionItemManager().getPlayerItems().get(uuid)) {
                AuctionHouse.getInstance().getData().getConfiguration().set("profiles." + uuid.toString() + ".items." + auctionItem.getUuid().toString() + ".uuid", auctionItem.getUuid().toString());
                AuctionHouse.getInstance().getData().getConfiguration().set("profiles." + uuid + ".items." + auctionItem.getUuid().toString() + ".item", auctionItem.getItemStack());
                AuctionHouse.getInstance().getData().getConfiguration().set("profiles." + uuid + ".items." + auctionItem.getUuid().toString() + ".price", auctionItem.getPrice());
                AuctionHouse.getInstance().getData().save();
            }
        }
    }
}
