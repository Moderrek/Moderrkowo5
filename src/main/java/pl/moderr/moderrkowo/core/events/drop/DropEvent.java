package pl.moderr.moderrkowo.core.events.drop;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.events.ModerrEvent;
import pl.moderr.moderrkowo.core.mysql.User;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.RandomUtils;

import java.util.Objects;
import java.util.Random;

public class DropEvent implements ModerrEvent, Listener {

    World w = null;
    Location loc = null;


    public DropEvent(World w){
        /*this.dropItemWeightedListOVERWORLD = new WeightedList<>();
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.COD, 2,64), 8);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.ENDER_PEARL, 1,5), 1);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.EMERALD, 1, 30), 15);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.COAL, 1, 64), 10);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.IRON_INGOT, 1, 40), 15);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.BAMBOO, 1, 3), 10);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.FIREWORK_ROCKET, 1, 7), 10);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.QUARTZ, 1, 20), 14);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.REDSTONE, 1, 20), 10);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.BLAZE_POWDER, 1, 30), 10);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.COCOA_BEANS, 1, 32), 2);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.LEATHER, 1, 20), 2);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.SLIME_BALL, 1, 16), 2);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.HONEY_BOTTLE, 1, 16), 2);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.EXPERIENCE_BOTTLE, 1, 20), 2);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.MUSIC_DISC_CAT), 1);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.MUSIC_DISC_CHIRP), 1);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.EXPERIENCE_BOTTLE, 20,64), 5);
        this.dropItemWeightedListOVERWORLD.put(new DropItem(Material.EXPERIENCE_BOTTLE, 1,20), 10);*/
        this.w = w;
    }

    @Override
    public String description() {
        return
                "&6Legendarny zrzut &7spadł na mapę\n" +
                        "&fx " + loc.getBlockX() + " z " + loc.getBlockZ() + "\n" +
                        "&aKto pierwszy otworzy zrzut zgarnia wszystko!";
    }
    @Override
    public String eventName() {
        return "Zrzut";
    }
    @Override
    public int timeSec() {
        return 35 * 60; // 5x60 | 5 minut
    }
    @Override
    public boolean broadcast() {
        return true;
    }
    @Override
    public boolean bossBar() {
        return false;
    }

    @Override
    public void PrepareEvent() {
        loc = RandomUtils.getRandom(w);
    }

    int taskId;

    @Override
    public void Action() {
        BukkitTask bt = Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            Bukkit.broadcastMessage(ColorUtils.color("  &fNikt nie otworzył zrzutu"));
            loc.getBlock().setType(Material.AIR);
            EndEvent();
        }, timeSec()* 20L);
        taskId = bt.getTaskId();
        doDrop();
    }
    @Override
    public void EndEvent() {
        setActive(false);
        Bukkit.getScheduler().cancelTask(taskId);
        Main.getInstance().eventManager.EndEvent();
    }

    @EventHandler
    public void click(InventoryOpenEvent e){
        if(e.getInventory().getHolder() instanceof Chest){
            Chest chest = (Chest) e.getInventory().getHolder();
            if(chest.getCustomName() == null){
                return;
            }
            if(Objects.requireNonNull(chest.getCustomName()).equalsIgnoreCase(ColorUtils.color("&c&lZrzut zasobów"))){
                    User u = UserManager.getUser(e.getPlayer().getUniqueId());
                    int kwota = RandomUtils.getRandomInt(100,20000);
                    u.addMoney(kwota);
                    e.getPlayer().sendMessage(ColorUtils.color("&9Drop &6> &a+ " + ChatUtil.getMoney(kwota)));
                    net.md_5.bungee.api.chat.TextComponent no = new TextComponent(ColorUtils.color("  &fZrzut zasobów został otworzony przez &a" + e.getPlayer().getName()));
                    no.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorUtils.color("&fOtworzony na &aX " + chest.getLocation().getBlockX() + " Z " + chest.getLocation().getBlockZ() + " &fprzez&a " + e.getPlayer().getName()))));
                    no.setColor(ChatColor.RED);
                    Main.getInstance().getServer().broadcast(new ComponentBuilder().append(no).create());
                    Objects.requireNonNull(Bukkit.getWorld("world")).spawnParticle(Particle.TOTEM,chest.getLocation().getBlockX(),chest.getLocation().getBlockY(),chest.getLocation().getBlockZ(),20,1,1,1,0.1);
                    chest.setCustomName(ColorUtils.color("&aOtworzony zrzut przez &2" + e.getPlayer().getName()));
                    Chest chestBlock = (Chest) chest.getBlock().getState();
                    chestBlock.setCustomName(ColorUtils.color("&aOtworzony zrzut przez &2" + e.getPlayer().getName()));
                    chestBlock.update();
                    EndEvent();
            }
        }
    }

    public void doDrop() {
            new BukkitRunnable() {
                public void run() {
                    loc.getBlock().setType(Material.CHEST);
                    final Chest chest = (Chest)loc.getWorld().getBlockAt(loc).getState();
                    chest.setCustomName(ColorUtils.color("&c&lZrzut zasobów"));
                    chest.update();
                    final Random rnd = new Random();
                    for (int i = 0; i != chest.getInventory().getSize(); ++i) {
                        final int chance = rnd.nextInt(100);
                        if (chance <= 85) {
//                            final ShulkerDropItem item = DropEvent.this.dropItemWeightedListOVERWORLD.get(rnd);
//                            ItemStack is = item.getDrop();
//                            chest.getInventory().setItem(i, is);
                        }
                    }
                    Objects.requireNonNull(Bukkit.getWorld("world")).strikeLightningEffect(Objects.requireNonNull(chest.getLocation()));
                }
            }.runTaskLater(Main.getInstance(), 1L);
    }

    boolean active = false;
    @Override
    public boolean getActive() {
        return active;
    }
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}
