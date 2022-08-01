package com.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Empire;
import com.model.MillenniumFalconSettings;
import com.model.UniverseMap;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class FileUtil {

    private static final String DEFAULT_FALCON = "falcon_config/falcon_initial_config.json";

    public static UniverseMap getUniverseMap(String fileName) {
        return DBUtil.getUniverseMap(fileName);
    }

    public static MillenniumFalconSettings getDefaultFalconSettings() throws IOException {
        InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(DEFAULT_FALCON);
        String jsonString = new String(Objects.requireNonNull(inputStream).readAllBytes());
        return new ObjectMapper().readValue(jsonString, MillenniumFalconSettings.class);
    }

    public static MillenniumFalconSettings getFalconSettings(String path) throws IOException {
        if (path == null || path.isEmpty()) {
            return null;
        }
        String jsonString = new String(Files.readAllBytes(Paths.get(path)));
        return new ObjectMapper().readValue(jsonString, MillenniumFalconSettings.class);
    }

    public static Empire getEmpireSettings(String path) throws IOException {
        if (path == null || path.isEmpty()) {
            return null;
        }
        String jsonString = new String(Files.readAllBytes(Paths.get(path)));
        return new ObjectMapper().readValue(jsonString, Empire.class);
    }
}
