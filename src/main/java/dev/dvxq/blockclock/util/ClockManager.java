package dev.dvxq.blockclock.util;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import dev.dvxq.blockclock.BlockClock;
import dev.dvxq.blockclock.config.GeneralConfig;
import dev.dvxq.blockclock.placement.*;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClockManager {
    private final BlockClock plugin;
    private final GeneralConfig config;
    public ClockManager(BlockClock plugin, GeneralConfig config) {
        this.plugin = plugin;
        this.config = config;
    }

    public void place(Location location, int number) {
        World world = BukkitAdapter.adapt(location.getWorld());

        Clipboard clipboard = null;
        File file = new File(new File(plugin.getDataFolder(), "schems"), number + ".schem");
        ClipboardFormat format = ClipboardFormats.findByFile(file);

        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();
        } catch (FileNotFoundException e) {
            plugin.getLogger().warning("Schem not found");
        } catch (IOException e) {
            plugin.getLogger().warning("Error while reading schem: " + e.getMessage());
        }

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ()))
                    .build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }
    public void placeClock() {
        List<ClockNumber> strategies = Arrays.asList(
                new ClockNumber(new HourFirstStrategy(this, config)),
                new ClockNumber(new HourSecondStrategy(this, config)),
                new ClockNumber(new MinuteFirstStrategy(this, config)),
                new ClockNumber(new MinuteSecondStrategy(this, config)),
                new ClockNumber(new SecondFirstStrategy(this, config)),
                new ClockNumber(new SecondSecondStrategy(this, config))
        );

        for (ClockNumber number : strategies) {
            number.build();
        }
    }

    public int getDigit(TimeDigit digitType) {
        LocalTime currentTime = LocalTime.now();
        int hours = currentTime.getHour();
        int minutes = currentTime.getMinute();
        int seconds = currentTime.getSecond();

        int hourFirst = hours / 10;
        int hourSecond = hours % 10;
        int minuteFirst = minutes / 10;
        int minuteSecond = minutes % 10;
        int secondFirst = seconds / 10;
        int secondSecond = seconds % 10;

        switch (digitType) {
            case HOUR_FIRST: return hourFirst;
            case HOUR_SECOND: return hourSecond;
            case MINUTE_FIRST: return minuteFirst;
            case MINUTE_SECOND: return minuteSecond;
            case SECOND_FIRST: return secondFirst;
            case SECOND_SECOND: return secondSecond;
            default:
                plugin.getLogger().warning("Unknown digit type: " + digitType);
                return 0;
        }
    }
    public enum TimeDigit {
        HOUR_FIRST,
        HOUR_SECOND,
        MINUTE_FIRST,
        MINUTE_SECOND,
        SECOND_FIRST,
        SECOND_SECOND
    }
}
