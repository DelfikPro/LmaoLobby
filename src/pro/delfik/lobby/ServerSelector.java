package pro.delfik.lobby;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import pro.delfik.lmao.ev.added.PacketEvent;
import pro.delfik.lmao.outward.gui.GeneralizedGUI;
import pro.delfik.lmao.util.U;
import implario.net.packet.PacketSSU;

public class ServerSelector implements Listener {
	private static final GeneralizedGUI gui = new GeneralizedGUI(Bukkit.createInventory(null, 9, "Каталог серверов"),
									(p, i) -> U.send(p.getName(), ServerInfo.list.get(i).getName()), null);
	
	@EventHandler
	public void event(PacketEvent event) {
		if (!(event.getPacket() instanceof PacketSSU)) return;
		PacketSSU packet = ((PacketSSU) event.getPacket());
		int players = packet.getOnline();
		String server = packet.getServer();
		ServerInfo info = ServerInfo.get(server);
		if (info == null) {
			gui.put(gui.getInventory().firstEmpty(), ServerInfo.construct(server, players).constructItem(), null);
			return;
		}
		info.setPlayers(players);
		info.update(gui);
		for (Player p : Bukkit.getOnlinePlayers()) p.updateInventory();
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		if(!(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) return;
		if(event.getMaterial() == Material.COMPASS)
			event.getPlayer().openInventory(gui.getInventory());
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getWhoClicked().getGameMode() == GameMode.CREATIVE) return;
		event.setCancelled(true);
	}
}

