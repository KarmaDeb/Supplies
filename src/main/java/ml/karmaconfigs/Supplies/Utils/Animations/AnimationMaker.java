package ml.karmaconfigs.Supplies.Utils.Animations;

import ml.karmaconfigs.Supplies.Suministry;
import ml.karmaconfigs.Supplies.Utils.BeaconChest;
import ml.karmaconfigs.Supplies.Utils.Files.Config;
import ml.karmaconfigs.Supplies.Utils.Files.Message;
import ml.karmaconfigs.Supplies.Utils.Holograms;
import ml.karmaconfigs.Supplies.Utils.Particles.ParticleFixer;
import ml.karmaconfigs.Supplies.Utils.Particles.ParticleType;
import ml.karmaconfigs.Supplies.Utils.SoundFixer;
import ml.karmaconfigs.Supplies.Utils.StringUtils;
import ml.karmaconfigs.Supplies.Utils.Suministries.SupplyLoader;
import org.bukkit.*;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Private GSA code
 *
 * The use of this code
 * without GSA team authorization
 * will be a violation of
 * terms of use determined
 * in <a href="https://karmaconfigs.ml/license/"> here </a>
 */
@SuppressWarnings("all")
public class AnimationMaker implements Suministry {

    private final Item item;

    private static List<Beacon> chests = new ArrayList<>();

    /**
     * Initialize the animation maker
     *
     * @param item the item where to play the animation
     */
    public AnimationMaker(Item item) {
        this.item = item;
    }

    private EulerAngle toAngle(int X, int Y, int Z) {
        return new EulerAngle(Math.toRadians(X), Math.toRadians(Y), Math.toRadians(Z));
    }

