package de.nogaemer.ngp.utils;

import de.nogaemer.ngp.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomFileManager {
    File folder;
    File cfgFile;

    private static YamlConfiguration cfg;

    public CustomFileManager(String folder, String cfgFile) {
        this.folder = new File(folder);
        this.cfgFile = new File(this.folder.getPath() + "/" + cfgFile);

        if (!this.folder.exists()) {
            this.folder.mkdirs();
        }

        if (!this.cfgFile.exists()) {
            try {
                this.cfgFile.createNewFile();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        cfg = YamlConfiguration.loadConfiguration(this.cfgFile);
    }

    public YamlConfiguration getCfg(){
        return cfg;
    }

    public void saveAllFiles() {
        try {
            cfg.save(cfgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
