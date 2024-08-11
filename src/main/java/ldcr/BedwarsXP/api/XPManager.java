package ldcr.BedwarsXP.api;

import ldcr.BedwarsXP.Config;
import ldcr.BedwarsXP.utils.ActionBarUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class XPManager {
    private static final Map<String, XPManager> managerMap = new HashMap<>();
    private final Map<UUID, Integer> xp = new HashMap<>();
    private final HashMap<UUID, Long> messageTimeMap = new HashMap<>();
    private final HashMap<UUID, Integer> messageCountMap = new HashMap<>();

    public static XPManager getXPManager(String bedwarsGame) {
        if (!managerMap.containsKey(bedwarsGame)) {
            managerMap.put(bedwarsGame, new XPManager());
        }
        return managerMap.get(bedwarsGame);
    }

    public static void reset(String bedwarsGame) {
        managerMap.remove(bedwarsGame);
    }

    public void updateXPBar(Player player) {
        player.setLevel(get(player));
    }

    private void set(Player player, int count) {
        xp.put(player.getUniqueId(), count);
        updateXPBar(player);
    }

    private int get(Player player) {
        UUID uniqueId = player.getUniqueId();
        xp.putIfAbsent(uniqueId, 0);
        return xp.get(uniqueId);
    }

    public void setXP(Player player, int count) {
        set(player, count);
    }

    public int getXP(Player player) {
        return get(player);
    }

    public void addXP(Player player, int count) {
        set(player, get(player) + count);
    }

    public boolean takeXP(Player player, int count) {
        if (!hasEnoughXP(player, count))
            return false;
        set(player, get(player) - count);
        return true;
    }

    public boolean hasEnoughXP(Player player, int count) {
        return get(player) >= count;
    }

    public void sendXPMessage(Player player, int count) {
        UUID uniqueId = player.getUniqueId();
        if (!Config.xpMessage.isEmpty()) {
            messageTimeMap.putIfAbsent(uniqueId, System.currentTimeMillis() + 500);
            messageCountMap.putIfAbsent(uniqueId, 0);

            int addedXp = messageCountMap.get(uniqueId) + count;
            messageCountMap.put(uniqueId, addedXp);
            if (System.currentTimeMillis() > messageTimeMap.get(uniqueId)) {
                ActionBarUtils.sendActionBar(player, Config.xpMessage.replaceAll("%xp%", Integer.toString(addedXp)));
                messageCountMap.remove(uniqueId);
                messageTimeMap.remove(uniqueId);
            }
        }
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.2F, 1.5F);
    }

    public void sendMaxXPMessage(Player player) {
        if (!Config.maxXPMessage.isEmpty()) {
            ActionBarUtils.sendActionBar(player, Config.maxXPMessage);
        }
    }
}
