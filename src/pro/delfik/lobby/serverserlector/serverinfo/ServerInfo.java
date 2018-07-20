package pro.delfik.lobby.serverserlector.serverinfo;

import lib.Converter;
import lib.gui.GeneralizedGUI;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pro.delfik.lobby.serverserlector.ServerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ServerInfo {
	private final int number;
	private int players;
	public static final List<ServerInfo> list = new ArrayList<>();
	public static final HashMap<String, ServerInfo> byName = new HashMap<>();

	protected ServerInfo(int number) {
		this(number, 0);
	}
	protected ServerInfo(int number, int players) {
		this.number = number;
		this.players = players;
		list.add(this);
		byName.put(getName(), this);
	}

	public int getNumber() {
		return number;
	}
	
	public int getPlayers() {
		return players;
	}
	
	public void setPlayers(int players) {
		this.players = players;
	}
	
	public ItemStack constructItem() {
		ItemStack copy = getType().item.clone();
		int players = getPlayers();
		ItemMeta m = copy.getItemMeta();
		m.setDisplayName(constructDisplayName());
		copy.setItemMeta(m);
		copy.setAmount(players == 0 ? 1 : players);
		return copy;
	}
	
	public abstract ServerType getType();
	
	public String getName() {
		return getType().shortcut + "_" + number;
	}
	
	
	public static ServerInfo construct(String name, int players) {
		boolean online = true;
		System.out.println(name);
		int number = Integer.parseInt(name.replaceAll(".*_", ""));
		ServerType type = ServerType.parse(name.replaceAll("_.*", ""));
		ServerInfo i;
		switch (type) {
			case LOBBY:
				i = new LobbyInfo(number, players);
				break;
			default:
				i = instanceFor(type, number, players);
		}
		return i;
	}
	
	private static ServerInfo instanceFor(ServerType type, int number, int players) {
		return new ServerInfo(number, players) {
			@Override
			public ServerType getType() {
				return type;
			}
		};
	}
	
	protected String constructDisplayName() {
		String postfix = players + " §eигрок" + Converter.plural(players, "", "а", "ов");
		return "§f§l" + getType().name + " " + getNumber() + " §e[§f" + postfix + "§e]";
	}
	
	public static ServerInfo get(String server) {
		return byName.get(server);
	}
	
	public void update(GeneralizedGUI gui) {
		gui.put(list.indexOf(this), constructItem(), null);
	}
}
