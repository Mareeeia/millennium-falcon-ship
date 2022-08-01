package com.ag04.clidemo.command;

import com.core.OnboardComputer;
import com.model.Empire;
import com.model.MillenniumFalconSettings;
import com.model.UniverseMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;

import static com.util.FileUtil.*;

@ShellComponent
@EnableAutoConfiguration
public class GiveMeTheOddsCommand {

    @Autowired
    private OnboardComputer onboardComputer;

    @ShellMethod("Displays odds given path to Millennium Falcon config and path to Empire config.")
    public Integer giveMeTheOdds(@ShellOption({"-F", "--falcon"}) String falconPath, @ShellOption({"-E", "--empire"}) String empirePath) throws IOException {
        MillenniumFalconSettings settings = getFalconSettings(falconPath);
        Empire empire = getEmpireSettings(empirePath);
        UniverseMap universeMap = getUniverseMap(settings.getRoutesDB());
        return this.onboardComputer.giveMeTheOdds(settings, empire, universeMap);
    }

}