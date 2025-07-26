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
    private final Map<UUID, Integer> xpCache = new HashMap<>();
    private final Map<UUID, Long> lastSendTime = new HashMap<>();

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
        UUID uuid = player.getUniqueId();

        if (!Config.xpMessage.isEmpty()) {
            // 累计经验
            xpCache.put(uuid, xpCache.getOrDefault(uuid, 0) + count);

            long currentTime = System.currentTimeMillis();
            long lastTime = lastSendTime.getOrDefault(uuid, 0L);

            // 判断是否已过去0.5秒（500ms）
            if (xpCache.containsKey(uuid) && currentTime > lastTime) {
                int totalXp = xpCache.get(uuid);

                // 清理缓存
                xpCache.remove(uuid);
                lastSendTime.put(uuid, currentTime + 500);

                // 发送消息
                String msg = Config.xpMessage.replace("%xp%", Integer.toString(totalXp));
                ActionBarUtils.sendActionBar(player, msg);
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
