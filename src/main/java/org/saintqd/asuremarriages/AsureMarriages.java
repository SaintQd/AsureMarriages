package org.saintqd.asuremarriages;

import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.saintqd.asurelib.AsureLib;
import org.saintqd.asurelib.utils.AsureUtils;
import org.saintqd.asurelib.utils.ResourceUtils;
import org.saintqd.asuremarriages.commands.MarryCommandsManager;
import org.saintqd.asuremarriages.listeners.PlayerListener;
import org.saintqd.asuremarriages.managers.MarriedPlayersManager;
import org.saintqd.asuremarriages.placeholders.AsureMarriagesPlaceholders;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AsureMarriages extends JavaPlugin {

    private static AsureMarriages plugin;
    private MarriedPlayersManager marriedPlayersManager;
    private AsureMarriagesPlaceholders placeholders = null;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        try {
            ResourceUtils.fetchAllResources(this,getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.marriedPlayersManager = new MarriedPlayersManager();

        loadData();

        MarryCommandsManager.setupCommands(this);

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        // Подключаем плейсхолдеры
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholders = new AsureMarriagesPlaceholders(this);
            placeholders.register();
        } else {
            placeholders = null;
            AsureUtils.sendDebugMessage(0,"<yellow>Could not find PlaceholderAPI! Placeholders won't be registered.");
        }

        //Создаем задачу регулярного сохранения данных раз в полчаса
        getServer().getScheduler().scheduleSyncRepeatingTask(this, this::saveData, 36000L, 36000L);
    }

    @Override
    public void onDisable() {
        saveData();
        AsureUtils.updateJarFile(this,this.getFile());
    }

    public void loadData() {
        reloadConfig();

        String selectedLang = getConfig().getString("Marriages.Language");
        HashMap<Key,String> langLines = AsureLib.inst().getLangManager().loadLanguageFile(this,
                plugin.getDataFolder().getPath() + File.separator + "lang" + File.separator + selectedLang + ".yml");
        AsureLib.inst().getLangManager().registerLangLines(langLines);

        long startTime = System.currentTimeMillis();
        long prevTime = startTime;

        marriedPlayersManager.updateParams(this);
        marriedPlayersManager.loadMarriedPlayerNames(this);
        marriedPlayersManager.getTimers().clear();
        long time = System.currentTimeMillis();
        AsureUtils.sendDebugMessage(0,"Loaded " + marriedPlayersManager.getMarriedPlayerNames().size() + " married players. ("+(time-prevTime)+" ms)");
        prevTime = System.currentTimeMillis();
    }

    public void saveData() {
        AsureUtils.sendDebugMessage(0,"Saving married players data...");
        marriedPlayersManager.saveMarriedPlayerData(this);
        AsureUtils.sendDebugMessage(0,"Saved "+marriedPlayersManager.getMarriedPlayerNames().size()+" married players.");
    }

    public static AsureMarriages inst() {
        return plugin;
    }

    public MarriedPlayersManager getMarriedPlayersManager() {
        return marriedPlayersManager;
    }


}
