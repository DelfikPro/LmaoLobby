package pro.delfik.lobby;

import lib.I;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pro.delfik.lmao.core.Person;
import implario.util.Rank;

public class OnlineHandler implements Listener {
	
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin(PlayerJoinEvent e) {
		resetPlayer(e.getPlayer());
		Person p = Person.get(e.getPlayer());
		
		if (p.getRank() != Rank.PLAYER) {
			String msg = "[+] " + p.getDisplayName() + "§f вошёл в лобби!";
			e.setJoinMessage(msg);
		} else e.setJoinMessage("");
		p.teleport(Lobby.spawnLocation());
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLeave(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		Person p = Person.get(player);
		e.setQuitMessage(p.getRank() != Rank.PLAYER ? "[-] " + player.getDisplayName() + "§f покинул лобби!" : "");
	}
	
	
	
	private static void resetPlayer(Player p) {
		I.delay(() -> p.setGameMode(GameMode.SURVIVAL), 1);
		p.setFoodLevel(20);
		p.setHealth(20);
		p.getInventory().clear();
		p.getInventory().setItem(0, Items.SERVER_SELECTOR);
	}
	
}
