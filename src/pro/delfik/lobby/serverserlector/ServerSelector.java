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
import pro.delfik.lmao.core.connection.database.ServerIO;
import pro.delfik.lmao.core.connection.handle.SocketEvent;
import pro.delfik.lmao.util.U;
import pro.delfik.lobby.Items;
import pro.delfik.lobby.OnlineHandler;
import pro.delfik.lobby.serverserlector.serverinfo.ServerInfo;

public class ServerSelector implements Listener {
	
	public static final GeneralizedGUI gui = new GeneralizedGUI(Bukkit.createInventory(null, 27, "Каталог серверов")
	, (p, i) -> U.send(p.getName(), ServerInfo.list.get(i).getName()), null);
	
	public static void init() {
		String query = ServerIO.connect("servers LOBBY_1");
		System.out.println(query);
		String[] servers = query.split("\n");
		for (int i = 0; i < servers.length; i++) {
			System.out.println(servers[i]);
			gui.put(i, ServerInfo.construct(servers[i]).constructItem(), null);
		}
	}
	
	public static void updateInOtherLobbies() {
		ServerIO.event("SSU", Bukkit.getMotd() + "/" + OnlineHandler.getOnline(), "LOBBY_1");
	}
	
	@EventHandler
	public void onSocket(SocketEvent e) {
		try {
			if (e.getChannel().equals("SSU-offline")) {
				changeServerStatus(e.getMsg(), false);
				return;
			}
			if (e.getChannel().equals("SSU-online")) {
				changeServerStatus(e.getMsg(), true);
				return;
			}
			if (!e.getChannel().equals("SSU")) return;
			String[] args = e.getMsg().split("/");
			ServerInfo info = ServerInfo.get(args[0]);
			if (info == null) {
				Bukkit.getLogger().info("§cСервер §e" + args[0] + " §cне зарегистрирован.");
				return;
			}
			info.updateInfo(args);
			info.update(gui);
			for (Player p : Bukkit.getOnlinePlayers()) p.updateInventory();
		} catch (Exception ignored) {ignored.printStackTrace();}
	}
	
	public void changeServerStatus(String server, boolean online) {
		ServerInfo info = ServerInfo.get(server);
		if (info.getOnline() == online) return;
		info.updateStatus(online);
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
		if (!e.getWhoClicked().getWorld().getName().startsWith("LOBBY_")) return;
		if (e.getWhoClicked().getGameMode() == GameMode.CREATIVE) return;
		e.setCancelled(true);
	}
	
}

