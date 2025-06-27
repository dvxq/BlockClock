package dev.dvxq.blockclock.util;

import dev.dvxq.blockclock.placement.PlacementStrategy;

public class ClockNumber {
    private final PlacementStrategy strategy;
    public ClockNumber(PlacementStrategy strategy) {
        this.strategy = strategy;
    }
    public void build() {
        strategy.place();
    }
}
