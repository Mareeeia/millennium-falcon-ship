package com.core;

import com.model.*;
import lombok.NonNull;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class employs the A* algorithm and uses Dijkstra as a heuristic to determine
 * if taking a certain path is ever viable, e.g. if we are at Planet A and have travelled for 5 days
 * and by Dijkstra there are at least 6 more days to the destination, then we can't reach the destination in 10 days,
 * so we can discard the path altogether.
 */
@Component
@EnableAutoConfiguration
public class OnboardComputer {

    private final DijkstraShortestPath dijkstraShortestPath;

    public OnboardComputer(DijkstraShortestPath dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public int giveMeTheOdds(@NonNull MillenniumFalconSettings settings,
                             @NonNull Empire empire,
                             @NonNull UniverseMap universeMap) {
        var planetMap = this.dijkstraShortestPath.mapOutDistancesToDestination(universeMap, settings.getArrival());
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
        Set<String> visitedPlanets = new HashSet<>();
        List<ShipState> arrivals = new ArrayList<>();

        Queue<ShipState> possibleShipMoves = new PriorityQueue<>(List.of(
                new ShipState(planetMap.get(source), maxFuel, 0, getBountyHunters(0, source, empire))));
        while (!possibleShipMoves.isEmpty()) {
            // Poll ship state from priority queue and add viable next moves to queue
            ShipState currentShipState = possibleShipMoves.poll();
            currentShipState.getAdjacentPlanets().entrySet().stream()
                    .filter(entry -> shouldVisitPlanet(entry.getKey(),
                            entry.getValue(),
                            currentShipState,
                            visitedPlanets,
                            empire.getCountdown()))
                    .forEach(entry -> updateNextMovesQueue(entry.getKey(),
                            entry.getValue(),
                            currentShipState,
                            empire,
                            possibleShipMoves));

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
                return calculateSuccessChance(List.of(currentShipState));
            }
            visitedPlanets.add(currentShipState.getPlanetName());
        }
        return 0;
    }

    private void updateNextMovesQueue(Planet planet,
                                      Integer distance,
                                      ShipState currentShipState,
                                      Empire empire,
                                      Queue<ShipState> possibleShipMoves) {
        int hunters = getBountyHunters(currentShipState.getTime() + distance, planet.getPlanetName(), empire);
        possibleShipMoves.add(ShipState.builder()
                .planet(planet)
                .fuel(currentShipState.getFuel() - distance)
                .time(currentShipState.getTime() + distance)
                .hunterEncounters(currentShipState.getHunterEncounters() + hunters).build());
    }

    private boolean shouldVisitPlanet(Planet planet,
                                      Integer distance,
                                      ShipState currentShipState,
                                      Set<String> visitedPlanets,
                                      int countdown) {
        return !visitedPlanets.contains(planet.getPlanetName())
                && planet.getHeuristicDistance() != Integer.MAX_VALUE  //It's possible to reach
                && currentShipState.hasEnoughFuel(distance)
                && canReachDestinationInTime(currentShipState,
                planet,
                distance,
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
        ShipState optimalArrival = arrivals.get(0); // by the priority queue first arrival should be optimal
        for (int i = 0; i < optimalArrival.getHunterEncounters(); i++) {
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
