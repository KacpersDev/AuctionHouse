package com.moontegro.auctionhouse;

import com.moontegro.auctionhouse.auction.manager.AuctionItemManager;
import com.moontegro.auctionhouse.command.AuctionHouseCommand;
import com.moontegro.auctionhouse.command.TradeCommand;
import com.moontegro.auctionhouse.database.IDatabase;
import com.moontegro.auctionhouse.database.impl.FlatFile;
import com.moontegro.auctionhouse.database.impl.Mongo;
import com.moontegro.auctionhouse.database.impl.MySQL;
import com.moontegro.auctionhouse.database.manager.MongoManager;
import com.moontegro.auctionhouse.database.manager.MySQLManager;
import com.moontegro.auctionhouse.listener.AuctionHouseListener;
import com.moontegro.auctionhouse.utils.config.Config;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

@Getter
public final class AuctionHouse extends JavaPlugin {

    @Getter
    private static AuctionHouse instance;

    private Config configuration, language, data;

    @Getter
    private static Economy econ = null;

    private AuctionItemManager auctionItemManager;
    private MongoManager mongoManager;
    private MySQLManager mySQLManager;

    private final NamespacedKey price = new NamespacedKey(this, "price");
    private final NamespacedKey uuid = new NamespacedKey(this, "uuid");

    private IDatabase database;

    @Override
    public void onEnable() {
        instance = this;

        this.loadConfiguration();
        this.loadCommand();
        this.loadListener();

        this.auctionItemManager = new AuctionItemManager();

        this.loadDatabase();
        this.database.loadItems();

        this.setupEconomy();
    }

    @Override
    public void onDisable() {
        this.database.saveItems();

        instance = null;
    }

    private void loadConfiguration() {
        this.configuration = new Config(this, new File(getDataFolder(), "configuration.yml"),
                new YamlConfiguration(), "configuration.yml");
        this.language = new Config(this, new File(getDataFolder(), "language.yml"),
                new YamlConfiguration(), "language.yml");
        this.data = new Config(this, new File(getDataFolder(), "data.yml"),
                new YamlConfiguration(), "data.yml");

        this.configuration.create();
        this.language.create();
        this.data.create();
    }

    private void loadCommand() {
        Objects.requireNonNull(getCommand("trade")).setExecutor(new TradeCommand());
        Objects.requireNonNull(getCommand("auctionhouse")).setExecutor(new AuctionHouseCommand());
    }

    private void loadListener() {
        Bukkit.getPluginManager().registerEvents(new AuctionHouseListener(), this);
    }

    private void loadDatabase() {
        String configBase = getConfiguration().getConfiguration().getString("database");
        if (configBase == null) {
            database = new FlatFile();
            return;
        }

        if (configBase.equalsIgnoreCase("mysql")) {
            database = new MySQL();
            this.mySQLManager = new MySQLManager();
        } else if (configBase.equalsIgnoreCase("mongo")) {
            database = new Mongo();
            this.mongoManager = new MongoManager();
        } else {
            database = new FlatFile();
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
