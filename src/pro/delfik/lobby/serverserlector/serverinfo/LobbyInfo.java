package pro.delfik.lobby.serverserlector.serverinfo;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pro.delfik.lobby.serverserlector.ServerType;

import java.util.List;

public class LobbyInfo extends ServerInfo {
	
	public LobbyInfo(int number, int players) {
		super(number, players);
	}
	
	@Override
	public ItemStack constructItem() {
		ItemStack i = super.constructItem();
		if (Bukkit.getMotd().equals(getName())) {
			ItemMeta m = i.getItemMeta();
			List<String> lore = m.getLore();
			lore.add("§e>> §f§lВы здесь §e<<");
			m.setLore(lore);
			i.setItemMeta(m);
		}
		return i;
	}
	
	@Override
	public ServerType getType() {
		return ServerType.LOBBY;
	}
}
