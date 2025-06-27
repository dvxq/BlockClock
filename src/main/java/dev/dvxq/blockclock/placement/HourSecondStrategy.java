package dev.dvxq.blockclock.placement;

import dev.dvxq.blockclock.config.GeneralConfig;
import dev.dvxq.blockclock.util.ClockManager;

public class HourSecondStrategy implements PlacementStrategy {
    private final ClockManager clockManager;
    private final GeneralConfig config;

    public HourSecondStrategy(ClockManager clockManager, GeneralConfig config) {
        this.clockManager = clockManager;
        this.config = config;
    }

    @Override
    public void place() {
        int number = clockManager.getDigit(ClockManager.TimeDigit.HOUR_SECOND);
        if (number > -1 && number < 10) clockManager.place(config.getHourSecond(), number);
    }
}
