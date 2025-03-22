package ldcr.BedwarsXP.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class ActionBarUtils {
    public static void sendActionBar(Player player, String message) {
        Component component = LegacyComponentSerializer.legacySection().deserialize(message);
        player.sendActionBar(component);
    }

}