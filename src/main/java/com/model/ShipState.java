package com.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Delegate;

@Getter
@Builder
public class ShipState implements Comparable<ShipState> {
    @Delegate
    private final Planet planet;
    private final int fuel;
    private final int time;
    private final int hunterEncounters;

    public ShipState(Planet planet, int fuel, int time, int hunterEncounters) {
        this.planet = new Planet(planet);
        this.fuel = fuel;
        this.time = time;
        this.hunterEncounters = hunterEncounters;
    }

    public boolean hasEnoughFuel(int distance) {
        return distance <= this.fuel;
    }

    public boolean canAffordToWait(int countdown) {
        return this.getHeuristicDistance() + time < countdown;
    }

    @Override
    public int compareTo(ShipState shipState) {
        var hunterComparator = Integer.compare(hunterEncounters, shipState.hunterEncounters);
        if (hunterComparator != 0) {
            return hunterComparator;
        }
        return Integer.compare(this.getHeuristicDistance() + time, shipState.getHeuristicDistance() + shipState.getTime()); // TODO: make better
    }
}
