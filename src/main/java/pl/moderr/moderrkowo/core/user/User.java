package pl.moderr.moderrkowo.core.user;

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.ItemStackUtil;
import pl.moderr.moderrkowo.core.api.util.Logger;
import pl.moderr.moderrkowo.core.services.mysql.UserManager;
import pl.moderr.moderrkowo.core.services.npc.NPCManager;
import pl.moderr.moderrkowo.core.services.npc.data.data.PlayerNPCData;
import pl.moderr.moderrkowo.core.services.npc.data.data.PlayerNPCSData;
import pl.moderr.moderrkowo.core.services.npc.data.quest.Quest;
import pl.moderr.moderrkowo.core.services.npc.data.tasks.*;
import pl.moderr.moderrkowo.core.user.level.LevelCategory;
import pl.moderr.moderrkowo.core.user.level.UserLevel;
import pl.moderr.moderrkowo.core.user.notification.MNotification;
import pl.moderr.moderrkowo.core.user.notification.NotificationType;
import pl.moderr.moderrkowo.core.user.ranks.Rank;
import pl.moderr.moderrkowo.core.user.ranks.RankManager;
import pl.moderr.moderrkowo.core.user.ranks.StuffRank;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Represents User of Moderrkowo which stores data and is saved to users repository.
 */
@Data
public class User {

    // Identifying
    private final UUID uuid;
    private final String name;

    // Data
    private final PlayerNPCSData questData;
    private final Date registered;
    private double money;
    private int coins;
    private Rank rank;
    private StuffRank stuffRank;
    private UserLevel level;
    private boolean sidebar;
    private int playtimeTicks;
    private String version;

    public User(UUID uuid, String name, double money, int coins, Rank rank, StuffRank stuffRank, @NotNull UserLevel level, PlayerNPCSData questData, Date registered, boolean sidebar, int playtimeTicks, String version) {
        this.uuid = uuid;
        this.name = name;
        this.money = money;
        this.coins = coins;
        this.rank = rank;
        this.stuffRank = stuffRank;
        this.level = level;
        if (level.getOwner() == null) {
            level.setOwner(this);
        }
        this.questData = questData;
        this.registered = registered;
        this.sidebar = sidebar;
        this.playtimeTicks = playtimeTicks;
        this.version = version;
    }

    /**
     * Creates new default instance of {@link User} for given Minecraft player.
     * @param player The Minecraft player
     * @return The new instance of User
     */
    @Contract("_ -> new")
    public static @NotNull User CreateDefault(@NotNull Player player) {
        final UUID uuid = player.getUniqueId();
        final String name = player.getName();
        final Date registered = new Date(Calendar.getInstance().getTime().getTime());
        final int playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        return new User(uuid, name, 3000, 0, Rank.None, StuffRank.None, new UserLevel(), new PlayerNPCSData(), registered, true, playtime, ModerrkowoPlugin.getVersion());
    }

    /**
     * Gets loaded {@link User} instance from cache by given player UUID
     *
     * @param uuid The online Minecraft player UUID
     * @return The loaded user instance
     */
    public static @Nullable User Get(UUID uuid) {
        return UserManager.getUser(uuid);
    }

    /**
     * Gets loaded {@link User} instance from cache by given player instance.
     *
     * @param player The player instance
     * @return The loaded user instance
     */
    public static @Nullable User Get(@NotNull Player player) {
        return Get(player.getUniqueId());
    }

    /**
     * Gets loaded {@link User} instance from cache by given player name.
     *
     * @param name The player name
     * @return The loaded user instance
     */
    public static @Nullable User Get(String name) {
        final OfflinePlayer cachedPlayer = Bukkit.getOfflinePlayerIfCached(name);
        if (cachedPlayer == null) return null;
        if (!cachedPlayer.hasPlayedBefore()) return null;
        return Get(cachedPlayer.getUniqueId());
    }

    /**
     * Saves {@link User} to users repository.
     */
    public void save() {
        UserManager.saveUser(this);
    }

    /**
     * Check is User loaded in cache
     *
     * @return Is loaded in cache
     */
    public boolean isCached() {
        return UserManager.isUserLoaded(uuid);
    }

