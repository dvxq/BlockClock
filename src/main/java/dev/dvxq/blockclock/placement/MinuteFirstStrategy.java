package dev.dvxq.blockclock.placement;

import dev.dvxq.blockclock.config.GeneralConfig;
import dev.dvxq.blockclock.util.ClockManager;

public class MinuteFirstStrategy implements PlacementStrategy {
    private final ClockManager clockManager;
    private final GeneralConfig config;

    public MinuteFirstStrategy(ClockManager clockManager, GeneralConfig config) {
        this.clockManager = clockManager;
        this.config = config;
    }

    @Override
    public void place() {
        int number = clockManager.getDigit(ClockManager.TimeDigit.MINUTE_FIRST);
        if (number > -1 && number < 6) clockManager.place(config.getMinuteFirst(), number);
    }
}
