package dev.dvxq.blockclock.placement;

import dev.dvxq.blockclock.config.GeneralConfig;
import dev.dvxq.blockclock.util.ClockManager;

public class HourFirstStrategy implements PlacementStrategy {
    private final ClockManager clockManager;
    private final GeneralConfig config;

    public HourFirstStrategy(ClockManager clockManager, GeneralConfig config) {
        this.clockManager = clockManager;
        this.config = config;
    }

    @Override
    public void place() {
        int number = clockManager.getDigit(ClockManager.TimeDigit.HOUR_FIRST);
        if (number < 3 && number > -1) clockManager.place(config.getHourFirst(), number);
    }
}
