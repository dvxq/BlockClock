package dev.dvxq.blockclock.placement;

import dev.dvxq.blockclock.config.GeneralConfig;
import dev.dvxq.blockclock.util.ClockManager;

public class SecondFirstStrategy implements PlacementStrategy{
    private final GeneralConfig config;
    private final ClockManager clockManager;
    public SecondFirstStrategy(ClockManager clockManager, GeneralConfig config) {
        this.clockManager = clockManager;
        this.config = config;
    }

    @Override
    public void place() {
        int number = clockManager.getDigit(ClockManager.TimeDigit.SECOND_FIRST);
//        if (number > -1 && number < 10)
            clockManager.place(config.getSecondFirst(), number);
    }
}
