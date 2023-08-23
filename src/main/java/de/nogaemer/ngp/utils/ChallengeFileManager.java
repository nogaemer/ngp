package de.nogaemer.ngp.utils;

import de.nogaemer.ngp.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ChallengeFileManager {
    private static File folder = new File(Main.getInstance().getDataFolder() + "/data/");
    private static File cfgFile = new File(Main.getInstance().getDataFolder() + "/data/challenges.yml");

    private static YamlConfiguration cfg;

    public static void setup(){
        if (!folder.exists()) {
            folder.mkdir();
        }

        if (!cfgFile.exists()) {
            try {
                cfgFile.createNewFile();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        cfg = YamlConfiguration.loadConfiguration(cfgFile);
    }

    public static YamlConfiguration getCfg(){
        return cfg;
    }

    public static void saveAllFiles() {
        try {
            cfg.save(cfgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
