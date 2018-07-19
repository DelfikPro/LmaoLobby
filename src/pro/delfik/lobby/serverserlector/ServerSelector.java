package pro.delfik.lobby.serverserlector;

import lib.Generate;
import lib.gui.GeneralizedGUI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import pro.delfik.lmao.core.connection.PacketEvent;
import pro.delfik.lmao.util.U;
import pro.delfik.lobby.Items;
import pro.delfik.lobby.serverserlector.serverinfo.ServerInfo;
import pro.delfik.net.packet.PacketSSU;

public class ServerSelector implements Listener {
	
	public static final GeneralizedGUI gui = new GeneralizedGUI(Bukkit.createInventory(null, 9, "Каталог серверов"),
									(p, i) -> U.send(p.getName(), ServerInfo.list.get(i).getName()), null);
	
	@EventHandler
	public void onPacketReceived(PacketEvent e) {
		if (!(e.getPacket() instanceof PacketSSU)) return;
		PacketSSU packet = ((PacketSSU) e.getPacket());
		
		int players = packet.getOnline();
		String server = packet.getServer();
		
		ServerInfo info = ServerInfo.get(server);
		if (info == null) {
			gui.put(gui.getInventory().firstEmpty(), ServerInfo.construct(server, players).constructItem(), null);
			return;
		}
		
		if (players != -1) info.setPlayers(players);
		else info.setOnline(false);
		
		info.update(gui);
		for (Player p : Bukkit.getOnlinePlayers()) p.updateInventory();
	}
	
	public void changeServerStatus(String server, boolean online) {
		ServerInfo info = ServerInfo.get(server);
		if (info.getOnline() == online) return;
		info.setOnline(online);
		info.update(gui);
	}
	
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
		if (Generate.equalsItem(e.getPlayer().getItemInHand(), Items.SERVER_SELECTOR)) {
			e.getPlayer().openInventory(gui.getInventory());
		}
	}
	
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getWhoClicked().getGameMode() == GameMode.CREATIVE) return;
		e.setCancelled(true);
	}
}

