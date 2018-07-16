package pro.delfik.lobby.serverserlector.serverinfo;

import lib.Generate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pro.delfik.lobby.serverserlector.ServerType;

import java.util.List;

public class BedwarsInfo extends ServerInfo {
	
	private String map;
	private String state;
	public static List<ServerInfo> getList() {
		return null;
	}
	
	public BedwarsInfo(int number, boolean online, int players, String map, String state) {
		super(number, online, players);
		this.map = map;
		this.state = state;
	}
	
	@Override
	public ItemStack constructItem() {
		Material m;
		String comment;
		switch (state.toUpperCase()) {
			
			case "GAME": comment = "Идёт игра"; break;
			case "STOPPED": comment = "Набор игроков"; break;
			case "COOLDOWN": comment = "Идёт отсчёт до начала игры"; break;
			case "END": comment = "Конец игры"; break;
			default: comment = "Сервер выключен"; break;
		}
		String display = constructDisplayName();
		if (!getOnline()) return Generate.itemstack(Material.BED, -1, 0, display, "§c§lСервер оффлайн.");
		return Generate.itemstack(Material.BED, getPlayers() == 0 ? 1 : getPlayers(), 0, display, "§f", "§fКарта: §l" + map, "§f",
				"§f§l" + comment, "§e> §aНажмите, чтобы подключиться §e<");
	}
	
	@Override
	public ServerType getType() {
		return ServerType.BEDWARS;
	}
	
}
