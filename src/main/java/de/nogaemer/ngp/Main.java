package de.nogaemer.ngp;

import de.nogaemer.ngp.MainListeners.JoinListener;
import de.nogaemer.ngp.MainListeners.QuitListener;
import de.nogaemer.ngp.challenges.ForceBattle;
import de.nogaemer.ngp.challenges.HBR;
import de.nogaemer.ngp.challenges.Randomizer;
import de.nogaemer.ngp.commands.InvSee;
import de.nogaemer.ngp.utils.*;
import de.nogaemer.ngp.utils.menues.gui.Gui;
import de.nogaemer.ngp.utils.menues.gui.LangGUI;
import de.nogaemer.ngp.utils.menues.gui.OverviewGUI;
import de.nogaemer.ngp.utils.menues.gui.challenges.ChallengeGUIRegistry;
import de.nogaemer.ngp.utils.msg.lang.Lang;
import de.nogaemer.ngp.utils.menues.gui.command.GuiCommand;
import de.nogaemer.ngp.utils.msg.lang.SetLangOnJoin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin implements Listener {

    static Main instance;

    static CustomFileManager fileManager;

    @Override
    public void onLoad() {
        Main.instance = this;

        resetWorld();
    }

    @Override
    public void onEnable() {
        String file = null;
        for (World world : Bukkit.getWorlds()) {
            if (world.getEnvironment() == World.Environment.THE_END){
                file = world.getWorldFolder().getPath();
            }
        }
        fileManager = new CustomFileManager(file + "/data/world_data", "data.yml");
        if (fileManager.getCfg().get("firstEnderdragon") == null){
            fileManager.getCfg().set("firstEnderdragon", true);
            fileManager.saveAllFiles();
        }

        FileManager.setup();
        ChallengeFileManager.setup();
        Lang.setup();
        ForceBattle.setup();
        ChallengeGUIRegistry.setup();
        LangGUI.setup();
        OverviewGUI.setup();
        HBR.setup();
        Randomizer.setup();

        getCommand("settings").setExecutor(new GuiCommand());
        getCommand("invsee").setExecutor(new InvSee());
        getCommand("test").setExecutor(new TestCmm());
        getCommand("test").setTabCompleter(new TestCmm());

        final PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new QuitListener(), this);
        manager.registerEvents(new JoinListener(), this);
        manager.registerEvents(new Gui(), this);
        manager.registerEvents(new SetLangOnJoin(), this);
        manager.registerEvents(new LangGUI(), this);
        manager.registerEvents(new OverviewGUI(), this);
        manager.registerEvents(new Timer(), this);
        manager.registerEvents(new ChallengeGUIRegistry(), this);
        manager.registerEvents(new ForceBattle(), this);
        manager.registerEvents(new HBR(), this);
        manager.registerEvents(new Randomizer(), this);
        manager.registerEvents(new InvSee(), this);
        manager.registerEvents(new TestListener(), this);

        Timer.setup(false, FileManager.getCfg().getInt("Timer.time"));
    }

    @Override
    public void onDisable() {
        FileManager.getCfg().set("Timer.time", Timer.getTime());
        FileManager.getCfg().set("Timer.running", Timer.isRunning());
        Randomizer.saveGame();
    }

    public static Main getInstance() {
        return instance;
    }

    public static CustomFileManager getFileManager() {
        return fileManager;
    }

    private void resetWorld(){
        if (!getConfig().contains("isReset")){
            getConfig().set("isReset", false);
            saveConfig();
            return;
        }
        if (getConfig().getBoolean("isReset")){
            File cfgFile = new File("plugins/data/config.yml");
            YamlConfiguration cfgabc = YamlConfiguration.loadConfiguration(cfgFile);
            cfgabc.set("Timer.Time", 0);
            try {
                cfgabc.save(cfgFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            File world = new File(Bukkit.getWorldContainer(), "world");
            File nether = new File(Bukkit.getWorldContainer(), "world_nether");
            File end = new File(Bukkit.getWorldContainer(), "world_the_end");

            Reset.reset(world);
            Reset.reset(nether);
            Reset.reset(end);

            getConfig().set("isReset", false);
            saveConfig();
        }
    }

}
