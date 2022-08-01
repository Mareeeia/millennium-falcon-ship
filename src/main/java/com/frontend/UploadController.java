package com.frontend;

import com.core.OnboardComputer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Empire;
import com.model.MillenniumFalconSettings;
import com.model.UniverseMap;
import com.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@EnableAutoConfiguration
public class UploadController {

    @Autowired
    private OnboardComputer onboardComputer;


    @GetMapping("/")
    public String homepage() {
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) { //TODO: Clean up

        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/";
        }
        try {
            var jsonString = new String(file.getBytes());
            var empire = new ObjectMapper().readValue(jsonString, Empire.class);
            int odds = calculateOdds(empire);
            attributes.addFlashAttribute("message", "The odds are " + odds + "%!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

    private int calculateOdds(Empire empire) throws IOException {
        MillenniumFalconSettings falcon = FileUtil.getDefaultFalconSettings();
        UniverseMap universeMap = FileUtil.getUniverseMap(falcon.getRoutesDB());
        return this.onboardComputer.giveMeTheOdds(falcon, empire, universeMap);
    }

}