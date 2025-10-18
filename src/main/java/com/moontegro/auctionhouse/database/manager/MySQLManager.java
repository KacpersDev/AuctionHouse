package com.moontegro.auctionhouse.database.manager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
public class MySQLManager {

    private final HikariDataSource hikariDataSource;

    public MySQLManager() {
        HikariConfig config = new HikariConfig();

        this.hikariDataSource = new HikariDataSource(config);
        init();
    }

    private void init() {
        try (PreparedStatement preparedStatement = getHikariDataSource().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS items (UUID varchar(36), items TEXT)")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
