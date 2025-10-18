package com.moontegro.auctionhouse.database.impl;

import com.moontegro.auctionhouse.AuctionHouse;
import com.moontegro.auctionhouse.auction.AuctionItem;
import com.moontegro.auctionhouse.database.IDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class MySQL implements IDatabase {

    @Override
    public void loadItems() {
        try (PreparedStatement preparedStatement = AuctionHouse.getInstance().getMySQLManager().getHikariDataSource().getConnection().prepareStatement("SELECT * FROM items")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                    List<AuctionItem> items = AuctionHouse.getInstance().getAuctionItemManager().fromDatabase(uuid, resultSet.getString("items"));

                    AuctionHouse.getInstance().getAuctionItemManager().getPlayerItems().put(uuid, items);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveItems() {
        for (UUID uuid : AuctionHouse.getInstance().getAuctionItemManager().getPlayerItems().keySet()) {
            try (PreparedStatement preparedStatement = AuctionHouse.getInstance().getMySQLManager().getHikariDataSource().getConnection()
                    .prepareStatement("INSERT INTO items(uuid, items) VALUES (?,?) ON DUPLICATE KEY UPDATE items = ?")) {
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setString(2, AuctionHouse.getInstance().getAuctionItemManager().toDatabase(uuid));
                preparedStatement.setString(3, AuctionHouse.getInstance().getAuctionItemManager().toDatabase(uuid));
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
