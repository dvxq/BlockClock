package dev.dvxq.blockclock.commands;

import dev.dvxq.blockclock.placement.PlacementStrategy;
import dev.dvxq.blockclock.util.ClockManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class tes implements CommandExecutor {
    private final ClockManager clockManager;

    public tes(ClockManager clockManager) {
        this.clockManager = clockManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(clockManager.getDigit(ClockManager.TimeDigit.HOUR_FIRST)
                + clockManager.getDigit(ClockManager.TimeDigit.HOUR_SECOND)
                + ":"
                + clockManager.getDigit(ClockManager.TimeDigit.MINUTE_FIRST)
                + clockManager.getDigit(ClockManager.TimeDigit.MINUTE_SECOND));
        return true;
    }
}
