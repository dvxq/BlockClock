package dev.dvxq.blockclock.placement;

import dev.dvxq.blockclock.config.GeneralConfig;
import dev.dvxq.blockclock.util.ClockManager;

public class MinuteSecondStrategy implements PlacementStrategy {
    private final GeneralConfig config;
    private final ClockManager clockManager;
    public MinuteSecondStrategy(ClockManager clockManager, GeneralConfig config) {
        this.clockManager = clockManager;
        this.config = config;
    }

    @Override
    public void place() {
        int number = clockManager.getDigit(ClockManager.TimeDigit.MINUTE_SECOND);
//        if (number > -1 && number < 10)
            clockManager.place(config.getHourSecond(), number);
    }
}
