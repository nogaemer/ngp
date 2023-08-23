package de.nogaemer.ngp.challenges;

import de.nogaemer.ngp.Main;
import de.nogaemer.ngp.utils.ChallengeFileManager;
import de.nogaemer.ngp.utils.enums.DropedXp;
import de.nogaemer.ngp.utils.items.Item;
import de.nogaemer.ngp.utils.menues.gui.Gui;
import de.nogaemer.ngp.utils.menues.gui.InvByList;
import de.nogaemer.ngp.utils.menues.gui.challenges.ChallengeGUI;
import de.nogaemer.ngp.utils.msg.lang.Lang;
import de.nogaemer.ngp.utils.enums.LootTables;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootContext;

import java.util.*;

public class Randomizer extends Inv implements Listener {
    public static boolean running = false;
    public static boolean drop = false;
    public static boolean interaction = false;
    String localiedDropName = "localiedDropName";
    static ItemStack openInv = Gui.setName(new ItemStack(Material.BUNDLE), "randomizer.openinv", "§6MOB");

    static final Mobs mobs = new Mobs();
    public static final InvByList entityitems = new InvByList();
    public static ArrayList<EntityType> explored = new ArrayList<>();

    public static void setup(){
        Inv.setup();
        loadGame();
    }

    public static void setRunning(boolean running) {
        if (running) {
            invs.forEach((s, inventory) -> inventory.setItem(31, challenge_green.get(s)));
            ChallengeGUI.switchRunning("randomizer", true);
            Randomizer.running = true;
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.getInventory().addItem(openInv);
            }
        } else {
            invs.forEach((s, inventory) -> inventory.setItem(31, challenge_red.get(s)));
            ChallengeGUI.switchRunning("randomizer", false);
            Randomizer.running = false;
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.getInventory().remove(openInv);
            }
        }
    }

    public static void saveGame(){
        mobs.saveMobs();
        ChallengeFileManager.getCfg().set("Randomizer.exploredmobs", entityToString(explored));

        ChallengeFileManager.saveAllFiles();
    }

    public static void loadGame(){
        mobs.loadMobs();

        //load Explored
        explored = stringToEntity((ArrayList<String>) ChallengeFileManager.getCfg().getList("Randomizer.exploredmobs"));
        if (explored == null){
            explored = new ArrayList<EntityType>();
        }

        for (EntityType entityType : explored) {
            entityitems.addItems(createHeadItem(entityType, mobs.getMob(entityType)));
        }
    }

    public static void resetGame(){
        mobs.resetMobs();
        explored.clear();
        entityitems.clear();
    }

    public static ArrayList<String> entityToString(ArrayList<EntityType> entity){
        if (entity == null)
            return null;

        ArrayList<String> names = new ArrayList<>();
        for (EntityType entityType : entity) {
            names.add(entityType.name());
        }
        return  names;
    }

    public static ArrayList<EntityType> stringToEntity(ArrayList<String> names){
        if (names == null)
            return null;

        ArrayList<EntityType> entitys = new ArrayList<>();
        for (String name : names) {
            entitys.add(EntityType.valueOf(name));
        }
        return entitys;
    }

    public static HashMap getInvs() {
        return Inv.invs;
    }

    public static ArrayList<ItemStack> createHeadItem(EntityType type, EntityType finaltype){
        return checkDouble(entityitems.getItems(), new ArrayList<ItemStack>(
                Collections.singleton(Gui.getEntityHeads(type, "§6§l" + Gui.nameToString(finaltype.name()),
                        List.of(Lang.msg("items.challenges.randomizer.mobhead.lore", "DE_DE") + " " + Gui.nameToString(type.name())),
                        "head"))));
    }

    public static ArrayList<ItemStack> checkDouble(ArrayList<ItemStack> mainList,ArrayList<ItemStack> addList){
        ArrayList<ItemStack> check = new ArrayList<ItemStack>(addList);
        for (int i = 0; i < check.size(); i++) {
            for (ItemStack mainItem : mainList){
                for (ItemStack addItem : addList){
                    if (Item.isSimilar(mainItem,addItem)){
                        check.remove(addItem);
                    }
                }
            }
        }

        return check;
    }

    //Drop Listener
    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        EntityType type = entity.getType();
        EntityType finaltype = mobs.getMob(type);

        if (mobs.containsKey(type) && running){
            if (event.getEntity().getKiller() != null){
                Player p = event.getEntity().getKiller();

                if (!explored.contains(entity.getType())){
                    explored.add(entity.getType());
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("§9" + Gui.nameToString(finaltype.name()), "§6" + Gui.nameToString(type.name()) + "§7 ->  " + "§6" + Gui.nameToString(finaltype.name())));
                }

                //Inventory
                entityitems.addItems(createHeadItem(type,finaltype));

                //XP
                int xp = DropedXp.valueOf(finaltype.name()).getXp(event.getEntity());
                event.setDroppedExp(xp);

                //loottable generator

                Location location = p.getLocation();
                int looting_mod = 0;
                double luck_mod = p.getAttribute(Attribute.GENERIC_LUCK).getValue();
                LootTables table = LootTables.valueOf(finaltype.name());;
                /*switch (finaltype.name()){
                    case "MUSHROOM_COW":
                        table = LootTables.MUSHROOM_COW;
                        break;
                    case "SNOW_GOLEM":
                        table = LootTables.SNOWMAN;
                        break;
                    default:

                }*/

                if (p.getInventory().getItemInMainHand() != null)
                {
                    looting_mod = p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
                }
                if (looting_mod == 0 && p.getInventory().getItemInOffHand() != null)
                {
                    looting_mod = p.getInventory().getItemInOffHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
                }

                // LootTable builder
                LootContext.Builder builder = new LootContext.Builder(location);
                builder.lootedEntity(event.getEntity());
                builder.lootingModifier(looting_mod);
                builder.luck((float) luck_mod);
                builder.killer(p);
                LootContext lootContext = builder.build();
                Collection<ItemStack> drops = LootTables.valueOf(finaltype.name()).getLootTable().populateLoot(new Random(), lootContext);
                for (ItemStack drop : drops) {
                    ItemMeta m = drop.getItemMeta();
                    m.setLocalizedName(localiedDropName);
                    drop.setItemMeta(m);
                }

                event.getDrops().clear();
                event.getDrops().addAll(drops);


                //EnderDragon XP
                if (finaltype == EntityType.ENDER_DRAGON){
                    Main.getFileManager().getCfg().set("firstEnderdragon", false);
                    Main.getFileManager().saveAllFiles();
                }
            } else {

                // LootTable builder
                LootContext.Builder builder = new LootContext.Builder(entity.getLocation());
                builder.lootedEntity(event.getEntity());
                ;
                LootContext lootContext = builder.build();
                Collection<ItemStack> drops = LootTables.valueOf(finaltype.name()).getLootTable().populateLoot(new Random(), lootContext);
                for (ItemStack drop : drops) {
                    ItemMeta m = drop.getItemMeta();
                    m.setLocalizedName(localiedDropName);
                    drop.setItemMeta(m);
                }

                event.getDrops().clear();
                event.getDrops().addAll(drops);


                //EnderDragon XP
                if (finaltype == EntityType.ENDER_DRAGON) {
                    Main.getFileManager().getCfg().set("firstEnderdragon", false);
                    Main.getFileManager().saveAllFiles();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if (!running)
            return;
        if (event.getItem() != null && event.getItem().isSimilar(openInv)){
            if (entityitems.getInvs().size() == 0){
                event.getPlayer().sendMessage("§l§c Sie müssen erst ein Mob töten");
                return;
            }
            event.getPlayer().openInventory(entityitems.getInvs().get(0));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (!running || !drop)
            return;

        String name = event.getItem().getItemStack().getItemMeta().getLocalizedName();
        if (!(name.equals(localiedDropName) || name.equals("randomizer.openinv"))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraftItem(CraftItemEvent event) {
        if (!running || !interaction)
            return;

        ItemStack item = event.getCurrentItem();
        ItemMeta m = item.getItemMeta();
        m.setLocalizedName(localiedDropName);
        item.setItemMeta(m);
        event.setCurrentItem(item);

        int amount = 0;
        int content_amount = 0;
        if (event.isShiftClick()){
            for (ItemStack content : event.getInventory().getContents()) {
                if (content == null || content.getType() == event.getInventory().getResult().getType())
                    continue;

                if (amount == 0){
                    amount = content.getAmount();
                } else if (content.getAmount() != 0 & amount > content.getAmount()){
                    amount = content.getAmount();
                }
            }
            item.setAmount(amount);
            event.setCurrentItem(item);
        }
        for (ItemStack content : event.getInventory().getContents()) {
            if (content.getType() == Material.AIR || content.getType() == event.getInventory().getRecipe().getResult().getType())
                continue;

            content_amount = 0;
            content_amount = (content.getAmount() - amount);
            content.setAmount(content_amount + 1);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        if (!running || !interaction)
            return;

        ItemStack item = event.getResult();
        ItemMeta m = item.getItemMeta();
        m.setLocalizedName(localiedDropName);
        item.setItemMeta(m);
        event.setResult(item);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        if (!running || !interaction)
            return;

        ItemStack item = event.getResult();
        ItemMeta m = item.getItemMeta();
        m.setLocalizedName(localiedDropName);
        item.setItemMeta(m);
        event.setResult(item);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPrepareAnvil(SmithItemEvent event) {
        if (!running || !interaction)
            return;
        
        ItemStack item = event.getCurrentItem();
        ItemMeta m = item.getItemMeta();
        m.setLocalizedName(localiedDropName);
        item.setItemMeta(m);
        event.setCurrentItem(item);
    }

    //Mob MENU
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (entityitems.getInvs().contains(event.getClickedInventory())) {
            Player p = (Player) event.getWhoClicked();
            int inv = 0;
            for (int i = 0; i < entityitems.getInvs().size(); i++) {
                if (entityitems.getInvs().get(i) == event.getClickedInventory()) {
                    inv = i;
                }
            }

            event.setCancelled(true);
                switch (event.getCurrentItem().getItemMeta().getLocalizedName()) {
                    case "last":
                        if (inv > 0) {
                            Gui.switchSmallInventory(event.getClickedInventory(), entityitems.getInvs().get(inv - 1), p, "left");
                        }
                        break;
                    case "next":
                        if (entityitems.getInvs().size() - 1 > inv) {
                            Gui.switchSmallInventory(event.getClickedInventory(), entityitems.getInvs().get(inv + 1), p, "right");
                        }
                        break;
                    case "set":

                        break;
                    case "back":

                        break;
                }
        }
        if (running && event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR && interaction){
            ArrayList<InventoryType> invs = new ArrayList<>();
            invs.add(InventoryType.SHULKER_BOX);
            invs.add(InventoryType.BARREL);
            invs.add(InventoryType.BLAST_FURNACE);
            invs.add(InventoryType.BEACON);
            invs.add(InventoryType.BREWING);
            invs.add(InventoryType.HOPPER);
            invs.add(InventoryType.CHEST);
            invs.add(InventoryType.DISPENSER);
            invs.add(InventoryType.DROPPER);
            if (invs.contains(event.getClickedInventory().getType())){
                if (event.getCurrentItem().getItemMeta() == null){
                    event.setCancelled(true);
                } else if (event.getCurrentItem().getItemMeta().getLocalizedName() == null || event.getCurrentItem().getItemMeta().getLocalizedName() != localiedDropName)
                    event.setCancelled(true);
            }
            if (event.getCurrentItem().getItemMeta().getLocalizedName().equals(openInv.getItemMeta().getLocalizedName()) || event.getCurrentItem().getItemMeta().getLocalizedName().equals(localiedDropName)){
                event.setCancelled(false);
            }
        }
    }
}

class Mobs {
    private HashMap<EntityType, EntityType> mobs = new HashMap<EntityType, EntityType>();

    public Mobs() {
    }

    public void resetMobs(){
        ArrayList<EntityType> mob = new ArrayList<EntityType>();
        for (EntityType value : EntityType.values()) {
            if (value.isAlive()){
                mob.add(value);
            }
        }
        mob.remove(EntityType.PLAYER);
        ArrayList<EntityType> shufflemob = new ArrayList<EntityType>(mob);
        Collections.shuffle(shufflemob);

        for (int i = 0; i < mob.size(); i++) {
            mobs.put(mob.get(i), shufflemob.get(i));
        }
    }

    public void loadMobs(){
        if (ChallengeFileManager.getCfg().getConfigurationSection("Randomizer.mobs").getKeys(false)  == null){
            resetMobs();
            return;
        }
        mobs = loadHashMap();
    }

    public void saveMobs(){
        saveHashMap(mobs);
    }

    public EntityType getMob(String name){
        return mobs.get(EntityType.valueOf(name));
    }

    public EntityType getMob(EntityType entity){
        return mobs.get(entity);
    }

    public boolean containsValue(EntityType entity){
        return mobs.containsValue(entity);
    }

    public boolean containsKey(EntityType entity){
        return mobs.containsKey(entity);
    }

    public void saveHashMap(HashMap<EntityType, EntityType> hm) {
        for (EntityType key : hm.keySet()) {
            ChallengeFileManager.getCfg().set("Randomizer.mobs."+key.name(), hm.get(key).name());
        }
        ChallengeFileManager.saveAllFiles();
    }

    public HashMap<EntityType, EntityType> loadHashMap() {
        HashMap<EntityType, EntityType> hm = new HashMap<EntityType, EntityType>();
        for (String key : ChallengeFileManager.getCfg().getConfigurationSection("Randomizer.mobs").getKeys(false)) {
            hm.put(EntityType.valueOf(key), EntityType.valueOf(ChallengeFileManager.getCfg().getString("Randomizer.mobs."+key)));
        }
        return hm;
    }
}

class Inv implements Listener {
    static final HashMap<String, Inventory> invs = new HashMap<String, Inventory>();

    static HashMap<String, ItemStack> challenge_red;
    static HashMap<String, ItemStack> challenge_green;

    static HashMap<String, ItemStack> drop_red;
    static HashMap<String, ItemStack> drop_green;

    static HashMap<String, ItemStack> interaction_red;
    static HashMap<String, ItemStack> interaction_green;

    public static void setup(){
        Bukkit.getPluginManager().registerEvents(new Inv(), Main.getInstance());
        challenge_green = Gui.setNames(Material.LIME_CONCRETE, "randomizer.mob", "menus.items.activated.name", "menus.items.activated.lore");
        challenge_red = Gui.setNames(Material.RED_CONCRETE, "randomizer.mob", "menus.items.deactivated.name", "menus.items.deactivated.lore");

        drop_red = Gui.setNames(Material.RED_CONCRETE, "randomizer.drop", "menus.items.activated.name", "menus.items.activated.lore");
        drop_green = Gui.setNames(Material.LIME_CONCRETE, "randomizer.drop", "menus.items.deactivated.name", "menus.items.deactivated.lore");

        interaction_red = Gui.setNames(Material.RED_CONCRETE, "randomizer.interaction", "menus.items.activated.name", "menus.items.activated.lore");
        interaction_green = Gui.setNames(Material.LIME_CONCRETE, "randomizer.interaction", "menus.items.deactivated.name", "menus.items.deactivated.lore");

        invs.put("EN_US", createInv("EN_US"));
        invs.put("DE_DE", createInv("DE_DE"));


    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (invs.containsValue(event.getClickedInventory())){
            Player p = (Player) event.getWhoClicked();
            switch (event.getCurrentItem().getItemMeta().getLocalizedName()) {
                case "randomizer.mob" -> Randomizer.setRunning(!Randomizer.running);
                case "randomizer.drop" -> {
                    if (Randomizer.drop) {
                        invs.forEach((s, inventory) -> inventory.setItem(29, drop_red.get(s)));
                        Randomizer.drop = false;
                    } else {
                        invs.forEach((s, inventory) -> inventory.setItem(29, drop_green.get(s)));
                        Randomizer.drop = true;
                    }
                }
                case "randomizer.interaction" -> {
                    if (Randomizer.interaction) {
                        invs.forEach((s, inventory) -> inventory.setItem(33, drop_red.get(s)));
                        Randomizer.interaction = false;
                    } else {
                        invs.forEach((s, inventory) -> inventory.setItem(33, drop_green.get(s)));
                        Randomizer.interaction = true;
                    }
                }
                case "back" -> Gui.switchInventory(event.getClickedInventory(), (Inventory) ChallengeGUI.getInvs().get(Lang.getLangName(event.getWhoClicked())), p, "down");
                case "randomizer.reset" -> Randomizer.resetGame();
            }

            event.setCancelled(true);
        }
    }

    private static Inventory createInv(String language){
        Inventory inv = Bukkit.createInventory(null, 5*9, (String) Lang.msg("menus.challenge.invs.force_battle.headline", language));

        for (int i = 0; i <= 8; i++)
            inv.setItem(i, Gui.setName(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), "", " "));
        for (int i = 9; i <= 44; i++)
            inv.setItem(i, Gui.setName(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), "", " "));

        inv.setItem(20,Gui.setName(new ItemStack(Material.DIRT), "randomizer.drop", "§6§l DROPS"));
        inv.setItem(29, drop_red.get(language));

        inv.setItem(22,Gui.setName(new ItemStack(Material.ALLAY_SPAWN_EGG), "randomizer.mob", "§6§l MOB"));
        inv.setItem(31, challenge_red.get(language));

        inv.setItem(24,Gui.setName(new ItemStack(Material.CHEST), "randomizer.interaction", "§6§l INTERACTION"));
        inv.setItem(33, interaction_red.get(language));

        inv.setItem(36, Gui.setName(new ItemStack(Material.DARK_OAK_DOOR), "back", Lang.msg("menus.items.back.name", language).toString(), (List) Lang.msg("menus.items.back.lore", language)));
        inv.setItem(4, Gui.setName(new ItemStack(Material.BARRIER), "randomizer.reset", Lang.msg("menus.challenge.invs.randomizer.reset.name", language).toString(), (List) Lang.msg("menus.challenge.invs.randomizer.reset.lore", language)));
        return inv;
    }

    public static HashMap getInvs() {
        return invs;
    }
}
