package de.nogaemer.ngp.utils.msg.lang;

import de.nogaemer.ngp.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class Lang {
    private static HashMap<String, YamlConfiguration> lang_ymlm = new HashMap<>();
    private static File cfgFile = new File("plugins/NGP/language/player.yml");
    public static YamlConfiguration EN_US;
    public static YamlConfiguration DE_DE;
    private static YamlConfiguration cfg;

    public static void setup(){
        File dataFolder = new File(Main.getInstance().getDataFolder().getPath());
        if (!dataFolder.exists()){
            dataFolder.mkdir();
        }

        File langFolder = new File(Main.getInstance().getDataFolder().getPath() + "/language");
        if (!langFolder.exists()){
            langFolder.mkdir();
        }


        if (!cfgFile.exists()) {
            try {
                cfgFile.createNewFile();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        ArrayList<File> langs = new ArrayList();
        langs.add(new File(langFolder, "DE_DE.yml"));
        langs.add(new File(langFolder, "EN_US.yml"));

        for (File lang : langs) {
            try {
                InputStream in = Main.getInstance().getResource(lang.getName());
                Files.copy(in, lang.toPath());
            } catch (IOException e) {
            }
        }

        for (File file : langFolder.listFiles()) {
            if (file.getName() != "player.yml"){
                YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                if (file.getName().equalsIgnoreCase("EN_US.yml")) EN_US = cfg;
                if (file.getName().equalsIgnoreCase("DE_DE.yml")) DE_DE = cfg;
            }
        }



        cfg = YamlConfiguration.loadConfiguration(cfgFile);
    }

    public static void changeLang(Player p, String language){

        switch (language){
            case "en_en" :
                cfg.set("players." + p.getUniqueId(), "EN_US.yml");
                break;
            case "de_de" :
                cfg.set("players." + p.getUniqueId(), "DE_DE.yml");
                break;
        }

        saveAllFiles();
    }

    public static YamlConfiguration getLang(CommandSender sender){

        if (sender instanceof Player){
            Player p = (Player) sender;
            if (cfg.getConfigurationSection("players").contains(p.getUniqueId().toString())){
                switch (cfg.getString("players." + p.getUniqueId())){
                    case "DE_DE.yml":
                        return DE_DE;
                    default:
                        return EN_US;
                }
            } else return EN_US;
        } else return EN_US;
    }

    public static String getLangName(CommandSender sender){

        if (sender instanceof Player){
            Player p = (Player) sender;
            if (cfg.getConfigurationSection("players").contains(p.getUniqueId().toString())){
                switch (cfg.getString("players." + p.getUniqueId())){
                    case "DE_DE.yml":
                        return "DE_DE";
                    default:
                        return "EN_US";
                }
            } else return "EN_US";
        } else return "EN_US";
    }

    public static Object msg(String path, CommandSender sender){
        Object msg;
        YamlConfiguration lang = getLang(sender);
        if (lang.get(path) == null){
            msg = path;
        } else {
            msg = lang.get(path);
        }
        return msg;
    }

    public static Object msg(String path, Player p){
        Object msg;
        YamlConfiguration lang = getLang(p);
        if (lang.get(path) == null){
            msg = path;
        } else {
            msg = lang.get(path);
        }

        return msg;
    }

    public static Object msg(String path, String language){
        Object msg;
        YamlConfiguration lang;
        switch (language){
            case "EN_US":
                 lang = EN_US;
                 break;
            case "DE_DE":
                lang = DE_DE;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + language);
        }

        if (lang.get(path) == null){
            msg = path;
        } else {
            msg = lang.get(path);
        }

        return msg;
    }

    public static void saveAllFiles() {
        try {
            cfg.save(cfgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static YamlConfiguration getCfg(){
        return cfg;
    }

    public static HashMap getPlayers(){
        if (cfg.get("players") != null){
            HashMap<String, YamlConfiguration> players = new HashMap<>();
            for (String uuid: cfg.getConfigurationSection("players").getKeys(false)) {
                if (cfg.getString("players." + uuid).equalsIgnoreCase("EN_US.yml")){
                    players.put(uuid, EN_US);
                } else if (cfg.getString("players." + uuid).equalsIgnoreCase("DE_DE.yml")){
                    players.put(uuid, DE_DE);
                }
            }
            return players;
        } else return null;
    }
}
