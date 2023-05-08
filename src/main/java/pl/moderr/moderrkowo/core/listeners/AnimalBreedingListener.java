package pl.moderr.moderrkowo.core.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.mysql.User;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.npc.data.data.PlayerNPCData;
import pl.moderr.moderrkowo.core.npc.data.npc.NPCData;
import pl.moderr.moderrkowo.core.npc.data.quest.Quest;
import pl.moderr.moderrkowo.core.npc.data.tasks.IQuestItem;
import pl.moderr.moderrkowo.core.npc.data.tasks.IQuestItemBreed;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.ArrayList;

public class AnimalBreedingListener implements Listener {

    public ItemStack goldenApple, goldenCarrot, wheat, carrot, seeds, dandelions;
    public EntityType horse, sheep, cow, mooshroomCow, pig, chicken, rabbit;
    public ArrayList<Material> breeadableFood = new ArrayList<>();
    public ArrayList<EntityType> breeadableAnimals = new ArrayList<>();

    public AnimalBreedingListener(Plugin p) {
        addBreeadableFood();
        addBreeadableAnimals();
        p.getServer().getPluginManager().registerEvents(this, p);
    }

    public void addBreeadableFood() {
        goldenApple = new ItemStack(Material.GOLDEN_APPLE);
        goldenCarrot = new ItemStack(Material.GOLDEN_CARROT);
        wheat = new ItemStack(Material.WHEAT);
        carrot = new ItemStack(Material.CARROT);
        seeds = new ItemStack(Material.WHEAT_SEEDS);
        dandelions = new ItemStack(Material.DANDELION);

        breeadableFood.add(goldenApple.getType());
        breeadableFood.add(goldenCarrot.getType());
        breeadableFood.add(wheat.getType());
        breeadableFood.add(carrot.getType());
        breeadableFood.add(seeds.getType());
        breeadableFood.add(dandelions.getType());
    }

    public void addBreeadableAnimals() {
        horse = EntityType.HORSE;
        sheep = EntityType.SHEEP;
        cow = EntityType.COW;
        mooshroomCow = EntityType.MUSHROOM_COW;
        pig = EntityType.PIG;
        chicken = EntityType.CHICKEN;
        rabbit = EntityType.RABBIT;

        breeadableAnimals.add(horse);
        breeadableAnimals.add(sheep);
        breeadableAnimals.add(cow);
        breeadableAnimals.add(mooshroomCow);
        breeadableAnimals.add(pig);
        breeadableAnimals.add(chicken);
        breeadableAnimals.add(rabbit);
    }

    @EventHandler
    public void onAnimalBreed(PlayerInteractEntityEvent e) {
        if (breeadableFood.contains(e.getPlayer().getItemInHand().getType())) {
            if (breeadableAnimals.contains(e.getRightClicked().getType())) {
                Entity AnimalBeingBreed = e.getRightClicked();
                try {
                    User u = UserManager.getUser(e.getPlayer().getUniqueId());
                    PlayerNPCData data = null;
                    for (PlayerNPCData villagers : u.getNPCSData().getNPCSData().values()) {
                        if (villagers.isActiveQuest()) {
                            data = villagers;
                            break;
                        }
                    }
                    if (data == null) {
                        return;
                    }
                    NPCData villager = Main.getInstance().NPCManager.npcs.get(data.getNpcId());
                    Quest quest = villager.getQuests().get(data.getQuestIndex());
                    for (IQuestItem item : quest.getQuestItems()) {
                        if (item instanceof IQuestItemBreed) {
                            int items = data.getQuestItemData().get(item.getQuestItemDataId());
                            int temp = items;
                            temp += 1;
                            data.getQuestItemData().replace(item.getQuestItemDataId(), items, temp);
                            e.getPlayer().sendMessage(ColorUtils.color("&c&lQ &6» &aRozmnożono &2" + ChatUtil.materialName(AnimalBeingBreed.getType())));
                            u.UpdateScoreboard();
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }

    }

}
