package ldcr.BedwarsXP.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedwarsXPDeathDropXPEvent extends Event implements Cancellable {
    private final String game;
    private final Player player;
    private int deathCost;
    private int deathDropped;
    private boolean canceled;
    public BedwarsXPDeathDropXPEvent(String game, Player p, int dropped, int cost) {
        this.game = game;
        player = p;
        deathCost = cost;
        deathDropped = dropped;
    }

    @Override
    public HandlerList getHandlers() {
        return new HandlerList();
    }

    public String getGameName() {
        return game;
    }

    public Player getDeadPlayer() {
        return player;
    }

    public int getXPCost() {
        return deathCost;
    }

    public void setXPCost(int drop) {
        deathCost = drop;
    }

    public int getXPDropped() {
        return deathDropped;
    }

    public void setXPDropped(int deathDropped) {
        this.deathDropped = deathDropped;
    }

    @Deprecated
    public int getXPCosted() {
        return deathCost;
    }

    @Deprecated
    public void setXPCosted(int drop) {
        deathCost = drop;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.canceled = b;
    }
}
