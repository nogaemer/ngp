package de.nogaemer.ngp.challenges;

import de.nogaemer.ngp.Main;
import de.nogaemer.ngp.utils.copystuff.AnvilGUI;
import de.nogaemer.ngp.utils.menues.gui.Gui;
import de.nogaemer.ngp.utils.Timer;
import de.nogaemer.ngp.utils.menues.gui.challenges.ChallengeGUI;
import de.nogaemer.ngp.utils.msg.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ForceBattle implements Listener {
    private static HashMap<UUID, ArrayList<Material>> items = new HashMap<>();
    private static HashMap<UUID, ArrayList<EntityType>> mobs = new HashMap<>();
    private static HashMap<String, AnvilGUI.Builder> anvilInvs = new HashMap<String, AnvilGUI.Builder>();

    private static HashMap<UUID, Material> item = new HashMap<>();
    private static HashMap<UUID, EntityType> mob = new HashMap<>();
    private static HashMap<UUID, ArrayList<ItemStack>> history = new HashMap<>();
    private static HashMap<UUID, ItemStack[]> historyitems = new HashMap<>();
    private static UUID historyPlayer;
    private static ArrayList<UUID> tempHistoryMap = new ArrayList<>();
    private static EntityType[] blacklistMob = {EntityType.MUSHROOM_COW, EntityType.WITHER, EntityType.ENDER_DRAGON, EntityType.GIANT,
            EntityType.ILLUSIONER, EntityType.IRON_GOLEM, EntityType.PLAYER, EntityType.ARMOR_STAND, EntityType.UNKNOWN, EntityType.SNOWMAN};
    private static Material[] blacklistItem = {Material.COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK, Material.COMMAND_BLOCK_MINECART, Material.STRUCTURE_BLOCK, Material.STRUCTURE_VOID,
            Material.AIR, Material.BARREL, Material.BEDROCK, Material.LIGHT, Material.FROGSPAWN};

    public static HashMap<UUID, ArmorStand> armorStandHashMap = new HashMap<>();
    private static HashMap<String, Boolean> active = new HashMap<>();
    public static boolean running = false;
    public static int endtime = 50;

    private static HashMap<UUID, ArrayList<Inventory>> historyinv = new HashMap<>();
    private static HashMap<String, Inventory> invs = new HashMap<String, Inventory>();
    private static HashMap<String, ItemStack> reditem = new HashMap<String, ItemStack>();
    private static HashMap<String, ItemStack> greenitem = new HashMap<String, ItemStack>();
    private static HashMap<String, ItemStack> redmob = new HashMap<String, ItemStack>();
    private static HashMap<String, ItemStack> greenmob = new HashMap<String, ItemStack>();
    private static ArrayList<Material> mat = new ArrayList<>();
    private static ArrayList<EntityType> entityTypes = new ArrayList<>();
    private static int joker = 1;

    public static void setup(){
        reditem.put("EN_US", Gui.setName(new ItemStack(Material.RED_DYE), "item", (String) Lang.msg("menus.items.deactivated.name", "EN_US"), (List) Lang.msg("menus.items.deactivated.lore", "EN_US")));
        reditem.put("DE_DE", Gui.setName(new ItemStack(Material.RED_DYE), "item", (String) Lang.msg("menus.items.deactivated.name", "DE_DE"), (List) Lang.msg("menus.items.deactivated.lore", "DE_DE")));
        greenitem.put("EN_US", Gui.setName(new ItemStack(Material.LIME_DYE), "item", (String) Lang.msg("menus.items.activated.name", "EN_US"), (List) Lang.msg("menus.items.activated.lore", "EN_US")));
        greenitem.put("DE_DE", Gui.setName(new ItemStack(Material.LIME_DYE), "item", (String) Lang.msg("menus.items.activated.name", "DE_DE"), (List) Lang.msg("menus.items.activated.lore", "DE_DE")));

        redmob.put("EN_US", Gui.setName(new ItemStack(Material.RED_DYE), "mob", (String) Lang.msg("menus.items.deactivated.name", "EN_US"), (List) Lang.msg("menus.items.deactivated.lore", "EN_US")));
        redmob.put("DE_DE", Gui.setName(new ItemStack(Material.RED_DYE), "mob", (String) Lang.msg("menus.items.deactivated.name", "DE_DE"), (List) Lang.msg("menus.items.deactivated.lore", "DE_DE")));
        greenmob.put("EN_US", Gui.setName(new ItemStack(Material.LIME_DYE), "mob", (String) Lang.msg("menus.items.activated.name", "EN_US"), (List) Lang.msg("menus.items.activated.lore", "EN_US")));
        greenmob.put("DE_DE", Gui.setName(new ItemStack(Material.LIME_DYE), "mob", (String) Lang.msg("menus.items.activated.name", "DE_DE"), (List) Lang.msg("menus.items.activated.lore", "DE_DE")));

        invs.put("EN_US", createInv("EN_US"));
        invs.put("DE_DE", createInv("DE_DE"));

        anvilInvs.put("EN_US", createAnvilInv("EN_US"));
        anvilInvs.put("DE_DE", createAnvilInv("DE_DE"));

        active.put("mobs", false);
        active.put("items", false);
    }

    public static void setRunning(boolean running) {
        System.out.println(running + " | " + Randomizer.running);
        if (running) {
            ChallengeGUI.switchRunning("forcebattle", true);
            ForceBattle.running = true;
        } else {
            ChallengeGUI.switchRunning("forcebattle", false);
            ForceBattle.running = false;
        }
    }

    public static void start(){
        if (running && Timer.isRunning() && active.containsValue(true)){
            for (Material material : Material.values()) {
                if (material.isItem() && !material.name().contains("SPAWN_EGG")){
                    mat.add(material);
                }
            }

            for (EntityType ent : EntityType.values()) {
                if (ent.isAlive()){
                    entityTypes.add(ent);
                }
            }

            for (EntityType ent : blacklistMob) {
                entityTypes.remove(ent);
            }

            for (Material material : blacklistItem) {
                mat.remove(material);
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID playerUUID = player.getUniqueId();

                if (active.get("mobs")){
                    mobs.put(playerUUID, entityTypes);
                }
                if (active.get("items")){
                    items.put(playerUUID, mat);
                }
                ItemStack item = Gui.setName(new ItemStack(Material.BARRIER), "joker", "§c§l JOKER");
                item.setAmount(joker);
                player.getInventory().addItem(item);
                createArmorstand(playerUUID);
                first(playerUUID);
            }
        }

    }

    public static void stop(){
    }

    public static void reset(){
        items.clear();
        item.clear();
        mobs.clear();
        mob.clear();
        history.clear();
        historyitems.clear();
        historyinv.clear();
        tempHistoryMap.clear();
        armorStandHashMap.forEach((uuid, armorStand) -> armorStand.remove());
    }

    private static void createArmorstand(UUID uuid){
        Player p = Bukkit.getPlayer(uuid);
        if (running && Timer.isRunning()){
            ArmorStand armorStand = (ArmorStand) p.getWorld().spawn(p.getLocation(), ArmorStand.class);
            armorStand.setInvisible(true);
            armorStand.setInvulnerable(true);
            armorStand.setSilent(true);
            armorStand.setMarker(true);

            p.setPassenger(armorStand);

            armorStandHashMap.put(uuid, armorStand);
        }
    }

    private static void first(UUID uuid){
        if (active.size() > 0){
            Random r = new Random();
            int object;
            while (true){
                int map = r.nextInt(active.size());
                ArrayList<String> list = new ArrayList<>(active.keySet());
                if (active.get(list.get(map))){
                    switch (list.get(map)){
                        case "items":
                            item.put(uuid, items.get(uuid).get(r.nextInt(items.get(uuid).size())));
                            break;
                        case "mobs":
                            mob.put(uuid, mobs.get(uuid).get(r.nextInt(mobs.get(uuid).size())));
                            break;
                    }
                    history.put(uuid, new ArrayList<ItemStack>(Collections.singleton((createItemHistory(uuid)))));
                    return;
                }
            }
        }
    }

    public static void next(UUID uuid){
        if (running && Timer.isRunning()){
            if (active.size() > 0){
                Random r = new Random();
                int object;
                while (true){
                    int map = r.nextInt(active.size());
                    ArrayList<String> list = new ArrayList<>(active.keySet());
                    if (active.get(list.get(map))){

                        switch (list.get(map)){
                            case "items":
                                if (items.size() == 0){
                                    items.put(uuid, mat);
                                }
                                item.put(uuid, items.get(uuid).get(r.nextInt(items.get(uuid).size())));
                                items.get(uuid).remove(item.get(uuid));

                                mob.put(uuid, null);
                                break;
                            case "mobs":
                                if (mobs.size() == 0){
                                    mobs.put(uuid, entityTypes);
                                }
                                mob.put(uuid, mobs.get(uuid).get(r.nextInt(mobs.get(uuid).size() - 1)));
                                mobs.get(uuid).remove(mob.get(uuid));

                                item.put(uuid, null);
                                break;
                        }
                        history.get(uuid).add(createItemHistory(uuid));
                        return;
                    }
                }
            }
        }

    }

    private static ItemStack createItemHistory(UUID uuid){
        ItemStack item;
        HashMap map;
        String name = "";

        if (ForceBattle.item.get(uuid) != null){
            name =  " §6" + Gui.nameToString(ForceBattle.item.get(uuid).name().toLowerCase());
        } else if (mob.get(uuid) != null){
            name =  " §6" + Gui.nameToString(mob.get(uuid).getName());
        }

        if (ForceBattle.item.get(uuid) != null){
            map = ForceBattle.item;
            item = Gui.setName(new ItemStack((Material) map.get(uuid)), "", name, new ArrayList<String>(Arrays.asList("",Timer.getActionBarMSG())));
        } else if (ForceBattle.mob.get(uuid) != null){
            map = ForceBattle.mob;
            EntityType entity = (EntityType) map.get(uuid);
            item = Gui.setName(new ItemStack(Material.getMaterial(entity.name() + "_SPAWN_EGG")), "", name, new ArrayList<String>(Arrays.asList("",Timer.getActionBarMSG())));
        } else {
            item = Gui.setName(new ItemStack(Material.BARRIER), "", "§c ERROR");
        }

        armorStandHashMap.get(uuid).setHelmet(item);
        return item;
    }

    public static String sendActionbar(UUID uuid){
        if (running && Timer.isRunning()){
            if (item.get(uuid) != null){
                return " §7- §6" + Gui.nameToString(item.get(uuid).name().toLowerCase());
            } else if (mob.get(uuid) != null){
                return " §7- §6" + Gui.nameToString(mob.get(uuid).getName());
            }
            return "ERROR";
        }
        return "";
    }

    public static void end(){
        if (running && Timer.isRunning()){
            if (endtime == Timer.getTime()){
                Timer.setRunning(false);

                List<ArrayList> historybycount = new ArrayList<>(history.values());
                Collections.sort(historybycount, Comparator.comparing(ArrayList::size));


                for (int i = 0; i < historybycount.size(); i++) {
                    int finalI = i;
                    for (UUID uuid : history.keySet()) {
                        if (history.get(uuid) == historybycount.get(finalI)){
                            history.put(uuid, historybycount.get(finalI));
                            if (!tempHistoryMap.contains(uuid)){
                                tempHistoryMap.add(uuid);
                            }

                        }
                    }
                }
                historyinv.putAll(createHistoryInv("EN_US"));
                result();

                Bukkit.getOnlinePlayers().forEach(p -> {
                    historyitems.put(p.getUniqueId(), p.getInventory().getContents());
                    p.getInventory().clear();

                    p.getInventory().setItem(3, Gui.setName(new ItemStack(Material.BARRIER), "skip", "§cSkip"));
                    p.getInventory().setItem(5, Gui.setName(new ItemStack(Material.ITEM_FRAME), "open", "§aOpen"));
                });
            }
        }

    }

    private static void result(){
        if (tempHistoryMap.size() == 0){
            historyitems.keySet().forEach(uuid -> {
                Player p = Bukkit.getPlayer(uuid);
                p.getInventory().clear();
                p.getInventory().setContents(historyitems.get(uuid));
            });
            reset();
            return;
        }

        Player p = Bukkit.getPlayer(tempHistoryMap.get(0));
        UUID uuid = p.getUniqueId();

        for (int i = 0; i < history.get(uuid).size(); i++) {
            int finalI = i;
            Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
                historyinv.get(uuid).get(finalI/28).setItem(finalI % 28 / 7 * 7 + finalI % 28 % 7 + 10 + finalI % 28 / 7 * 2, history.get(uuid).get(finalI));
                Bukkit.getOnlinePlayers().forEach(player -> player.openInventory(historyinv.get(uuid).get(finalI /28)));
                if (finalI == history.get(uuid).size() - 1){
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.closeInventory();
                        player.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    });
                    switch (tempHistoryMap.size()) {
                        case 1 -> Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("1. Platz " + p.getName(), history.get(uuid).size() + " items"));
                        case 2 -> Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("2. Platz " + p.getName(), history.get(uuid).size() + " items"));
                        case 3 -> Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("3. Platz " + p.getName(), history.get(uuid).size() + " items"));
                        default -> Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(p.getName(), history.get(uuid).size() + " items"));
                    }
                    historyPlayer = tempHistoryMap.get(0);
                    tempHistoryMap.remove(0);
                }
            }, i * 3);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (running && Timer.isRunning()){
            UUID uuid = event.getPlayer().getUniqueId();
            if (item.containsKey(uuid) && item.get(uuid) == event.getItem().getItemStack().getType() && running && Timer.isRunning()){
                next(event.getPlayer().getUniqueId());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if (running && Timer.isRunning() && event.getEntity().getKiller() != null){
            UUID uuid = event.getEntity().getKiller().getUniqueId();
            if (event.getEntity().getKiller() != null && mob.containsKey(uuid) && mob.get(uuid) == event.getEntity().getType() && running && Timer.isRunning()){
                next(event.getEntity().getKiller().getUniqueId());
            }
        }
    }

    /*@EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        Player p2;
        if (running && Timer.isRunning()){
            for (UUID uuid : mob.keySet()) {
                if (uuid == p.getUniqueId()){
                    p2 = player;
                }
            }
        }

        if (running && Timer.isRunning() && active.containsValue(true)){
            ArrayList<Material> mat = new ArrayList<>();
            ArrayList<EntityType> entityTypes = new ArrayList<>();

            for (Material material : Material.values()) {
                if (material.isItem() && !material.name().contains("SPAWN_EGG")){
                    mat.add(material);
                }
            }

            for (EntityType ent : EntityType.values()) {
                if (ent.isAlive()){
                    entityTypes.add(ent);
                }
            }

            for (EntityType ent : blacklistMob) {
                entityTypes.remove(ent);
            }

            for (Material material : blacklistItem) {
                mat.remove(material);
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (active.get("mobs")){
                    mobs.put(player, entityTypes);
                }
                if (active.get("items")){
                    items.put(player, mat);
                }
                ItemStack item = Gui.setName(new ItemStack(Material.BARRIER), "joker", "§c§l JOKER");
                item.setAmount(joker);
                player.getInventory().addItem(item);
                createArmorstand(player);
                first(player);
            }
        }


    }*/

    /*@EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (armorStandHashMap.containsKey(event.getPlayer())){
            armorStandHashMap.get(event.getPlayer()).remove();
        }
    }*/



    /** ############################################################################################################################################################ **/


    private static Inventory createInv(String language){
        Inventory inv = Bukkit.createInventory(null, 5*9, (String) Lang.msg("menus.challenge.invs.force_battle.headline", language));

        for (int i = 0; i <= 8; i++) {
            inv.setItem(i, Gui.setName(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), "", " "));
        }
        for (int i = 9; i <= 44; i++) {
            inv.setItem(i, Gui.setName(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), "", " "));
        }
        inv.setItem(23,Gui.setName(new ItemStack(Material.GRASS_BLOCK), "item", "§6§l ITEM"));
        inv.setItem(21,Gui.setName(new ItemStack(Material.ALLAY_SPAWN_EGG), "mob", "§6§l MOB"));
        inv.setItem(32,reditem.get(language));
        inv.setItem(30, redmob.get(language));
        inv.setItem(36, Gui.setName(new ItemStack(Material.DARK_OAK_DOOR), "back", Lang.msg("menus.items.back.name", language).toString(), (List) Lang.msg("menus.items.back.lore", language)));

        inv.setItem(3, Gui.setName(new ItemStack(Material.CLOCK), "set", Lang.msg("menus.timer.items.set.name", language).toString(), (List) Lang.msg("menus.timer.items.set.lore", language)));
        inv.setItem(5, Gui.setName(new ItemStack(Material.BARRIER), "joker", Lang.msg("menus.timer.items.set.name", language).toString(), (List) Lang.msg("menus.timer.items.set.lore", language)));

        return inv;
    }

    public static HashMap getInvs() {
        return invs;
    }

    private static HashMap<UUID, ArrayList<Inventory>> createHistoryInv(String language){
        HashMap<UUID, ArrayList<Inventory>> inv = new HashMap<>();

        for (UUID uuid : history.keySet()) {
            Player p = Bukkit.getPlayer(uuid);
            for (int i = 0; i < history.get(uuid).size(); i++) {
                if (i % 28 == 0) {
                    Inventory inventory = Bukkit.createInventory(null, 5 * 9, (String) "Forced Items");
                    for (int x = 0; x <= 8; x++) {
                        inventory.setItem(x, Gui.setName(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), "", " "));
                    }
                    for (int x = 9; x <= 44; x++) {
                        inventory.setItem(x, Gui.setName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "", " "));
                    }
                    if (history.get(uuid).size() <= 28){
                        System.out.println("sesdedsed");
                    }else if (i/28 == 0){
                        inventory.setItem(26,Gui.setName(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "right", "-->"));
                    } else if (i/28 == history.get(uuid).size()/28){
                        inventory.setItem(18,Gui.setName(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "left", "<--"));
                    } else {
                        inventory.setItem(18,Gui.setName(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "left", "<--"));
                        inventory.setItem(26,Gui.setName(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "right", "-->"));
                    }
                    if (inv.get(uuid) == null){
                        inv.put(uuid, new ArrayList<Inventory>(Collections.singleton(inventory)));
                    } else{
                        inv.get(uuid).add(inventory);
                    }
                }
            }
        }


        return inv;
    }

    private static AnvilGUI.Builder createAnvilInv(String language){
        AnvilGUI.Builder builder = new AnvilGUI.Builder();

        builder.plugin(Main.getInstance());
        builder.itemLeft(new ItemStack(Material.CLOCK));
        builder.title((String) Lang.msg("menus.timer.anvil.title", language));
        builder.text((String) Lang.msg("menus.timer.anvil.placeholder", language));
        builder.onComplete((player, text) -> {
            try {
                if (text.contains(":") &text.length() <=5) {
                    String a = text.split("\\:")[0];
                    String b = text.split("\\:")[1];
                    endtime = (Integer.parseInt(a)*60) + Integer.parseInt(b);
                } else if (text.contains(":") & text.length() <=8){
                    String a = text.split("\\:")[0];
                    String b = text.split("\\:")[1];
                    String c = text.split("\\:")[2];
                    endtime = (Integer.parseInt(a)*3600) + (Integer.parseInt(b)*60) + Integer.parseInt(c);
                } else if (text.contains(":") & text.length() <=11){
                    String s = text.split("\\:")[0];
                    String m = text.split("\\:")[1];
                    String h = text.split("\\:")[2];
                    String d = text.split("\\:")[2];
                    endtime = (Integer.parseInt(m)*86400) + (Integer.parseInt(m)*3600) + (Integer.parseInt(h)*60) + Integer.parseInt(d);
                } else endtime = Integer.parseInt(text); AnvilGUI.Response.close();

                invs.forEach((s, inventory) -> {
                    ItemMeta m = inventory.getItem(3).getItemMeta();
                    ArrayList<String> lore = new ArrayList<>();
                    lore.addAll((List) Lang.msg("menus.timer.items.set.lore", s));
                    lore.add("");
                    lore.add(Lang.msg("menus.challenge.invs.force_battle.endtime", s) + text);
                    m.setLore(lore);
                    inventory.getItem(3).setItemMeta(m);

                });
            } catch (NumberFormatException e) {
                return AnvilGUI.Response.text((String) Lang.msg("menus.timer.anvil.error", language));
            }
            player.openInventory(invs.get(Lang.getLangName(player)));
            return AnvilGUI.Response.close();
        });

        return builder;
    }



    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {

        if (ForceBattle.getInvs().containsValue(event.getClickedInventory())){
            Player p = (Player) event.getWhoClicked();

            event.setCancelled(true);
            switch (event.getCurrentItem().getItemMeta().getLocalizedName()){
                case "item":
                    if (event.getCurrentItem().getType() == Material.RED_DYE || event.getCurrentItem().getType() == Material.LIME_DYE){
                        if (active.get("items")){
                            active.put("items", false);
                            invs.forEach((s, inventory) -> inventory.setItem(event.getSlot(), reditem.get(s)));
                        } else {
                            active.put("items", true);
                            invs.forEach((s, inventory) -> inventory.setItem(event.getSlot(), greenitem.get(s)));
                        }
                    }
                    break;
                case "mob":
                    if (event.getCurrentItem().getType() == Material.RED_DYE || event.getCurrentItem().getType() == Material.LIME_DYE){
                        if (active.get("mobs")){
                            active.put("mobs", false);
                            invs.forEach((s, inventory) -> inventory.setItem(event.getSlot(), redmob.get(s)));
                        } else {
                            active.put("mobs", true);
                            invs.forEach((s, inventory) -> inventory.setItem(event.getSlot(), greenmob.get(s)));
                        }
                    }
                    break;
                case "set":
                    anvilInvs.get(Lang.getLangName(p)).open(p);
                    break;
                case "joker":
                    if (event.getClick() == ClickType.LEFT && event.getCurrentItem().getAmount() < event.getCurrentItem().getMaxStackSize()){
                        joker ++;
                    } else if (event.getClick() == ClickType.RIGHT && event.getCurrentItem().getAmount() > 1){
                        joker --;
                    }
                    invs.forEach((s, inventory) -> {
                        ItemStack item = event.getCurrentItem();
                        item.setAmount(joker);
                        inventory.setItem(event.getSlot(), item);
                    });
                    break;
                case "back":
                    Gui.switchInventory(event.getClickedInventory(), (Inventory) ChallengeGUI.getInvs().get(Lang.getLangName(event.getWhoClicked())), p, "down");
                    break;
            }
        }
        historyinv.forEach((uuid, inventories) -> {
            if (inventories.contains(event.getClickedInventory())) {
                Player p = (Player) event.getWhoClicked();
                int inv = 0;
                for (int i = 0; i < inventories.size(); i++) {
                    if (inventories.get(i) == event.getClickedInventory()){
                        inv = i;
                    }
                }

                event.setCancelled(true);
                switch (event.getCurrentItem().getItemMeta().getLocalizedName()) {
                    case "left":
                            Gui.switchInventory(event.getClickedInventory() ,inventories.get(inv - 1), p, "left");
                        break;
                    case "right":
                        Gui.switchInventory(event.getClickedInventory() ,inventories.get(inv + 1), p, "right");
                        break;
                    case "set":

                        break;
                    case "back":

                        break;
                }
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (item.getItemMeta() != null && item.getItemMeta().getLocalizedName() != null && running){
            if (item.getItemMeta().getLocalizedName().equalsIgnoreCase("skip")){
                event.setCancelled(true);
            }else if (item.getItemMeta().getLocalizedName().equalsIgnoreCase("open")){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item.getItemMeta() != null && item.getItemMeta().getLocalizedName() != null && running){
            if (item.getItemMeta().getLocalizedName().equalsIgnoreCase("skip")){
                event.setCancelled(true);
            } else if (item.getItemMeta().getLocalizedName().equalsIgnoreCase("open")){
                event.setCancelled(true);
            } else if (item.getItemMeta().getLocalizedName().equalsIgnoreCase("joker")){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler()
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();

        if (item != null) {
            if (item.getItemMeta() != null && item.getItemMeta().getLocalizedName() != null && running){
                if (item.getItemMeta().getLocalizedName().equalsIgnoreCase("skip")){
                    if (tempHistoryMap.size() == 0){
                        event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                    }
                    event.setCancelled(true);
                    result();
                }else if (item.getItemMeta().getLocalizedName().equalsIgnoreCase("open")){
                    player.openInventory(historyinv.get(historyPlayer).get(0));
                    event.setCancelled(true);
                }else if (item.getItemMeta().getLocalizedName().equalsIgnoreCase("joker") && Timer.isRunning()){
                    if (item.getAmount() > 0){
                        item.setAmount(item.getAmount() - 1);
                    } else event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                    next(event.getPlayer().getUniqueId());
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (armorStandHashMap.containsKey(p.getUniqueId()) && running){
            if (p.getLocation().getBlock().getType() == Material.NETHER_PORTAL || p.getLocation().getBlock().getType() == Material.END_PORTAL || p.getLocation().getBlock().getType() == Material.END_GATEWAY){
                if (p.getPassengers().contains(armorStandHashMap.get(p.getUniqueId()))){
                    p.removePassenger(armorStandHashMap.get(p.getUniqueId()));
                    return;
                }
            } else {
                if (!p.getPassengers().contains(armorStandHashMap.get(p.getUniqueId()))) {
                    armorStandHashMap.get(p.getUniqueId()).teleport(p.getLocation());
                    p.setPassenger(armorStandHashMap.get(p.getUniqueId()));
                    return;
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (running && armorStandHashMap.containsKey(uuid)){
            armorStandHashMap.get(uuid).teleport(p.getLocation());
            p.setPassenger(armorStandHashMap.get(uuid));
        }
    }

    /*@EventHandler(ignoreCancelled = true)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        if (running && armorStandHashMap.containsKey(event.getEntity())){
        }
    }*/


}
