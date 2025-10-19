package com.moontegro.auctionhouse.auction.manager;

import com.moontegro.auctionhouse.auction.AuctionItem;
import com.moontegro.auctionhouse.utils.itemstack.ItemStackSerializer;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class AuctionItemManager {

    private final Map<UUID, List<AuctionItem>> playerItems = new HashMap<>();

    public String toDatabase(UUID uuid) {
        String items = "";
        for (AuctionItem auctionItem : playerItems.get(uuid)) {
            if (items.isEmpty()) {
                items = auctionItem.toString();
            } else {
                items = items + ":" + auctionItem.toString();
            }
        }

        return items;
    }

    public List<AuctionItem> fromDatabase(UUID uuid, String items) {
        List<AuctionItem> auctionItems = new ArrayList<>();
        for (String item : items.split(":")) {
            UUID itemUUID = UUID.fromString(item.split("-")[0]);
            ItemStack itemStack = ItemStackSerializer.deSerialize(item.split("-")[0]);
            long price = Long.parseLong(item.split("-")[0]);

            auctionItems.add(new AuctionItem(itemUUID, uuid, itemStack, price));
        }

        return auctionItems;
    }

    public AuctionItem getById(UUID itemId) {
        for (UUID uuid : playerItems.keySet()) {
            for (AuctionItem item : playerItems.get(uuid)) {
                if (item.getUuid().equals(itemId)) {
                    return item;
                }
            }
        }

        return null;
    }
}
