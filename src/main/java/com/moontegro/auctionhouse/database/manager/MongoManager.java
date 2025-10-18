package com.moontegro.auctionhouse.database.manager;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.moontegro.auctionhouse.AuctionHouse;
import lombok.Getter;
import org.bson.Document;

import java.util.Objects;

@Getter
public class MongoManager {

    private final MongoCollection<Document> items;

    public MongoManager() {
        MongoClient mongoClient = MongoClients.create(new ConnectionString(Objects.requireNonNull(AuctionHouse.getInstance()
                .getConfiguration().getConfiguration().getString("mongo-uri"))));
        MongoDatabase mongoDatabase = mongoClient.getDatabase("auctions");
        this.items = mongoDatabase.getCollection("items");
    }
}