    /**
     * Play the falling chest animation
     *
     * @param name the chest name
     * @param player the player who call it
     */
    public void startAnimationFor(String name, Player player) {
        Location location = item.getLocation();

        if (Config.playAnimation) {

            location.setYaw(location.getYaw() + 180);

            ArmorStand stand = location.getWorld().spawn(location.add(0D, 50D, 0D), ArmorStand.class);
            stand.setArms(true);
            stand.setBasePlate(false);
            stand.setGravity(true);
            stand.setNoDamageTicks(20 * 1000000000);
            stand.setBodyPose(toAngle(118, 0, 0));
            stand.setLeftArmPose(toAngle(128, 140, 0));
            stand.setRightArmPose(toAngle(128, 220, 0));
            stand.setLeftLegPose(toAngle(177, 0, 0));
            stand.setRightLegPose(toAngle(177, 0, 0));

            int rand = new Random().nextInt(1000000);

            stand.setMetadata("Suministry", new FixedMetadataValue(plugin, "Suministry_" + rand));

            ItemStack head = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);

            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwner("Chest");

            head.setItemMeta(meta);

            stand.setHelmet(head);

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Entity entity : stand.getWorld().getEntities()) {
                        if (entity.getEntityId() == stand.getEntityId()) {
                            World world = entity.getLocation().getWorld();
                            double X = entity.getLocation().getX();
                            double Y = entity.getLocation().getY() - 1D;
                            double Z = entity.getLocation().getZ();

                            Location location1 = new Location(world, X, Y, Z);

                            for (Block block : getNearbyBlocks(location1)) {
                                for (int i = 0; i < 3; i++) {
                                    for (Player player : plugin.getServer().getOnlinePlayers()) {
                                        ParticleFixer fixer = new ParticleFixer(ParticleType.SMOKE);
                                        fixer.sendParticle(block.getLocation());
                                    }
                                }
                            }

                            if (!location1.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) {
                                cancel();
                                Location highestY = trySummonChest(location1);

                                highestY.setYaw(highestY.getYaw() + 180);

                                Block highest = highestY.getBlock();

                                for (Player player : plugin.getServer().getOnlinePlayers()) {
                                    ParticleFixer fixer = new ParticleFixer(ParticleType.EXPLOSION);
                                    fixer.sendParticle(stand.getLocation());
                                }

                                if (highest.getRelative(BlockFace.EAST).getType().equals(Material.CHEST)) {
                                    highest.getLocation().setX(highest.getLocation().getX() + 2D);
                                }

                                highest.setType(Material.BEACON);

                                Firework firework = highest.getWorld().spawn(highestY, Firework.class);
                                FireworkMeta fwMeta = firework.getFireworkMeta();

                                int r = new Random().nextInt(255);
                                int g = new Random().nextInt(255);
                                int b = new Random().nextInt(255);

                                int r2 = new Random().nextInt(255);
                                int g2 = new Random().nextInt(255);
                                int b2 = new Random().nextInt(255);

                                fwMeta.addEffect(FireworkEffect.builder().withColor(Color.fromBGR(r, g, b)).trail(true).flicker(true)
                                        .withFade(Color.fromBGR(r2, g2, b2)).with(FireworkEffect.Type.STAR).build());

                                fwMeta.setPower(2);

                                firework.setFireworkMeta(fwMeta);

                                BeaconChest chest = new BeaconChest((Beacon) highest.getLocation().getBlock().getState());

                                for (ml.karmaconfigs.Supplies.Utils.Suministries.Suministry suministry : new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries()) {
                                    if (suministry.isGrenade(name)) {

                                        SupplyLoader saver = new SupplyLoader(chest.getLocation());

                                        saver.saveLoc(suministry, player);

                                        ItemStack barrier = new ItemStack(Material.BARRIER, 1);
                                        ItemMeta barrierMeta = barrier.getItemMeta();

                                        barrierMeta.setDisplayName(StringUtils.toColor("&cREMOVE"));

                                        barrier.setItemMeta(barrierMeta);

                                        ItemStack[] stacks = suministry.getContents();

                                        chest.setInventory(stacks, suministry.getPercentage());

                                        if (Suministry.hasHolographic()) {
                                            Holograms holo = new Holograms(chest.getChest(), Message.hologram(player, suministry.getName()));

                                            World w = chest.getLocation().getWorld();
                                            double x = chest.getLocation().getX() + 0.5;
                                            double y = chest.getLocation().getY() + 1.65;
                                            double z = chest.getLocation().getZ() + 0.5;

                                            Location loc = new Location(w, x, y, z);

                                            holo.createHologram(loc);
                                        }

                                        SoundFixer.ENTITY_GENERIC_EXPLODE.playSound(chest.getLocation(), 0.5F, 0.1F);

                                        for (Entity ent : chest.getLocation().getWorld().getNearbyEntities(chest.getLocation(), 10, 10, 10)) {
                                            ent.setVelocity(chest.getLocation().toVector().subtract(ent.getLocation().toVector()).normalize().multiply(-4));
                                        }

                                        chests.add(chest.getChest());

                                        for (Block block : getNearbyBlocks(chest.getLocation())) {

                                            if (block != null) {
                                                if (!block.getType().equals(Material.AIR)) {
                                                    float x = (float) -0.5 + (float) (Math.random() * ((0.5 - -0.5) + 1));
                                                    float y = (float) -1 + (float) (Math.random() * ((1 - -1) + 1));
                                                    float z = (float) -0.5 + (float) (Math.random() * ((0.5 - -0.5) + 1));

                                                    try {
                                                        Item dropped = block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(block.getType()));
                                                        dropped.setVelocity(new Vector(x, y, z));

                                                        dropped.setPickupDelay(20 * 100000000);

                                                        new BukkitRunnable() {
                                                            @Override
                                                            public void run() {
                                                                if (!dropped.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) {
                                                                    cancel();

                                                                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                                                                        SoundFixer.ENTITY_CHICKEN_EGG.playSound(chest.getLocation(), 0.5F, 2.0F);
                                                                        ParticleFixer cloud = new ParticleFixer(ParticleType.CLOUD);
                                                                        for (int ii = 0; ii < 5; ii++) {
                                                                            cloud.sendParticle(dropped.getLocation());
                                                                        }
                                                                        dropped.remove();
                                                                    }, 20 * 3);
                                                                }
                                                            }
                                                        }.runTaskTimer(plugin, 0, 1);
                                                    } catch (IllegalArgumentException ignore) {
                                                    }
                                                }
                                            }
                                        }

                                        stand.remove();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 1);
        } else {
            Location highestY = trySummonChest(location);

            highestY.setYaw(highestY.getYaw() + 180);

            Block highest = highestY.getBlock();

            for (Player online : plugin.getServer().getOnlinePlayers()) {
                ParticleFixer fixer = new ParticleFixer(ParticleType.EXPLOSION);
                fixer.sendParticle(location);
            }


            highest.setType(Material.BEACON);

            Firework firework = highest.getWorld().spawn(highestY, Firework.class);
            FireworkMeta fwMeta = firework.getFireworkMeta();

            int r = new Random().nextInt(255);
            int g = new Random().nextInt(255);
            int b = new Random().nextInt(255);

            int r2 = new Random().nextInt(255);
            int g2 = new Random().nextInt(255);
            int b2 = new Random().nextInt(255);

            fwMeta.addEffect(FireworkEffect.builder().withColor(Color.fromBGR(r, g, b)).trail(true).flicker(true)
                    .withFade(Color.fromBGR(r2, g2, b2)).with(FireworkEffect.Type.STAR).build());

            fwMeta.setPower(2);

            firework.setFireworkMeta(fwMeta);

            BeaconChest chest = new BeaconChest((Beacon) highest.getLocation().getBlock().getState());

            for (ml.karmaconfigs.Supplies.Utils.Suministries.Suministry suministry : new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry().getSuministries()) {
                if (suministry.isGrenade(name)) {

                    SupplyLoader saver = new SupplyLoader(chest.getLocation());

                    saver.saveLoc(suministry, player);

                    ItemStack[] stacks = suministry.getContents();

                    chest.setInventory(stacks, suministry.getPercentage());

                    if (Suministry.hasHolographic()) {
                        Holograms holo = new Holograms(chest.getChest(), Message.hologram(player, suministry.getName()));

                        World w = chest.getLocation().getWorld();
                        double x = chest.getLocation().getX() + 0.5;
                        double y = chest.getLocation().getY() + 1.65;
                        double z = chest.getLocation().getZ() + 0.5;

                        Location loc = new Location(w, x, y, z);

                        holo.createHologram(loc);
                    }

                    SoundFixer.ENTITY_GENERIC_EXPLODE.playSound(chest.getLocation(), 0.5F, 0.1F);

                    for (Entity ent : chest.getLocation().getWorld().getNearbyEntities(chest.getLocation(), 10, 10, 10)) {
                        ent.setVelocity(chest.getLocation().toVector().subtract(ent.getLocation().toVector()).normalize().multiply(-4));
                    }

                    chests.add(chest.getChest());

                    for (Block block : getNearbyBlocks(chest.getLocation())) {

                        if (block != null) {
                            if (!block.getType().equals(Material.AIR)) {
                                float x = (float) -0.5 + (float) (Math.random() * ((0.5 - -0.5) + 1));
                                float y = (float) -1 + (float) (Math.random() * ((1 - -1) + 1));
                                float z = (float) -0.5 + (float) (Math.random() * ((0.5 - -0.5) + 1));

                                try {
                                    Item dropped = block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(block.getType()));
                                    dropped.setVelocity(new Vector(x, y, z));

                                    dropped.setPickupDelay(20 * 100000000);

                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            if (!dropped.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) {
                                                cancel();

                                                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                                                    SoundFixer.ENTITY_CHICKEN_EGG.playSound(chest.getLocation(), 0.5F, 2.0F);
                                                    ParticleFixer cloud = new ParticleFixer(ParticleType.CLOUD);
                                                    for (int ii = 0; ii < 5; ii++) {
                                                        cloud.sendParticle(dropped.getLocation());
                                                    }
                                                    dropped.remove();
                                                }, 20 * 3);
                                            }
                                        }
                                    }.runTaskTimer(plugin, 0, 1);
                                } catch (IllegalArgumentException ignore) {
                                }
                            }
                        }
                    }

                    break;
                }
            }
        }
    }

    /**
     * Play the falling chest animation
     *
     * @param location where it should be player
     * @param player the player name
     * @param name the supply name
     */
    public void startAnimationFor(Location location, String player, String name) {
        ml.karmaconfigs.Supplies.Utils.Suministries.Suministry suministry = new ml.karmaconfigs.Supplies.Utils.Suministries.Suministry(name);
        if (suministry.exists()) {
            if (Config.playAnimation) {

                location.setYaw(location.getYaw() + 180);

                ArmorStand stand = location.getWorld().spawn(location.add(0D, 50D, 0D), ArmorStand.class);
                stand.setArms(true);
                stand.setBasePlate(false);
                stand.setGravity(true);
                stand.setNoDamageTicks(20 * 1000000000);
                stand.setBodyPose(toAngle(118, 0, 0));
                stand.setLeftArmPose(toAngle(128, 140, 0));
                stand.setRightArmPose(toAngle(128, 220, 0));
                stand.setLeftLegPose(toAngle(177, 0, 0));
                stand.setRightLegPose(toAngle(177, 0, 0));

                int rand = new Random().nextInt(1000000);

                stand.setMetadata("Suministry", new FixedMetadataValue(plugin, "Suministry_" + rand));

                ItemStack head = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (byte) 3);

                SkullMeta meta = (SkullMeta) head.getItemMeta();
                meta.setOwner("Chest");

                head.setItemMeta(meta);

                stand.setHelmet(head);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Entity entity : stand.getWorld().getEntities()) {
                            if (entity.getEntityId() == stand.getEntityId()) {

                                World world = entity.getLocation().getWorld();
                                double X = entity.getLocation().getX();
                                double Y = entity.getLocation().getY() - 1D;
                                double Z = entity.getLocation().getZ();

                                Location location1 = new Location(world, X, Y, Z);

                                for (Block block : getNearbyBlocks(location1)) {
                                    for (int i = 0; i < 3; i++) {
                                        for (Player player : plugin.getServer().getOnlinePlayers()) {
                                            ParticleFixer fixer = new ParticleFixer(ParticleType.SMOKE);
                                            fixer.sendParticle(block.getLocation());
                                        }
                                    }
                                }

                                if (!location1.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) {
                                    cancel();
                                    Location highestY = trySummonChest(location1);

                                    highestY.setYaw(highestY.getYaw() + 180);

                                    Block highest = highestY.getBlock();

                                    for (Player player : plugin.getServer().getOnlinePlayers()) {
                                        ParticleFixer fixer = new ParticleFixer(ParticleType.EXPLOSION);
                                        fixer.sendParticle(stand.getLocation());
                                    }

                                    highest.setType(Material.BEACON);

                                    Firework firework = highest.getWorld().spawn(highestY, Firework.class);
                                    FireworkMeta fwMeta = firework.getFireworkMeta();

                                    int r = new Random().nextInt(255);
                                    int g = new Random().nextInt(255);
                                    int b = new Random().nextInt(255);

                                    int r2 = new Random().nextInt(255);
                                    int g2 = new Random().nextInt(255);
                                    int b2 = new Random().nextInt(255);

                                    fwMeta.addEffect(FireworkEffect.builder().withColor(Color.fromBGR(r, g, b)).trail(true).flicker(true)
                                            .withFade(Color.fromBGR(r2, g2, b2)).with(FireworkEffect.Type.STAR).build());

                                    fwMeta.setPower(2);

                                    firework.setFireworkMeta(fwMeta);

                                    BeaconChest chest = new BeaconChest((Beacon) highest.getLocation().getBlock().getState());

                                    SupplyLoader saver = new SupplyLoader(chest.getLocation());

                                    saver.saveLoc(suministry, player);

                                    ItemStack barrier = new ItemStack(Material.BARRIER, 1);
                                    ItemMeta barrierMeta = barrier.getItemMeta();

                                    barrierMeta.setDisplayName(StringUtils.toColor("&cREMOVE"));

                                    barrier.setItemMeta(barrierMeta);

                                    ItemStack[] stacks = suministry.getContents();

                                    chest.setInventory(stacks, suministry.getPercentage());

                                    if (Suministry.hasHolographic()) {
                                        Holograms holo = new Holograms(chest.getChest(), Message.hologram(player, suministry.getName()));

                                        World w = chest.getLocation().getWorld();
                                        double x = chest.getLocation().getX() + 0.5;
                                        double y = chest.getLocation().getY() + 1.65;
                                        double z = chest.getLocation().getZ() + 0.5;

                                        Location loc = new Location(w, x, y, z);

                                        holo.createHologram(loc);
                                    }

                                    SoundFixer.ENTITY_GENERIC_EXPLODE.playSound(chest.getLocation(), 0.5F, 0.1F);

                                    for (Entity ent : chest.getLocation().getWorld().getNearbyEntities(chest.getLocation(), 10, 10, 10)) {
                                        ent.setVelocity(chest.getLocation().toVector().subtract(ent.getLocation().toVector()).normalize().multiply(-4));
                                    }

                                    chests.add(chest.getChest());

                                    for (Block block : getNearbyBlocks(chest.getLocation())) {

                                        if (block != null) {
                                            if (!block.getType().equals(Material.AIR)) {
                                                float x = (float) -0.5 + (float) (Math.random() * ((0.5 - -0.5) + 1));
                                                float y = (float) -1 + (float) (Math.random() * ((1 - -1) + 1));
                                                float z = (float) -0.5 + (float) (Math.random() * ((0.5 - -0.5) + 1));

                                                try {
                                                    Item dropped = block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(block.getType()));
                                                    dropped.setVelocity(new Vector(x, y, z));

                                                    dropped.setPickupDelay(20 * 100000000);

                                                    new BukkitRunnable() {
                                                        @Override
                                                        public void run() {
                                                            if (!dropped.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) {
                                                                cancel();

                                                                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                                                                    SoundFixer.ENTITY_CHICKEN_EGG.playSound(chest.getLocation(), 0.5F, 2.0F);
                                                                    ParticleFixer cloud = new ParticleFixer(ParticleType.CLOUD);
                                                                    for (int ii = 0; ii < 5; ii++) {
                                                                        cloud.sendParticle(dropped.getLocation());
                                                                    }
                                                                    dropped.remove();
                                                                }, 20 * 3);
                                                            }
                                                        }
                                                    }.runTaskTimer(plugin, 0, 1);
                                                } catch (IllegalArgumentException ignore) {
                                                }
                                            }
                                        }
                                    }

                                    stand.remove();
                                }
                            }
                        }
                    }
                }.runTaskTimer(plugin, 0, 1);
            } else {
                Location highestY = trySummonChest(location);

                highestY.setYaw(highestY.getYaw() + 180);

                Block highest = highestY.getBlock();

                for (Player online : plugin.getServer().getOnlinePlayers()) {
                    ParticleFixer fixer = new ParticleFixer(ParticleType.EXPLOSION);
                    fixer.sendParticle(location);
                }


                highest.setType(Material.BEACON);

                Firework firework = highest.getWorld().spawn(highestY, Firework.class);
                FireworkMeta fwMeta = firework.getFireworkMeta();

                int r = new Random().nextInt(255);
                int g = new Random().nextInt(255);
                int b = new Random().nextInt(255);

                int r2 = new Random().nextInt(255);
                int g2 = new Random().nextInt(255);
                int b2 = new Random().nextInt(255);

                fwMeta.addEffect(FireworkEffect.builder().withColor(Color.fromBGR(r, g, b)).trail(true).flicker(true)
                        .withFade(Color.fromBGR(r2, g2, b2)).with(FireworkEffect.Type.STAR).build());

                fwMeta.setPower(2);

                firework.setFireworkMeta(fwMeta);

                BeaconChest chest = new BeaconChest((Beacon) highest.getLocation().getBlock().getState());

                SupplyLoader saver = new SupplyLoader(chest.getLocation());

                saver.saveLoc(suministry, player);

                ItemStack[] stacks = suministry.getContents();

                chest.setInventory(stacks, suministry.getPercentage());

                if (Suministry.hasHolographic()) {
                    Holograms holo = new Holograms(chest.getChest(), Message.hologram(player, suministry.getName()));

                    World w = chest.getLocation().getWorld();
                    double x = chest.getLocation().getX() + 0.5;
                    double y = chest.getLocation().getY() + 1.65;
                    double z = chest.getLocation().getZ() + 0.5;

                    Location loc = new Location(w, x, y, z);

                    holo.createHologram(loc);
                }

                SoundFixer.ENTITY_GENERIC_EXPLODE.playSound(chest.getLocation(), 0.5F, 0.1F);

                for (Entity ent : chest.getLocation().getWorld().getNearbyEntities(chest.getLocation(), 10, 10, 10)) {
                    ent.setVelocity(chest.getLocation().toVector().subtract(ent.getLocation().toVector()).normalize().multiply(-4));
                }

                chests.add(chest.getChest());

                for (Block block : getNearbyBlocks(chest.getLocation())) {
                    if (block != null) {
                        if (!block.getType().equals(Material.AIR)) {
                            float x = (float) -0.5 + (float) (Math.random() * ((0.5 - -0.5) + 1));
                            float y = (float) -1 + (float) (Math.random() * ((1 - -1) + 1));
                            float z = (float) -0.5 + (float) (Math.random() * ((0.5 - -0.5) + 1));

                            try {
                                Item dropped = block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(block.getType()));
                                dropped.setVelocity(new Vector(x, y, z));

                                dropped.setPickupDelay(20 * 100000000);

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (!dropped.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) {
                                            cancel();

                                            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                                                SoundFixer.ENTITY_CHICKEN_EGG.playSound(chest.getLocation(), 0.5F, 2.0F);
                                                ParticleFixer cloud = new ParticleFixer(ParticleType.CLOUD);
                                                for (int ii = 0; ii < 5; ii++) {
                                                    cloud.sendParticle(dropped.getLocation());
                                                }
                                                dropped.remove();
                                            }, 20 * 3);
                                        }
                                    }
                                }.runTaskTimer(plugin, 0, 1);
                            } catch (IllegalArgumentException ignore) {
                            }
                        }
                    }
                }
            }
        }
    }

    private ItemStack createStack(Material type, HashMap<Integer, Enchantment> enchants, int amount, String name, List<String> lore) {
        ItemStack stack = new ItemStack(type, amount);

        ItemMeta stackMeta = stack.getItemMeta();

        stackMeta.setDisplayName(StringUtils.toColor(name));

        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, StringUtils.toColor(lore.get(i)));
        }

        stackMeta.setLore(lore);

        if (enchants != null) {
            if (enchants.isEmpty()) {
                for (int level : enchants.keySet()) {
                    stackMeta.addEnchant(enchants.get(level), level, true);
                }
            }
        }

        stack.setItemMeta(stackMeta);

        return stack;
    }

    private List<Block> getNearbyBlocks(Location location) {
        List<Block> blocks = new ArrayList<>();
        for (int x = location.getBlockX() - 2; x <= location.getBlockX() + 2; x++) {
            for (int y = location.getBlockY() - 2; y <= location.getBlockY() + 2; y++) {
                for (int z = location.getBlockZ() - 2; z <= location.getBlockZ() + 2; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    private Location trySummonChest(Location location) {

        World world = location.getWorld();
        double X = location.getX();
        double Y = location.getY();
        double Z = location.getZ();

        Location random = new Location(world, X, Y, Z);

        double newY = random.getY();

        while (!new Location(world, X, newY, Z).getBlock().getType().equals(Material.AIR)) {
            newY++;
        }

        return new Location(world, X, newY, Z);
    }

    public List<Beacon> getChests() {
        return chests;
    }

    private boolean isEmpty(Inventory inv) {
        boolean empty = true;
        for (ItemStack item : inv.getContents()) {
            if (item != null) {
                if (!item.getType().equals(Material.AIR)) {
                    empty = false;
                }
            }
        }
        return empty;
    }
}