    /**
     * Sets new amount of money and updates player scoreboard.
     *
     * @param money The new amount of money
     */
    public void setMoney(double money) {
        this.money = money;
        if (sidebar) {
            updateScoreboard();
        } else {
            getPlayer().sendMessage(ColorUtil.color("&7= " + ChatUtil.formatMoney(money)));
        }
    }

    /**
     * Adds given amount of money and updates player scoreboard.
     *
     * @param money The given amount of money
     */
    public void addMoney(double money) {
        this.money += money;
        if (sidebar) {
            updateScoreboard();
        } else {
            getPlayer().sendMessage(ColorUtil.color("&a+ " + ChatUtil.formatMoney(money)));
        }
    }

    /**
     * Subtracts given amount of money and updates player scoreboard.
     *
     * @param money The given amount of money
     */
    public void subtractMoney(double money) {
        this.money -= money;
        if (sidebar) {
            updateScoreboard();
        } else {
            getPlayer().sendMessage(ColorUtil.color("&c- " + ChatUtil.formatMoney(money)));
        }
    }

    /**
     * Checks is user can afford to buy.
     *
     * @param money The cost
     * @return Is user can afford to buy
     */
    public boolean hasMoney(double money) {
        return this.money >= money;
    }

    public void addExp(LevelCategory category, double exp) {
        level.get(category).addExp(exp);
    }

    /**
     * Gets unique id
     *
     * @return The unique id
     */
    public UUID getUniqueId() {
        return uuid;
    }

    /**
     * Gets player instance
     *
     * @return The player instance
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    /**
     * Checks is user have given {@link Rank} or higher
     *
     * @param rank The given rank
     * @return Is having rank or higher
     */
    public boolean hasRank(Rank rank) {
        return RankManager.getPriority(this.rank) >= RankManager.getPriority(rank);
    }

    /**
     * Checks is player fighting any other player.
     *
     * @return Is fighting
     */
    public boolean isFighting() {
        return ModerrkowoPlugin.getInstance().getAntyLogoutService().isFighting(uuid);
    }

    /**
     * Sends message to player
     *
     * @param component The message component
     */
    public void message(Component component) {
        getPlayer().sendMessage(component);
    }

    /**
     * Sends raw text message to player
     *
     * @param content The raw text message
     */
    public void message(String content) {
        getPlayer().sendMessage(content);
    }

    /**
     * Sends message to player with given content and text color.
     *
     * @param content The content
     * @param color   The text color
     */
    public void message(String content, TextColor color) {
        getPlayer().sendMessage(Component.text().content(content).color(color).build());
    }

    /**
     * Shows title to player
     *
     * @param title The title
     */
    public void title(Title title) {
        getPlayer().showTitle(title);
    }

    /**
     * Plays sound at player location with default params.
     *
     * @param sound The sound
     */
    public void playSound(Sound sound) {
        playSound(sound, 1.0F, 1.0F);
    }

