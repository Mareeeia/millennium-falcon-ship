package com.core;

import com.model.Empire;
import com.model.Planet;
import lombok.*;
import lombok.experimental.Delegate;

import java.util.*;
import java.util.stream.Collectors;

public class PlanetaryRoutes {

    private final DijkstraShortestPath dijkstraShortestPath;
    private final Integer maxTime;

    private final Empire empire;

    private final static int FUEL_MAX = 6;

    public PlanetaryRoutes(DijkstraShortestPath dijkstraShortestPath,
                           Empire empire) {
        this.dijkstraShortestPath = dijkstraShortestPath;
        this.empire = empire;
        this.maxTime = empire.getCountdown();
    }

    /**
     *
     * @return
     */
    public int calculateMissionSuccessChance(String source, String  destination) {
        this.dijkstraShortestPath.calculateShortestPath(source, destination);
        var planetMap = this.dijkstraShortestPath.getPlanetMap();
        Set<String> settledPlanets = new HashSet<>();
        List<ShipState> arrivals = new ArrayList<>();
        Queue<ShipState> unsettledPlanets = new PriorityQueue<>(List.of(
                new ShipState(planetMap.get(source), FUEL_MAX, 0, getBountyHunters(0, source))));
        while (!unsettledPlanets.isEmpty()) {
            ShipState currentShipState = unsettledPlanets.poll();
            currentShipState.getAdjacentPlanets().entrySet().stream()
                    .filter(entry -> !settledPlanets.contains(entry.getKey().getName()))
                    .filter(entry -> entry.getValue() <= currentShipState.getFuel())
                    .filter(entry -> entry.getValue() + currentShipState.getTime() + entry.getKey().getHeuristicDistance() <= maxTime)
                    .forEach(entry -> {
                        int hunters = getBountyHunters(currentShipState.getTime() + entry.getValue(), entry.getKey().getName());
                        unsettledPlanets.add(new ShipState(entry.getKey(),
                                currentShipState.getFuel() - entry.getValue(),
                                currentShipState.getTime() + entry.getValue(),
                                currentShipState.getHunterEncounters() + hunters)); //TODO: tidy
                    });
            if (currentShipState.getHeuristicDistance() + currentShipState.getTime() < maxTime) {
                unsettledPlanets.add(new ShipState(currentShipState.getPlanet(),
                        FUEL_MAX,
                        currentShipState.getTime() + 1,
                        currentShipState.getHunterEncounters() + getBountyHunters(currentShipState.getTime() + 1, currentShipState.getName())));
            }
            if (currentShipState.getName().equals(destination)) {
                arrivals.add(currentShipState);
            }
            settledPlanets.add(currentShipState.getName());
        }
        return calculateSuccessChance(arrivals);
    }

    private Integer calculateSuccessChance(List<ShipState> arrivals) {
        if (arrivals.isEmpty()) {
            return 0;
        }
        double encounterChance = 0;
        for (int i = 0; i < arrivals.get(0).getHunterEncounters(); i++) {
            encounterChance += Math.pow(9, i)/Math.pow(10, i + 1);
        }
        return (int) Math.round((1D - encounterChance) * 100);
    }

    private int getBountyHunters(int time, String planetName) {
        var daySet = this.empire.getBountyHunters().stream()
                .filter(entry -> entry.getLeft().equals(planetName))
                .map(entry -> entry.getRight())
                .collect(Collectors.toSet());
        return daySet.contains(time) ? 1 : 0;
    }

    private void evaluateDistanceAndPath(Planet adjacentPlanet, Integer distance, Planet sourcePlanet) {
        int newDistance = sourcePlanet.getHeuristicDistance() + distance;
        if (newDistance < adjacentPlanet.getHeuristicDistance()) {
            adjacentPlanet.setHeuristicDistance(newDistance);
        }
    }

    @Getter
    @Setter
    public class ShipState implements Comparable<ShipState> {

        private int fuel = 6;

        @Delegate
        private Planet planet;

        private int time = 0;

        private int hunterEncounters = 0;
//
//        public ShipState(String name) {
//            this.planet = new Planet(name);
//        }

        public ShipState(Planet planet, int fuel, int time, int hunterEncounters) {
            this.planet = new Planet(planet);
            this.fuel = fuel;
            this.time = time;
            this.hunterEncounters = hunterEncounters;
        }



//        public ShipState(String name, int fuel, int time) {
//            this.planet = new Planet(name);
//            this.fuel = fuel;
//            this.time = time;
//        }

        @Override
        public int compareTo(ShipState shipState) {
            var hunterComparator = Integer.compare(hunterEncounters, shipState.hunterEncounters);
            if (hunterComparator != 0) return hunterComparator;
            return Integer.compare(this.getHeuristicDistance(), shipState.getHeuristicDistance()); // TODO: make better
        }
    }

}
