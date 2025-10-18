package com.moontegro.auctionhouse.database.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.moontegro.auctionhouse.AuctionHouse;
import com.moontegro.auctionhouse.auction.AuctionItem;
import com.moontegro.auctionhouse.database.IDatabase;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

public class Mongo implements IDatabase {

    @Override
    public void loadItems() {
        FindIterable<Document> iterable = AuctionHouse.getInstance().getMongoManager().getItems().find();
        try (MongoCursor<Document> cursor = iterable.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();

                UUID uuid = UUID.fromString(document.getString("uuid"));
                List<AuctionItem> auctionItems = AuctionHouse.getInstance().getAuctionItemManager().fromDatabase(uuid, document.getString("items"));

                AuctionHouse.getInstance().getAuctionItemManager().getPlayerItems().put(uuid, auctionItems);
            }
        }
    }

    @Override
    public void saveItems() {
        for (UUID uuid : AuctionHouse.getInstance().getAuctionItemManager().getPlayerItems().keySet()) {
            Document document = new Document();
            document.put("uuid", uuid.toString());
            document.put("items", AuctionHouse.getInstance().getAuctionItemManager().toDatabase(uuid));

            AuctionHouse.getInstance().getMongoManager().getItems().replaceOne(Filters.eq("uuid", uuid), document,
                    new ReplaceOptions().upsert(true));
        }
    }
}