    /**
     * Plays sound at player location.
     *
     * @param sound  The sound
     * @param volume The volume (default 1.0F)
     * @param pitch  The pitch (default 1.0F)
     */
    public void playSound(Sound sound, float volume, float pitch) {
        final Player player = getPlayer();
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Shows inventory for player
     *
     * @param inventory The inventory
     */
    public void inventory(Inventory inventory) {
        getPlayer().openInventory(inventory);
    }

    /**
     * Gets player ender chest inventory
     *
     * @return The ender chest
     */
    public Inventory getEnderChest() {
        return getPlayer().getEnderChest();
    }

    /**
     * Gives player item stack if he doesn't have space in inventory, item stack drops on ground.
     *
     * @param itemStack The item stack
     */
    public void give(ItemStack itemStack) {
        ItemStackUtil.addItemStackToPlayer(getPlayer(), itemStack);
    }

    /**
     * Gives player item if he doesn't have space in inventory, item drops on ground.
     *
     * @param material The item type
     */
    public void give(Material material) {
        ItemStackUtil.addItemStackToPlayer(getPlayer(), new ItemStack(material));
    }

    /**
     * Updates scoreboard for player
     */
    public void updateScoreboard() {
        if (!sidebar) {
            return;
        }
        ScoreboardManager sm = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = sm.getNewScoreboard();
        Objective objective;
        objective = scoreboard.registerNewObjective("Moderrkowo", "dummy", ColorUtil.color("&6⚔ Moderrkowo ⚔"), RenderType.INTEGER);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        PlayerNPCData data = null;
        try {
            for (PlayerNPCData villagers : getQuestData().getNPCSData().values()) {
                if (villagers.isActiveQuest()) {
                    data = villagers;
                    break;
                }
            }
        } catch (Exception ignored) {

        }
//        Score score1 = objective.getScore(ColorUtils.color(RankManager.getRankNameShort(_RANK, true) + "&9" + _NAME));
        Score score1 = objective.getScore(" ");
        Score score2 = objective.getScore(ColorUtil.color("&fPostać &c" + level.playerLevel() + " lvl"));
        Score score3 = objective.getScore(ColorUtil.color("&fPortfel &6" + ChatUtil.formatMoney(getMoney())));
        Score score4 = objective.getScore(ColorUtil.color("&fCzas gry &a" + ChatUtil.getTicksToTime(getPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE))));
        Score score7 = objective.getScore(ColorUtil.color("&6moderrkowo.pl"));
        if (data == null) {
            Score score5 = objective.getScore(ColorUtil.color("&fAktywny quest &abrak"));
            Score score6 = objective.getScore("  ");
            score1.setScore(-1);
            score2.setScore(-2);
            score3.setScore(-3);
            score4.setScore(-4);
            score5.setScore(-5);
            score6.setScore(-6);
            score7.setScore(-7);
        } else {
            final NPCManager npc = ModerrkowoPlugin.getInstance().getNpc();
            ;
            if (!npc.npcs.containsKey(data.getNpcId())) {
                Score score5 = objective.getScore(ColorUtil.color("&fAktywny quest &abrak"));
                Score score6 = objective.getScore("  ");
                score1.setScore(-1);
                score2.setScore(-2);
                score3.setScore(-3);
                score4.setScore(-4);
                score5.setScore(-5);
                score6.setScore(-6);
                score7.setScore(-7);
                getPlayer().setScoreboard(scoreboard);
                return;
            }
            Quest q = npc.npcs.get(data.getNpcId()).getQuests().get(data.getQuestIndex());
            Score score6 = objective.getScore(ColorUtil.color("&fAktywny quest &a" + q.getName()));
            int itemI = 0;
            int last = 0;
            for (int i = -6; i != -6 - npc.npcs.get(data.getNpcId()).getQuests().get(data.getQuestIndex()).getQuestItems().size(); i--) {
                try {
                    IQuestItem iQuestItem = q.getQuestItems().get(itemI);
                    if (iQuestItem instanceof IQuestItemCollect) {
                        IQuestItemCollect item = (IQuestItemCollect) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtil.color("&a✔ " + item.getQuestItemPrefix() + " " + item.getCount() + " " + ChatUtil.materialName(item.getMaterial())));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtil.color("&c✘ " + item.getQuestItemPrefix() + " " + count + " " + ChatUtil.materialName(item.getMaterial())));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemFish) {
                        IQuestItemFish item = (IQuestItemFish) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtil.color("&a✔ " + item.getQuestItemPrefix() + " " + item.getCount() + " " + ChatUtil.materialName(item.getMaterial())));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtil.color("&c✘ " + item.getQuestItemPrefix() + " " + count + " " + ChatUtil.materialName(item.getMaterial())));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemPay) {
                        IQuestItemPay item = (IQuestItemPay) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtil.color("&a✔ " + item.getQuestItemPrefix() + " " + ChatUtil.formatMoney(item.getCount())));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtil.color("&c✘ " + item.getQuestItemPrefix() + " " + ChatUtil.formatMoney(count)));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemBreak) {
                        IQuestItemBreak item = (IQuestItemBreak) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtil.color("&a✔ " + item.getQuestItemPrefix() + " " + item.getCount() + " " + ChatUtil.materialName(item.getMaterial())));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtil.color("&c✘ " + item.getQuestItemPrefix() + " " + count + " " + ChatUtil.materialName(item.getMaterial())));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemGive) {
                        IQuestItemGive item = (IQuestItemGive) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtil.color("&a✔ " + item.getQuestItemPrefix() + " " + item.getCount() + " " + ChatUtil.materialName(item.getMaterial())));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtil.color("&c✘ " + item.getQuestItemPrefix() + " " + count + " " + ChatUtil.materialName(item.getMaterial())));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemCraft) {
                        IQuestItemCraft item = (IQuestItemCraft) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtil.color("&a✔ " + item.getQuestItemPrefix() + " " + item.getCount() + " " + ChatUtil.materialName(item.getMaterial())));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtil.color("&c✘ " + item.getQuestItemPrefix() + " " + count + " " + ChatUtil.materialName(item.getMaterial())));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemKill) {
                        IQuestItemKill item = (IQuestItemKill) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtil.color("&a✔ " + item.getQuestItemPrefix() + " " + item.getCount() + " " + ChatUtil.materialName(item.getEntityType())));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtil.color("&c✘ " + item.getQuestItemPrefix() + " " + count + " " + ChatUtil.materialName(item.getEntityType())));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemBreed) {
                        IQuestItemBreed item = (IQuestItemBreed) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtil.color("&a✔ " + item.getQuestItemPrefix() + " " + item.getCount() + " " + ChatUtil.materialName(item.getEntityType().toEntity())));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtil.color("&c✘ " + item.getQuestItemPrefix() + " " + count + " " + ChatUtil.materialName(item.getEntityType().toEntity())));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemFishingRod) {
                        IQuestItemFishingRod item = (IQuestItemFishingRod) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtil.color("&a✔ " + item.getQuestItemPrefix() + " " + item.getCount() + " razy"));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtil.color("&c✘ " + item.getQuestItemPrefix() + " " + count + " razy"));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemVisit) {
                        IQuestItemVisit item = (IQuestItemVisit) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= 1) {
                            tempScore = objective.getScore(ColorUtil.color("&a✔ " + item.getQuestItemPrefix() + " " + ChatUtil.materialName(item.getBiome())));
                        } else {
                            int count = 1 - temp;
                            if (count > 1) {
                                count = 1;
                            }
                            tempScore = objective.getScore(ColorUtil.color("&c✘ " + item.getQuestItemPrefix() + " " + ChatUtil.materialName(item.getBiome())));
                        }
                        tempScore.setScore(i);
                    }
                    itemI++;
                    last = i;
                } catch (Exception exception) {
                    System.out.println("Exception  >> " + getName());
                    exception.printStackTrace();
                }
            }
            Score score9 = objective.getScore("  ");
            score1.setScore(-1);
            score2.setScore(-2);
            score3.setScore(-3);
            score4.setScore(-4);
            score6.setScore(-5);
            score9.setScore(last - 1);
            score7.setScore(last - 2);
        }
        getPlayer().setScoreboard(scoreboard);
    }

    /**
     * @deprecated Going to change this because swallowing exceptions is bad.
     */
    @Deprecated
    public void tryUpdateScoreboard() {
        try {
            updateScoreboard();
        } catch (Exception ignored) {
        }
    }

    /**
     * TODO change this function to async
     */
    public void tryLoadNotifications() {
        try {
            String sqlGet = "SELECT * FROM `" + ModerrkowoPlugin.getMySQL().notificationTable + "` WHERE `OWNER`=?";
            PreparedStatement stmt = ModerrkowoPlugin.getMySQL().getConnection().prepareStatement(sqlGet);
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs == null) return;
            while (rs.next()) {
                try {
                    MNotification notify = new MNotification(UUID.fromString(rs.getString("ID")), UUID.fromString(rs.getString("OWNER")), NotificationType.valueOf(rs.getString("TYPE")), true, rs.getString("DATA"));
                    getPlayer().sendMessage(ColorUtil.color(notify.getData()));
                    notify.remove();
                } catch (Exception e) {
                    Logger.logAdminLog("Wystąpił problem z powiadomieniem");
                    e.printStackTrace();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Logger.logAdminLog("Wystąpił problem podczas ładowania powiadomieniu");
        }
    }

}
