package com.core;

import com.model.Planet;
import com.model.UniverseMap;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class PlanetMapper {

    public static Map<String, Planet> makePlanetMap(UniverseMap universeMap) {
        Map<String, Planet> planetMap = new HashMap<>();
        universeMap.getPlanets().forEach((key, value) -> makePlanets(key, value, planetMap));
        return planetMap;
    }

    private static void makePlanets(Pair<String, String> route, int weight, Map<String, Planet> planetMap) {
        var startPlanet = planetMap.getOrDefault(route.getLeft(), new Planet(route.getLeft()));
        var destinationPlanet = planetMap.getOrDefault(route.getRight(), new Planet(route.getRight()));
        startPlanet.addAdjacentPlanet(destinationPlanet, weight);
        destinationPlanet.addBackwardsAdjacentPlanet(startPlanet, weight);
        planetMap.put(route.getLeft(), startPlanet);
        planetMap.put(route.getRight(), destinationPlanet);
    }
}
