package dev.dvxq.blockclock.config;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import dev.dvxq.blockclock.BlockClock;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class GeneralConfig {
    private final FileConfiguration config;
    private final BlockClock plugin;

    public World world;
    public Location hourFirst;
    public Location hourSecond;
    public Location minuteFirst;
    public Location minuteSecond;
    public Location secondFirst;
    public Location secondSecond;
    private org.bukkit.World bukkitWorld;

    public GeneralConfig(FileConfiguration config, BlockClock plugin) {
        this.config = config;
        this.plugin = plugin;
        parseConfigValues();
    }

    private void parseConfigValues() {
        String worldName = config.getString("coordinates.world");
        if (worldName == null) {
            plugin.getLogger().warning("World not found in config, using default 'world'");
            worldName = "world";
        }
        bukkitWorld = Bukkit.getWorld(worldName);
        if (bukkitWorld == null || bukkitWorld.toString().isEmpty()) {
            plugin.getLogger().warning("bukkitWorld is null, \"world\" was adapted as WE world");
            world = BukkitAdapter.adapt(Bukkit.getWorld("world"));
        } else world = BukkitAdapter.adapt(bukkitWorld);

        String[] coordinateKeys = {
                "hour-first", "hour-second", "minute-first", "minute-second", "second-first", "second-second"
        };

        for (String key : coordinateKeys) {
            parseCoordinates(key);
        }
    }

    private void parseCoordinates(String key) {
        String path = "coordinates." + key;
        String[] coords = config.getString(path).split(" ");
        if (coords.length == 3) {
            try {
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                int z = Integer.parseInt(coords[2]);

                switch (key) {
                    case "hour-first": {
                        hourFirst = new Location(bukkitWorld, x, y, z);
                        break;
                    }
                    case "hour-second": {
                        hourSecond = new Location(bukkitWorld, x, y, z);
                        break;
                    }
                    case "minute-first": {
                        minuteFirst = new Location(bukkitWorld, x, y, z);
                        break;
                    }
                    case "minute-second": {
                        minuteSecond = new Location(bukkitWorld, x, y, z);
                        break;
                    }
                    case "second-first": {
                        secondFirst = new Location(bukkitWorld, x, y, z);
                        break;
                    }
                    case "second-second": {
                        secondSecond = new Location(bukkitWorld, x, y, z);
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                plugin.getLogger().warning("Invalid format for " + key + " coordinates");
            }
        } else {
            plugin.getLogger().warning(key + " should have 3 values (x y z)");
        }
    }
}
