package com.core;

import com.model.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OnboardComputer {

    public int giveMeTheOdds(MillenniumFalconSettings settings, Empire empire, UniverseMap universeMap) {
        var planetMap = DijkstraShortestPath.mapOutDistancesToDestination(universeMap, settings.getArrival());
        return calculateMissionSuccessChance(settings.getDeparture(),
                settings.getArrival(),
                settings.getAutonomy(),
                empire,
                planetMap);
    }

    private int calculateMissionSuccessChance(String source,
                                              String destination,
                                              Integer maxFuel,
                                              Empire empire,
                                              Map<String, Planet> planetMap) {
        Set<String> visitedPlanets = new HashSet<>(); //TODO: If we use greedy option, the first arrival will be the most efficient
        List<ShipState> arrivals = new ArrayList<>();
        Queue<ShipState> possibleShipMoves = new PriorityQueue<>(List.of(
                new ShipState(planetMap.get(source), maxFuel, 0, getBountyHunters(0, source, empire))));
        while (!possibleShipMoves.isEmpty()) {
            // Poll ship state from priority queue and add viable next moves to queue
            ShipState currentShipState = possibleShipMoves.poll();
            currentShipState.getAdjacentPlanets().entrySet().stream()
                    .filter(entry -> shouldVisitPlanet(entry, currentShipState, visitedPlanets, empire.getCountdown()))
                    .forEach(entry -> updateStatesQueue(entry, currentShipState, empire, possibleShipMoves));

            // Alternatively, if we can afford to, and it makes sense, wait in place/refuel
            if (currentShipState.canAffordToWait(empire.getCountdown())) {
                int hunters = getBountyHunters(currentShipState.getTime() + 1, currentShipState.getPlanetName(), empire);
                possibleShipMoves.add(ShipState.builder()
                        .planet(currentShipState.getPlanet())
                        .fuel(maxFuel)
                        .time(currentShipState.getTime() + 1)
                        .hunterEncounters(currentShipState.getHunterEncounters() + hunters).build());
            }
            if (currentShipState.getPlanetName().equals(destination)) {
                arrivals.add(currentShipState);
            }
            visitedPlanets.add(currentShipState.getPlanetName());
        }
        return calculateSuccessChance(arrivals);
    }

    private void updateStatesQueue(Map.Entry<Planet, Integer> planetAndDistance,
                                   ShipState currentShipState,
                                   Empire empire,
                                   Queue<ShipState> possibleShipMoves) {
        int hunters = getBountyHunters(currentShipState.getTime() + planetAndDistance.getValue(), planetAndDistance.getKey().getPlanetName(), empire);
        possibleShipMoves.add(ShipState.builder()
                .planet(planetAndDistance.getKey())
                .fuel(currentShipState.getFuel() - planetAndDistance.getValue())
                .time(currentShipState.getTime() + planetAndDistance.getValue())
                .hunterEncounters(currentShipState.getHunterEncounters() + hunters).build());
    }

    private boolean shouldVisitPlanet(Map.Entry<Planet, Integer> planetAndDistance,
                                      ShipState currentShipState,
                                      Set<String> visitedPlanets,
                                      int countdown) {
        return !visitedPlanets.contains(planetAndDistance.getKey().getPlanetName())
                && currentShipState.hasEnoughFuel(planetAndDistance.getValue())
                && canReachDestinationInTime(currentShipState,
                planetAndDistance.getKey(),
                planetAndDistance.getValue(),
                countdown);
    }

    private boolean canReachDestinationInTime(ShipState currentShipState, Planet planet, int distance, int countDown) {
        return countDown >= currentShipState.getTime() +         // time so far
                distance +                                       // time between current planet and next one
                planet.getHeuristicDistance();                   // time projected by Dijkstra
    }

    private Integer calculateSuccessChance(List<ShipState> arrivals) {
        if (arrivals.isEmpty()) {
            return 0;
        }
        double encounterChance = 0;
        for (int i = 0; i < arrivals.get(0).getHunterEncounters(); i++) {
            encounterChance += Math.pow(9, i) / Math.pow(10, i + 1);
        }
        return (int) Math.round((1D - encounterChance) * 100);
    }

    private int getBountyHunters(int time, String planetName, Empire empire) {
        var daySet = empire.getBountyHunters().stream()
                .filter(entry -> entry.getPlanet().equals(planetName))
                .map(BountyHunter::getDay)
                .collect(Collectors.toSet());
        return daySet.contains(time) ? 1 : 0;
    }
}
