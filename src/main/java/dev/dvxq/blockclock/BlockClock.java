package dev.dvxq.blockclock;

import dev.dvxq.blockclock.commands.UpdateClockCommand;
import dev.dvxq.blockclock.commands.tes;
import dev.dvxq.blockclock.config.GeneralConfig;
import dev.dvxq.blockclock.placement.HourFirstStrategy;
import dev.dvxq.blockclock.placement.HourSecondStrategy;
import dev.dvxq.blockclock.placement.MinuteFirstStrategy;
import dev.dvxq.blockclock.placement.MinuteSecondStrategy;
import dev.dvxq.blockclock.util.ClockManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class BlockClock extends JavaPlugin {
    private GeneralConfig config;
    private ClockManager clockManager;
    private HourFirstStrategy hourFirstStrategy;
    private HourSecondStrategy hourSecondStrategy;
    private MinuteFirstStrategy minuteFirstStrategy;
    private MinuteSecondStrategy minuteSecondStrategy;
    @Override
    public void onEnable() {
        ensurePluginExistence();
        saveDefaultConfig();

        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        config = new GeneralConfig(getConfig(), this);


        clockManager = new ClockManager(this, config);
        hourFirstStrategy = new HourFirstStrategy(clockManager, config);
        hourSecondStrategy = new HourSecondStrategy(clockManager, config);
        minuteFirstStrategy = new MinuteFirstStrategy(clockManager, config);
        minuteSecondStrategy = new MinuteSecondStrategy(clockManager, config);

        getCommand("test").setExecutor(new tes(clockManager));
        getCommand("updatec").setExecutor(new UpdateClockCommand(clockManager));
    }

    @Override
    public void onDisable() {
    }

    private void ensurePluginExistence() {
        PluginManager pm = getServer().getPluginManager();
        Plugin worldEdit = pm.getPlugin("WorldEdit");
        if (worldEdit == null || !worldEdit.isEnabled()) {
            getLogger().warning("Failed to load plugin, WorldEdit must be enabled");
            pm.disablePlugin(this);
        }
    }

    @Override
    public void saveDefaultConfig() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveResource("config.yml", false);
        }
    }
}
