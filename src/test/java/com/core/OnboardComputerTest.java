package com.core;

import com.model.BountyHunter;
import com.model.Empire;
import com.model.MillenniumFalconSettings;
import com.model.UniverseMap;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OnboardComputerTest {

    private final OnboardComputer onboardComputer = new OnboardComputer(new DijkstraShortestPath(new PlanetMapper()));


    /**
     * As the testing is quite generic across, we can parametrise the arguments.
     *
     * @return
     */
    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of(makeFalcon(), Empire.builder().countdown(6).build(), makeSmallMap(), 100),
                Arguments.of(makeFalcon(), makeEmpireWithCountdown(7), makeLargerMap(), 0),
                Arguments.of(makeFalcon(), makeEmpireWithCountdown(8), makeLargerMap(), 81),
                Arguments.of(makeFalcon(), makeEmpireWithCountdown(9), makeLargerMap(), 90),
                Arguments.of(makeFalcon(), makeEmpireWithCountdown(10), makeLargerMap(), 100),
                Arguments.of(makeFalcon(), makeEmpireWithCountdown(100), makeLargerMap(), 100),
                Arguments.of(makeFalcon(), makeAmbushingEmpire(100), makeHugeBidirectionalMap(), 90),
                Arguments.of(makeFalcon(), makeAmbushingEmpire(100), makeUnconnectedMap(), 0)
        );
    }

    private static UniverseMap makeSmallMap() {
        return new UniverseMap(Map.of(Pair.of("Tatooine", "Dagobah"), 4,
                Pair.of("Dagobah", "Endor"), 1));
    }

    private static Empire makeEmpireWithCountdown(int countdown) {
        var hunterCoords = List.of(
                new BountyHunter("Hoth", 6),
                new BountyHunter("Hoth", 7),
                new BountyHunter("Hoth", 8)
        );
        return Empire.builder().bountyHunters(hunterCoords).countdown(countdown).build();
    }

    private static Empire makeAmbushingEmpire(int countdown) {
        var hunterCoords = List.of(
                new BountyHunter("Tatooine", 0),
                new BountyHunter("Hoth", 6),
                new BountyHunter("Hoth", 7),
                new BountyHunter("Hoth", 8)
        );
        return Empire.builder().bountyHunters(hunterCoords).countdown(countdown).build();
    }

    private static MillenniumFalconSettings makeFalcon() {
        return MillenniumFalconSettings.builder()
                .departure("Tatooine")
                .arrival("Endor")
                .autonomy(6)
                .build();
    }

    private static UniverseMap makeLargerMap() {
        return new UniverseMap(Map.of(Pair.of("Tatooine", "Dagobah"), 6,
                Pair.of("Dagobah", "Endor"), 4,
                Pair.of("Dagobah", "Hoth"), 1,
                Pair.of("Hoth", "Endor"), 1,
                Pair.of("Tatooine", "Hoth"), 6));
    }

    private static UniverseMap makeHugeBidirectionalMap() {
        return new UniverseMap(Map.of(Pair.of("Tatooine", "B"), 6,
                Pair.of("A", "C"), 4,
                Pair.of("C", "C1"), 1,
                Pair.of("C", "C2"), 2,
                Pair.of("C", "B2"), 4,
                Pair.of("C2", "B1"), 3,
                Pair.of("B", "B1"), 9,
                Pair.of("B", "B2"), 2,
                Pair.of("Tatooine", "B3"), 1,
                Pair.of("B1", "Endor"), 6));
    }

    private static UniverseMap makeUnconnectedMap() {
        return new UniverseMap(Map.of(Pair.of("Dagobah", "Endor"), 4,
                Pair.of("Tatooine", "Hoth"), 6));
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    public void testRoutes(MillenniumFalconSettings falcon, Empire empire, UniverseMap map, int odds) {
        int path = onboardComputer.giveMeTheOdds(falcon, empire, map);
        assertEquals(odds, path);
    }

    @Test
    public void testThrowsExceptionWhenNoInformation() {
        var thrown = assertThrows(NullPointerException.class, () -> onboardComputer.giveMeTheOdds(null, null, null));
        assertEquals("settings is marked non-null but is null", thrown.getMessage());
    }
}
