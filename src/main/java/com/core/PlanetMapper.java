package com.core;

import com.model.Planet;
import com.model.UniverseMap;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PlanetMapper {

    public Map<String, Planet> makePlanetMap(UniverseMap universeMap) {
        Map<String, Planet> planetMap = new HashMap<>();
        universeMap.getPlanets()
                .forEach((key, value) -> makePlanets(key, value, planetMap));
        return planetMap;
    }

    /**
     * Builds a planet and updates its adjacency graph with both children and parents,
     * so that we can traverse bi-directionally.
     *
     * @param route     Pair of planets between which there is a way.
     * @param distance  Distance between planets.
     * @param planetMap The map of <String, Planet> we are creating and updating.
     */
    private void makePlanets(Pair<String, String> route, int distance, Map<String, Planet> planetMap) {
        var startPlanet = planetMap.getOrDefault(route.getLeft(), new Planet(route.getLeft()));
        var destinationPlanet = planetMap.getOrDefault(route.getRight(), new Planet(route.getRight()));
        startPlanet.addAdjacentPlanet(destinationPlanet, distance);
        destinationPlanet.addAdjacentPlanet(startPlanet, distance);
        planetMap.put(route.getLeft(), startPlanet);
        planetMap.put(route.getRight(), destinationPlanet);
    }
}
