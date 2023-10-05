package ldcr.BedwarsXP.api;

import ldcr.BedwarsXP.Config;
import ldcr.BedwarsXP.utils.ActionBarUtils;
import ldcr.BedwarsXP.utils.SoundMachine;
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
        Integer value = xp.get(player.getUniqueId());
        if (value == null) {
            value = 0;
            xp.put(player.getUniqueId(), 0);
        }
        return value;
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
        if (!messageTimeMap.containsKey(player.getUniqueId())) {
            messageTimeMap.put(player.getUniqueId(), System.currentTimeMillis());
        }
        if (!messageCountMap.containsKey(player.getUniqueId())) {
            messageCountMap.put(player.getUniqueId(), 0);
        }
        int c = messageCountMap.get(player.getUniqueId()) + count;
        messageCountMap.put(player.getUniqueId(), c);
        if (System.currentTimeMillis() - messageTimeMap.get(player.getUniqueId()) > 500) {
            if (!Config.xpMessage.isEmpty()) {
                ActionBarUtils.sendActionBar(player, Config.xpMessage.replaceAll("%xp%", Integer.toString(c)));
            }
            messageCountMap.put(player.getUniqueId(), 0);
            messageTimeMap.put(player.getUniqueId(), System.currentTimeMillis());
        }
        player.playSound(player.getLocation(), SoundMachine.get("ORB_PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP"), 0.2F, 1.5F);
    }

    public void sendMaxXPMessage(Player player) {
        if (!Config.maxXPMessage.isEmpty()) {
            ActionBarUtils.sendActionBar(player, Config.maxXPMessage);
        }
    }
}
