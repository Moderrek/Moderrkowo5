package pl.moderr.moderrkowo.core.events.user.quest;

import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.services.mysql.UserManager;
import pl.moderr.moderrkowo.core.services.npc.NPCManager;
import pl.moderr.moderrkowo.core.services.npc.data.data.PlayerNPCData;
import pl.moderr.moderrkowo.core.services.npc.data.npc.NPCData;
import pl.moderr.moderrkowo.core.services.npc.data.quest.Quest;
import pl.moderr.moderrkowo.core.services.npc.data.tasks.IQuestItem;
import pl.moderr.moderrkowo.core.services.npc.data.tasks.IQuestItemBreed;
import pl.moderr.moderrkowo.core.user.User;

import java.util.ArrayList;

public class AnimalBreedingListener implements Listener {

    public ItemStack goldenApple, goldenCarrot, wheat, carrot, seeds, dandelions;
    public EntityType horse, sheep, cow, mooshroomCow, pig, chicken, rabbit;
    public ArrayList<Material> breeadableFood = new ArrayList<>();
    public ArrayList<EntityType> breeadableAnimals = new ArrayList<>();

    public AnimalBreedingListener(@NotNull Plugin p) {
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
    public void onAnimalBreed(@NotNull PlayerInteractEntityEvent e) {
        if (breeadableFood.contains(e.getPlayer().getItemInHand().getType())) {
            if (breeadableAnimals.contains(e.getRightClicked().getType())) {
                Entity AnimalBeingBreed = e.getRightClicked();
                try {
                    Ageable ageable = (Ageable) AnimalBeingBreed;
                    if (!ageable.isAdult()) {
                        return;
                    }
                    User u = UserManager.getUser(e.getPlayer().getUniqueId());
                    PlayerNPCData data = null;
                    for (PlayerNPCData villagers : u.getQuestData().getNPCSData().values()) {
                        if (villagers.isActiveQuest()) {
                            data = villagers;
                            break;
                        }
                    }
                    if (data == null) {
                        return;
                    }
                    final NPCManager npc = ModerrkowoPlugin.getInstance().getNpc();
                    NPCData villager = npc.npcs.get(data.getNpcId());
                    Quest quest = villager.getQuests().get(data.getQuestIndex());
                    for (IQuestItem item : quest.getQuestItems()) {
                        if (item instanceof IQuestItemBreed) {
                            int items = data.getQuestItemData().get(item.getQuestItemDataId());
                            int temp = items;
                            temp += 1;
                            data.getQuestItemData().replace(item.getQuestItemDataId(), items, temp);
                            e.getPlayer().sendMessage(ColorUtil.color("&c&lQ &6» &aRozmnożono &2" + ChatUtil.materialName(AnimalBeingBreed.getType())));
                            u.updateScoreboard();
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }

    }

}
