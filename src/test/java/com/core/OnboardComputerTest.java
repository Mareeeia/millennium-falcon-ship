package com.core;

import com.model.BountyHunter;
import com.model.Empire;
import com.model.MillenniumFalconSettings;
import com.model.UniverseMap;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OnboardComputerTest {

    private final OnboardComputer onboardComputer = new OnboardComputer();

    @Test
    public void testGetSuccessChanceNoHunters() {
        int path = onboardComputer.giveMeTheOdds(makeFalcon(), Empire.builder().countdown(6).build(), makeSmallMap());
        assertEquals(100, path);
    }

    @Test
    public void testGetSuccessChanceShortCountdown() {
        int path = onboardComputer.giveMeTheOdds(makeFalcon(), makeEmpireWithCountdown(7), makeLargerMap());
        assertEquals(0, path);
    }

    @Test
    public void testGetSuccessChanceCountdown8() {
        int path = onboardComputer.giveMeTheOdds(makeFalcon(), makeEmpireWithCountdown(8), makeLargerMap());
        assertEquals(81, path);
    }

    @Test
    public void testGetSuccessChanceCountdown9() {
        int path = onboardComputer.giveMeTheOdds(makeFalcon(), makeEmpireWithCountdown(9), makeLargerMap());
        assertEquals(90, path);
    }

    @Test
    public void testGetSuccessChanceCountdown10() {
        int path = onboardComputer.giveMeTheOdds(makeFalcon(), makeEmpireWithCountdown(10), makeLargerMap());
        assertEquals(100, path);
    }

    @Test
    public void testGetsBestOptionDespiteLongCountdown() {
        int path = onboardComputer.giveMeTheOdds(makeFalcon(), makeEmpireWithCountdown(100), makeLargerMap());
        assertEquals(100, path);
    }

    private UniverseMap makeSmallMap() {
        return new UniverseMap(Map.of(Pair.of("Tatooine", "Dagobah"), 4,
                Pair.of("Dagobah", "Endor"), 1));
    }

    private Empire makeEmpireWithCountdown(int countdown) {
        var hunterCoords = List.of(
                new BountyHunter("Hoth", 6),
                new BountyHunter("Hoth", 7),
                new BountyHunter("Hoth", 8)
        );

        return Empire.builder().bountyHunters(hunterCoords).countdown(countdown).build();
    }

    private MillenniumFalconSettings makeFalcon() {
        return MillenniumFalconSettings.builder()
                .departure("Tatooine")
                .arrival("Endor")
                .autonomy(6)
                .build();
    }

    private UniverseMap makeLargerMap() {
        return new UniverseMap(Map.of(Pair.of("Tatooine", "Dagobah"), 6,
                Pair.of("Dagobah", "Endor"), 4,
                Pair.of("Dagobah", "Hoth"), 1,
                Pair.of("Hoth", "Endor"), 1,
                Pair.of("Tatooine", "Hoth"), 6));
    }
}
