package dev.dvxq.blockclock;

import dev.dvxq.blockclock.config.GeneralConfig;
import dev.dvxq.blockclock.placement.*;
import dev.dvxq.blockclock.util.ClockManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public final class BlockClock extends JavaPlugin {
    private GeneralConfig config;
    private ClockManager clockManager;
    private HourFirstStrategy hourFirstStrategy;
    private HourSecondStrategy hourSecondStrategy;
    private MinuteFirstStrategy minuteFirstStrategy;
    private MinuteSecondStrategy minuteSecondStrategy;
    private SecondFirstStrategy secondFirstStrategy;
    private SecondSecondStrategy secondSecondStrategy;
    @Override
    public void onEnable() {
        ensurePluginExistence();
        ensureSchemsExistence();
        copyAndSyncDataFolder();
        saveDefaultConfig();

        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        config = new GeneralConfig(getConfig(), this);


        clockManager = new ClockManager(this, config);
        hourFirstStrategy = new HourFirstStrategy(clockManager, config);
        hourSecondStrategy = new HourSecondStrategy(clockManager, config);
        minuteFirstStrategy = new MinuteFirstStrategy(clockManager, config);
        minuteSecondStrategy = new MinuteSecondStrategy(clockManager, config);
        secondFirstStrategy = new SecondFirstStrategy(clockManager, config);
        secondSecondStrategy = new SecondSecondStrategy(clockManager, config);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            clockManager.placeClock();
        }, 0L, 20L);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    private void ensurePluginExistence() {
        PluginManager pm = getServer().getPluginManager();
        Plugin worldEdit = pm.getPlugin("WorldEdit");
        if (worldEdit == null || !worldEdit.isEnabled()) {
            getLogger().warning("Failed to load plugin, WorldEdit must be enabled");
            pm.disablePlugin(this);
        }
    }
    private void copyAndSyncDataFolder() {
        File targetFolder = new File(getDataFolder(), "data");
        getLogger().info("Checking 'data' folder at " + targetFolder.getAbsolutePath());

        if (!targetFolder.exists()) {
            getLogger().info("Data folder not found, creating and copying from resources...");
            try {
                targetFolder.mkdirs();
                copyFolderFromResources("data", targetFolder);
                getLogger().info("Initial copy of 'data' folder completed.");
            } catch (IOException e) {
                getLogger().severe("Failed to create and copy 'data' folder: " + e.getMessage());
                return;
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            syncMissingFiles("data", targetFolder);
            getLogger().info("Data folder synchronization completed.");
        } catch (IOException e) {
            getLogger().severe("Failed to sync 'data' folder: " + e.getMessage());
        } catch (URISyntaxException e) {
            getLogger().severe("Failed to sync 'data' folder: " + e.getMessage());
        }
    }
    private void syncMissingFiles(String resourcePath, File targetFolder) throws IOException, URISyntaxException {
        File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        try (JarFile jar = new JarFile(jarFile)) {
            var resourceFiles = jar.stream()
                    .filter(entry -> !entry.isDirectory() && entry.getName().startsWith(resourcePath + "/"))
                    .map(entry -> entry.getName().substring(resourcePath.length() + 1))
                    .collect(Collectors.toSet());

            for (String resourceFile : resourceFiles) {
                File targetFile = new File(targetFolder, resourceFile);
                if (!targetFile.exists()) {
                    getLogger().info("Missing file detected: " + resourceFile + ", copying from resources...");
                    try (var inputStream = jar.getInputStream(jar.getEntry(resourcePath + "/" + resourceFile))) {
                        Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        getLogger().info("Copied " + resourceFile + " to " + targetFolder.getAbsolutePath());
                    } catch (IOException e) {
                        getLogger().warning("Failed to copy " + resourceFile + ": " + e.getMessage());
                    }
                }
            }
        }
    }
    private void ensureSchemsExistence() {
        File dataFolder = new File(getDataFolder(), "schems");
        if (!dataFolder.exists()) {
            try {
                dataFolder.mkdirs();
                copyFolderFromResources("schems", dataFolder);
            } catch (IOException e) {
                getLogger().severe("Failed to copy data folder: " + e.getMessage());
            } catch (URISyntaxException e) {}
        }
    }
    private void copyFolderFromResources(String resourcePath, File targetFolder) throws IOException, URISyntaxException {
        File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        try (JarFile jar = new JarFile(jarFile)) {
            jar.stream()
                    .filter(entry -> entry.getName().startsWith(resourcePath + "/") || entry.getName().equals(resourcePath))
                    .forEach(entry -> {
                        String entryName = entry.getName().substring(resourcePath.length() + 1);
                        File targetFile = new File(targetFolder, entryName);

                        if (entry.isDirectory()) {
                            targetFile.mkdirs();
                        } else {
                            try {
                                Files.copy(jar.getInputStream(entry), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException e) {
                                getLogger().warning("Failed to copy " + entryName + ": " + e.getMessage());
                            }
                        }
                    });
        }
    }
    @Override
    public void saveDefaultConfig() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveResource("config.yml", false);
        }
    }
}
