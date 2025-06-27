package dev.dvxq.blockclock.commands;

import dev.dvxq.blockclock.config.GeneralConfig;
import dev.dvxq.blockclock.placement.HourFirstStrategy;
import dev.dvxq.blockclock.util.ClockManager;
import dev.dvxq.blockclock.util.ClockNumber;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class UpdateClockCommand implements CommandExecutor {
    private final ClockManager clockManager;

    public UpdateClockCommand(ClockManager clockManager) {
        this.clockManager = clockManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        clockManager.placeClock();
        return true;
    }
}
