package com.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Empire;
import com.model.MillenniumFalconSettings;
import com.model.UniverseMap;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    public static MillenniumFalconSettings getFalconSettings(String path) throws IOException {
        if (path == null || path.isEmpty()) {
            path = FileUtil.class.getClassLoader().getResource("falcon_config/falcon_initial_config.json").toString();
        }
        String jsonString = new String(Files.readAllBytes(Paths.get(path)));
        return new ObjectMapper().readValue(jsonString, MillenniumFalconSettings.class);
    }

    public static Empire getEmpireSettings(String path) throws IOException {
        if (path == null || path.isEmpty()) {
            path = FileUtil.class.getClassLoader().getResource("empire/basic_empire.json").toString();
        }
        String jsonString = new String(Files.readAllBytes(Paths.get(path)));
        return new ObjectMapper().readValue(jsonString, Empire.class);
    }

    public static UniverseMap getUniverseMap(String fileName) {
        return DBUtil.getUniverseMap(fileName);
    }
}
