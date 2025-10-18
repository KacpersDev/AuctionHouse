package com.moontegro.auctionhouse.auction;

import com.moontegro.auctionhouse.utils.itemstack.ItemStackSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AuctionItem {

    private UUID uuid;
    private ItemStack itemStack;
    private long price;

    public String toString() {
        return uuid.toString() + "-" + ItemStackSerializer.serialize(itemStack) + "-" + price;
    }
}
