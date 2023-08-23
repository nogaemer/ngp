//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.nogaemer.ngp.utils.enums;

import de.nogaemer.ngp.Main;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.Random;


public enum DropedXp {
    ELDER_GUARDIAN(EntityType.ELDER_GUARDIAN, 10, 0, false, false),
    WITHER_SKELETON(EntityType.WITHER_SKELETON, 5, 0, true, false),
    STRAY(EntityType.STRAY, 5, 0, true, false),
    HUSK(EntityType.HUSK, 5, 0, true, true),
    ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER, 5, 0, true, false),
    SKELETON_HORSE(EntityType.SKELETON_HORSE, 1, 2, false, false),
    ZOMBIE_HORSE(EntityType.ZOMBIE_HORSE, 1, 2, false, false),
    ARMOR_STAND(EntityType.ARMOR_STAND, 5, 0, false, false),
    DONKEY(EntityType.DONKEY, 1, 2, false, false),
    MULE(EntityType.MULE, 5, 0, false, false),
    EVOKER(EntityType.EVOKER, 10, 0, false, false),
    VEX(EntityType.VEX, 5, 0, true, false),
    VINDICATOR(EntityType.VINDICATOR, 5, 0, true, false),
    ILLUSIONER(EntityType.ILLUSIONER, 5, 0, true, false),
    CREEPER(EntityType.CREEPER, 5, 0, true, false),
    SKELETON(EntityType.SKELETON, 5, 0, true, false),
    SPIDER(EntityType.SPIDER, 5, 0, true, false),
    GIANT(EntityType.GIANT, 5, 0, false, false),
    ZOMBIE(EntityType.ZOMBIE, 5, 0, true, true),
    SLIME(EntityType.SLIME, 4, 0, false, false),
    GHAST(EntityType.GHAST, 5, 0, true, false),
    ZOMBIFIED_PIGLIN(EntityType.ZOMBIFIED_PIGLIN, 5, 0, true, true),
    ENDERMAN(EntityType.ENDERMAN, 5, 0, true, false),
    CAVE_SPIDER(EntityType.CAVE_SPIDER, 5, 0, true, false),
    SILVERFISH(EntityType.SILVERFISH, 5, 0, true, false),
    BLAZE(EntityType.BLAZE, 10, 0, false, false),
    MAGMA_CUBE(EntityType.MAGMA_CUBE, 4, 0, false, false),
    ENDER_DRAGON(EntityType.ENDER_DRAGON, 12000, 0, false, false),
    WITHER(EntityType.WITHER, 50, 0, false, false),
    BAT(EntityType.BAT, 0, 0, false, false),
    WITCH(EntityType.WITCH, 5, 0, true, false),
    ENDERMITE(EntityType.ENDERMITE, 3, 0, false, false),
    GUARDIAN(EntityType.GUARDIAN, 10, 0, false, false),
    SHULKER(EntityType.SHULKER, 5, 0, true, false),
    PIG(EntityType.PIG, 1, 2, false, false),
    SHEEP(EntityType.SHEEP, 1, 2, false, false),
    COW(EntityType.COW, 1, 2, false, false),
    CHICKEN(EntityType.CHICKEN, 1, 2, false, false),
    SQUID(EntityType.SQUID, 1, 2, false, false),
    WOLF(EntityType.WOLF, 1, 2, false, false),
    MUSHROOM_COW(EntityType.MUSHROOM_COW, 1, 2, false, false),
    SNOWMAN(EntityType.SNOWMAN, 0, 0, false, false),
    OCELOT(EntityType.OCELOT, 1, 2, false, false),
    IRON_GOLEM(EntityType.IRON_GOLEM, 0, 0, false, false),
    HORSE(EntityType.HORSE, 1, 2, false, false),
    RABBIT(EntityType.RABBIT, 1, 2, false, false),
    POLAR_BEAR(EntityType.POLAR_BEAR, 1, 2, false, false),
    LLAMA(EntityType.LLAMA, 1, 2, false, false),
    PARROT(EntityType.PARROT, 1, 2, false, false),
    VILLAGER(EntityType.VILLAGER, 0, 0, false, false),
    TURTLE(EntityType.TURTLE, 1, 2, false, false),
    PHANTOM(EntityType.PHANTOM, 5, 0, true, false),
    COD(EntityType.COD, 1, 2, false, false),
    SALMON(EntityType.SALMON, 1, 2, false,  false),
    PUFFERFISH(EntityType.PUFFERFISH, 1, 2, false, false),
    TROPICAL_FISH(EntityType.TROPICAL_FISH, 1, 2, false, false),
    DROWNED(EntityType.DROWNED, 5, 0, true, false),
    DOLPHIN(EntityType.DOLPHIN, 1, 2, false, false),
    CAT(EntityType.CAT, 1, 2, false, false),
    PANDA(EntityType.PANDA, 1, 2, false, false),
    PILLAGER(EntityType.PILLAGER, 5, 0, true, false),
    RAVAGER(EntityType.RAVAGER, 20, 0, false, false),
    TRADER_LLAMA(EntityType.TRADER_LLAMA, 1, 2, false, false),
    WANDERING_TRADER(EntityType.WANDERING_TRADER, 0, 0, false, false),
    FOX(EntityType.FOX, 1, 2, false, false),
    BEE(EntityType.BEE, 1, 2, false, false),
    HOGLIN(EntityType.HOGLIN, 5, 0, true, false),
    PIGLIN(EntityType.PIGLIN, 5, 0, true, false),
    STRIDER(EntityType.STRIDER, 1, 1, false, false),
    ZOGLIN(EntityType.ZOGLIN, 5, 0, true, false),
    PIGLIN_BRUTE(EntityType.PIGLIN_BRUTE, 20, 0, false, false),
    AXOLOTL(EntityType.AXOLOTL, 1, 2, false, false),
    GLOW_SQUID(EntityType.GLOW_SQUID, 1, 2, false, false),
    GOAT(EntityType.GOAT, 1, 2, false, false),
    ALLAY(EntityType.ALLAY, 0, 0, false, false),
    FROG(EntityType.FROG, 1, 2, false, false),
    TADPOLE(EntityType.TADPOLE, 0, 0, false, false),
    WARDEN(EntityType.WARDEN, 5, 0, true, false),
    UNKNOWN(null, 0, 0, false, false);


    private final EntityType entityType;
    private final int xpDropped;
    private final int range;
    private final boolean equip;
    private final boolean hasAdult;

    DropedXp(EntityType entityType, int xpDropped, int range, boolean equip, boolean hasAdult) {
        this.entityType = entityType;
        this.xpDropped = xpDropped;
        this.range = range;
        this.equip = equip;
        this.hasAdult = hasAdult;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public int getXp(LivingEntity entity) {
        boolean isAdult = true;
        int equippedPieces = 0;
        int Xp = randomInt(xpDropped, xpDropped + (range + 1));

        if (entity instanceof Ageable){
            Ageable ageable = (Ageable) entity;
            isAdult = ageable.isAdult();
        }

        if (entity.getEquipment() != null){
            for (ItemStack armorContent : entity.getEquipment().getArmorContents()) {
                if (armorContent.getType() != Material.AIR){
                    equippedPieces ++;
                }
            }
            if (entity.getEquipment().getItemInMainHand().getType() != Material.AIR){
                equippedPieces ++;
            }
            if (entity.getEquipment().getItemInOffHand().getType() != Material.AIR){
                equippedPieces ++;
            }
        }

        if (equippedPieces != 0 && this.equip){
            for (int i = 0; i < equippedPieces; i++) {
                Xp += randomInt(1, 4);
            }
        }

        if (!isAdult && this.hasAdult) {
            Xp += randomInt(1, 4);
        }

        if (!isAdult && !this.hasAdult)
            return 0;

        if (entity.getKiller() == null || !(entity.getKiller() instanceof Player)){
            return 0;
        }

        if (entity instanceof Slime && (this.getEntityType() == EntityType.SLIME || this.getEntityType() == EntityType.MAGMA_CUBE)){
            Slime slime = (Slime) entity;
            return slime.getSize();
        }

        if (this.getEntityType() == EntityType.ENDER_DRAGON){
            if (Main.getFileManager().getCfg().getBoolean("firstEnderdragon")){
                System.out.println(true);
                return 12000;
            }
            System.out.println(Main.getFileManager().getCfg().getBoolean("firstEnderdragon"));
            System.out.println(Main.getFileManager().getCfg().get("firstEnderdragon"));
            return 500;
        }

        return Xp;
    }

    public int getXp(boolean equip, boolean baby, int size, boolean firstSpawn) {
        if (range == 0){
            return xpDropped;
        }
        return randomInt(xpDropped, xpDropped + (range + 1));
    }



    private int randomInt(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }
}
