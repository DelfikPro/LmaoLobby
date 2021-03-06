package pro.delfik.lobby;

import implario.util.Converter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import implario.util.ServerType;
import pro.delfik.lmao.outward.Generate;
import pro.delfik.lmao.outward.gui.GeneralizedGUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerInfo {
	private final int number;
	private final ServerType type;
	private int players;
	public static final List<ServerInfo> list = new ArrayList<>();
	public static final HashMap<String, ServerInfo> byName = new HashMap<>();

	private ServerInfo(ServerType type, int number, int players) {
		this.number = number;
		this.players = players;
		this.type = type;
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
		ItemStack copy = itemstacks.get(getType()).clone();
		int players = getPlayers();
		ItemMeta m = copy.getItemMeta();
		m.setDisplayName(constructDisplayName());
		copy.setItemMeta(m);
		copy.setAmount(players == 0 ? 1 : players);
		return copy;
	}
	
	public ServerType getType(){
		return type;
	}
	
	public String getName() {
		return getType().toString() + "_" + number;
	}
	
	public static ServerInfo construct(String name, int players) {
		int number = Integer.parseInt(name.replaceAll(".*_", ""));
		ServerType type = ServerType.getType(name);
		return new ServerInfo(type, number, players);
	}
	
	protected String constructDisplayName() {
		String postfix = players + " §eигрок" + Converter.plural(players, "", "а", "ов");
		return "§f§l" + getType().toString() + " " + getNumber() + " §e[§f" + postfix + "§e]";
	}
	
	public static ServerInfo get(String server) {
		return byName.get(server);
	}
	
	public void update(GeneralizedGUI gui) {
		gui.put(list.indexOf(this), constructItem(), null);
	}

	private static HashMap<ServerType, ItemStack> itemstacks = new HashMap<>();

	static{
		itemstacks.put(ServerType.LOBBY, Generate.itemstack(Material.SEA_LANTERN, 1, 0,
				"§a§lЛобби", "§eМесто, где можно пообщаться с другими игроками,",
				"§eНайти себе друзей или просто", "§eВесело провести время"));
		itemstacks.put(ServerType.BW, Generate.itemstack(Material.BED, 1, 0,
				"§c§lBedWars", "§eВсем известные кроватные войны"));
		itemstacks.put(ServerType.SF, Generate.itemstack(Material.STICK, 1, 0,
				"§6§lStickFight","§eМини-игра, в которой тебе придётся",
				"§eОбхитрять противника и скидывать его!"));
		itemstacks.put(ServerType.B, Generate.itemstack(Material.GRASS, 1, 0,
				"§d§lСтроительные сервера", "§eДоступно с ранга §2Строитель"));
		itemstacks.put(ServerType.TEST, Generate.itemstack(Material.COMMAND, 1, 0,
				"§d§lСервер для администрации.", "§eВсё самое секретное",
				"§eПроисходит именно здесь!"));
		itemstacks.put(ServerType.PVP, Generate.itemstack(Material.BOW, 1, 0,
				"§c§lP§fv§c§lP", "§eОтточи своё умение драться", "§eВ наших побоищах!"));
		itemstacks.put(ServerType.UHC, Generate.itemstack(Material.GOLDEN_APPLE, 1, 0,
				"§6§lUHC §6Meetup", "§eУБИВАТБ!", "§eУБИВАТБ!", "§eУБИВАТБ!"));
		itemstacks.put(ServerType.SPLEEF, Generate.itemstack(Material.BOW, 1, 0, ""));
		itemstacks.put(ServerType.UNKNOWN, Generate.itemstack(Material.BOW, 1, 0, ""));
		//TODO
	}
}
