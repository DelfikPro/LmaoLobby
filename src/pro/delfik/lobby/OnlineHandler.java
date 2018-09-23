package pro.delfik.lobby;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import pro.delfik.lmao.user.Person;
import implario.util.Rank;
import pro.delfik.lmao.outward.item.I;

public class OnlineHandler implements Listener {
	@EventHandler(priority = EventPriority.HIGH)
	public void event(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		resetPlayer(player);
		event.setJoinMessage("");
		Person person = Person.get(player);
		if(person == null) return;
		player.setAllowFlight(true);
		if(person.getRank() != Rank.PLAYER)
			event.setJoinMessage("[+] " + player.getDisplayName() + "§f вошёл в лобби!");
		player.teleport(Lobby.spawnLocation());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void event(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Person person = Person.get(player);
		if(person == null){
			event.setQuitMessage("");
			return;
		}
		event.setQuitMessage(person.getRank() != Rank.PLAYER ? "[-] " + player.getDisplayName() + "§f покинул лобби!" : "");
	}

	private static void resetPlayer(Player player) {
		I.delay(() -> player.setGameMode(GameMode.SURVIVAL), 1);
		I.delay(() -> {
			player.setGameMode(GameMode.SURVIVAL);
			player.setAllowFlight(true);
		}, 1);
		player.setFoodLevel(20);
		player.setHealth(20);
		Inventory inventory = player.getInventory();
		inventory.clear();
		inventory.setItem(0, Items.SERVER_SELECTOR);
	}
}
